package com.dreamhouse.orders.controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dreamhouse.orders.model.OrderStatus;
import com.dreamhouse.orders.model.OrdersService;
import com.dreamhouse.orders.model.OrdersVO;
import com.dreamhouse.orders.model.ReturnReason;
import com.dreamhouse.orders.model.ReturnStatus;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/front/orders")
public class OrdersFrontController {

	@Autowired
	private OrdersService ordersSvc;

	// =========================
	// 前台：我的訂單列表
	// =========================
	@GetMapping("/myOrders")
	public String myOrders(HttpSession session, Model model) {

		Integer memberId = (Integer) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/mem/login";
		}

		List<OrdersVO> ordersList = ordersSvc.getOrdersByMemberId(memberId);

		model.addAttribute("ordersList", ordersList);
		return "front-end/orders/myOrders";
	}

	// =========================
	// 前台：訂單明細
	// =========================
	@GetMapping("/detail")
	public String orderDetail(@RequestParam("orderId") Integer orderId, HttpSession session, Model model) {

		// ===== 開發測試用：可以直接在這裡改 memberId =====
		Integer testMemberId = 2; // 改成測試的會員ID
		session.setAttribute("memberId", testMemberId);
		// ========================================

		Integer memberId = (Integer) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/front/mem/login";
		}

		OrdersVO order = ordersSvc.getMemberOrderDetail(orderId, memberId);

		if (order == null) {
			return "redirect:/front/orders/myOrders";
		}

		model.addAttribute("ordersList", Arrays.asList(order));
		return "front-end/orders/myOrders";
	}

	@GetMapping("/front/orders/myOrders")
	public String myOrders(@RequestParam(required = false) Integer memberId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
			@RequestParam(required = false) String orderStatus, Model model) {

		// 開發測試用會員===================
		if (memberId == null) {
			memberId = 2;
		}
		// =============================

		List<OrdersVO> ordersList = ordersSvc.findOrdersByConditions(memberId, startDate, endDate, orderStatus);

		// 回傳給畫面
		model.addAttribute("ordersList", ordersList);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("orderStatus", orderStatus);

		return "front-end/orders/myOrders";
	}

	// =========================
	// 取消訂單
	// =========================
	@PostMapping("/cancel")
	public String cancelOrder(@RequestParam("orderId") Integer orderId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Integer memberId = (Integer) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/front/mem/login";
		}

		OrdersVO order = ordersSvc.getMemberOrderDetail(orderId, memberId);
		if (order != null && (OrderStatus.PENDING_PAYMENT.getLabel().equals(order.getOrderStatus())
				|| OrderStatus.PENDING_SHIPMENT.getLabel().equals(order.getOrderStatus()))) {

			order.setOrderStatus(OrderStatus.CANCELLED.getLabel());
			order.setOrderUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
			ordersSvc.updateOrder(order);
			redirectAttributes.addFlashAttribute("successMessage", "訂單 " + orderId + " 已成功取消！");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "訂單 " + orderId + " 無法取消！");
		}

		return "redirect:/front/orders/myOrders";
	}

	// =========================
	// 退貨申請
	// =========================
	@PostMapping("/return")
	public String returnOrder(@RequestParam("orderId") Integer orderId,
			@RequestParam("returnReason") String returnReason, @RequestParam("returnText") String returnText,
			HttpSession session, RedirectAttributes redirectAttributes) {
		Integer memberId = (Integer) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/front/mem/login";
		}

		OrdersVO order = ordersSvc.getMemberOrderDetail(orderId, memberId);
		if (order != null && (OrderStatus.SHIPPED.getLabel().equals(order.getOrderStatus())
				|| OrderStatus.COMPLETED.getLabel().equals(order.getOrderStatus()))) {

			order.setReturnStatus(ReturnStatus.APPLYING.getLabel());
			ReturnReason reasonEnum = ReturnReason.valueOf(returnReason); // 轉成 enum
			order.setReturnReason(reasonEnum.getLabel()); // 存中文
			order.setReturnText(returnText);
			order.setReturnCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			ordersSvc.updateOrder(order);

			redirectAttributes.addFlashAttribute("successMessage", "訂單 " + orderId + " 退貨申請已送出！");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "訂單 " + orderId + " 無法申請退貨！");
		}

		return "redirect:/front/orders/myOrders";
	}

}
