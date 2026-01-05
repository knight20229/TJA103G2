package com.dreamhouse.orders.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.orderproductsize.model.OrderProductSizeService;
import com.dreamhouse.orderproductsize.model.OrderProductSizeVO;
import com.dreamhouse.orders.model.OrdersService;
import com.dreamhouse.orders.model.OrdersVO;
import com.dreamhouse.prod.model.ProdSizeConnectVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/orders")
public class OrdersController {

	@Autowired
	OrdersService ordersSvc;

	@Autowired
	OrderProductSizeService orderProductSizeSvc;

	// 訂單列表頁面（含訂單明細）
	@GetMapping("/list")
	public String listAllOrders(ModelMap model) {
		List<OrdersVO> orders = ordersSvc.getAll();

		// 為每個訂單組裝明細資料
		List<Map<String, Object>> orderListWithDetails = new ArrayList<>();
		for (OrdersVO order : orders) {
			try {
				Map<String, Object> orderData = new HashMap<>();

				// 訂單基本資訊
				orderData.put("order", order);

				// 查詢並組裝訂單明細（使用 JOIN FETCH 一次查詢）
				List<OrderProductSizeVO> items = orderProductSizeSvc.getByOrderIdWithDetails(order.getOrderId());
				List<Map<String, Object>> details = new ArrayList<>();

				for (OrderProductSizeVO item : items) {
					Map<String, Object> detail = new HashMap<>();

					// 基本資訊（所有明細都有）
					detail.put("quantity", item.getQuantity());
					detail.put("price", item.getPrice());
					detail.put("isCustom", item.getIsCustom());

					if (item.getIsCustom()) {
						detail.put("productName", "客製化商品");
						detail.put("productImageBase64", null);
						detail.put("width", null);
						detail.put("length", null);

					} else {
						// 透過 JPA 關聯獲取商品和尺寸資訊
						ProdSizeConnectVO psc = item.getProdSizeConnect();
						if (psc != null && psc.getProdVO() != null && psc.getSizeVO() != null) {
							detail.put("productName", psc.getProdVO().getProductName());
							detail.put("productImageBase64", convertBlobToBase64(psc.getProdVO().getImageData()));
							detail.put("width", psc.getSizeVO().getWidth());
							detail.put("length", psc.getSizeVO().getLength());
						} else {
							detail.put("productName", "商品資訊不存在");
							detail.put("productImageBase64", null);
							detail.put("width", null);
							detail.put("length", null);
						}
					}

					details.add(detail);
				}

				orderData.put("details", details);
				orderListWithDetails.add(orderData);

			} catch (Exception e) {
				// 如果某個訂單處理失敗，記錄錯誤但繼續處理其他訂單
				System.err.println("處理訂單 ID " + order.getOrderId() + " 時發生錯誤: " + e.getMessage());
				e.printStackTrace();
			}
		}

		model.addAttribute("orderListWithDetails", orderListWithDetails);
		return "back-end/orders/listAllOrders";
	}

	@GetMapping("addOrders")
	public String addOrdersPage(ModelMap model) {
		OrdersVO ordersVO = new OrdersVO();
		model.addAttribute("OrdersVO", ordersVO);
		return "back-end/orders/addOrders";
	}

	@PostMapping("insert")
	public String insert(@Valid OrdersVO ordersVO, BindingResult result, ModelMap model) throws IOException {
		if (result.hasErrors()) {
			return "back-end/orders/addOrder";
		}
		ordersSvc.addOrder(ordersVO);
		List<OrdersVO> list = ordersSvc.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success","-(新增成功)");
		return "redirect:/orders/listAllOrder"; // 新增成功後重導至IndexController_inSpringBoot.java
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("ordersId") String ordersId, Model model) {
		OrdersVO ordersVO = ordersSvc.getByOrderID(Integer.valueOf(ordersId));
		model.addAttribute("OrdersVO", ordersVO);
		return "back-end/orders/update_order_input";
	}
	
	@PostMapping("update")
	public String update(@Valid OrdersVO ordersVO,BindingResult result, ModelMap model) throws IOException{
		if (result.hasErrors()) {
			return "back-end/orders/update_order_input";
		}
		ordersSvc.updateOrder(ordersVO);

		model.addAttribute("success", "- (修改成功)");
		ordersVO = ordersSvc.getByOrderID(Integer.valueOf(ordersVO.getOrderId()));
		model.addAttribute("ordersVO", ordersVO);
		return "back-end/orders/listOneOrder";
	}

	/**
	 * 將 BLOB 圖片轉換為 Base64 字串
	 */
	private String convertBlobToBase64(byte[] imageData) {
		if (imageData == null || imageData.length == 0) {
			return null;
		}
		return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);
	}
}
