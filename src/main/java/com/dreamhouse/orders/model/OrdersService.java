package com.dreamhouse.orders.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository ordersrepo;

	@Transactional
	public void addOrder(OrdersVO ordersVO) {
		ordersrepo.save(ordersVO);
	}

	public void updateOrder(OrdersVO ordersVO) {
		ordersrepo.save(ordersVO);
	}

	// 取消訂單
	public boolean cancelOrder(Integer orderId) {
		OrdersVO order = getByOrderID(orderId);
		if (order != null && (OrderStatus.PENDING_PAYMENT.getLabel().equals(order.getOrderStatus())
				|| OrderStatus.PENDING_SHIPMENT.getLabel().equals(order.getOrderStatus()))) {
			order.setOrderStatus(OrderStatus.CANCELLED.getLabel());
			order.setOrderUpdateTime(Timestamp.valueOf(LocalDateTime.now()));
			ordersrepo.save(order);
			return true;
		}
		return false;
	}

	// 退貨申請
	public boolean returnOrder(Integer orderId, String returnReason, String returnText) {
		OrdersVO order = getByOrderID(orderId);
		if (order != null && (OrderStatus.SHIPPED.getLabel().equals(order.getOrderStatus())
				|| OrderStatus.COMPLETED.getLabel().equals(order.getOrderStatus()))) {
			order.setReturnStatus(ReturnStatus.RETURNING.getLabel());
			order.setReturnReason(returnReason);
			order.setReturnText(returnText);
			order.setReturnCreateTime(Timestamp.valueOf(LocalDateTime.now()));
			ordersrepo.save(order);
			return true;
		}
		return false;
	}

	public void updateReturnStatus(Integer orderId, ReturnStatus returnStatus, RefusalReason refusalReason) {
		LocalDateTime now = LocalDateTime.now();
		Timestamp ts = Timestamp.valueOf(now);

		OrdersVO ordersVO = new OrdersVO();

		ordersVO.setOrderId(orderId);

		// 存 VO 時用 label
		ordersVO.setReturnStatus(returnStatus.getLabel());

		if (returnStatus == ReturnStatus.REJECTED && refusalReason != null) {
			ordersVO.setRefusalReason(refusalReason.getLabel());
		}

		if (returnStatus == ReturnStatus.RETURNING) {
			// RETURNING 狀態，更新退貨創建時間與審核時間
			ordersVO.setReturnCreateTime(ts);
			ordersVO.setReturnApproveTime(ts);
		} else if (returnStatus == ReturnStatus.REJECTED) {
			// REJECTED 狀態，只更新審核時間
			ordersVO.setReturnApproveTime(ts);
		}

		ordersrepo.save(ordersVO);
	}

	public void delete(Integer orderId) {
		if (ordersrepo.existsById(orderId)) {
			ordersrepo.deleteById(orderId);
		}

	}

	public OrdersVO getByOrderID(Integer orderId) {
		Optional<OrdersVO> optional = ordersrepo.findById(orderId);
		return optional.orElse(null); // 如果值存在就回傳其值，沒找到回傳 null
	}

	public List<OrdersVO> getAll() {
		return ordersrepo.findAll();
	}

	public List<OrdersVO> getAllReturn() {
		return ordersrepo.findAllReturn();
	}

	/**
	 * 確認訂單出貨 - 更新訂單狀態為已出貨
	 * @param orderId 訂單ID
	 * @return 是否更新成功
	 */
	@Transactional
	public boolean confirmShipment(Integer orderId) {
		try {
			// 查詢訂單
			Optional<OrdersVO> optional = ordersrepo.findById(orderId);
			if (!optional.isPresent()) {
				return false;
			}

			OrdersVO order = optional.get();

			// 檢查當前狀態是否為「待出貨」
			if (!OrderStatus.PENDING_SHIPMENT.getLabel().equals(order.getOrderStatus())) {
				return false;
			}

			// 更新狀態和時間
			order.setOrderStatus(OrderStatus.SHIPPED.getLabel());
			order.setOrderUpdateTime(Timestamp.valueOf(LocalDateTime.now()));

			ordersrepo.save(order);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<OrdersVO> getOrdersByMemberId(Integer memberId) {
		return ordersrepo.findByMemberId(memberId);
	}

	public OrdersVO getMemberOrderDetail(Integer orderId, Integer memberId) {
		return ordersrepo.findByOrderIdAndMemberId(orderId, memberId).orElse(null);
	}

	public List<OrdersVO> findOrdersByConditions(Integer memberId, LocalDate startDate, LocalDate endDate,
			String orderStatus) {

		return ordersrepo.findOrdersByConditions(memberId, startDate, endDate, orderStatus);
	}

}
