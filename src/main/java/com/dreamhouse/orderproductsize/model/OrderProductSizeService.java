package com.dreamhouse.orderproductsize.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderProductSizeService {

	@Autowired
	private OrderProductSizeRepository repository;

	/**
	 * 根據訂單 ID 查詢訂單明細
	 * @param orderId 訂單 ID
	 * @return 訂單明細列表
	 */
	@Transactional(readOnly = true)
	public List<OrderProductSizeVO> getByOrderId(Integer orderId) {
		return repository.findByOrderId(orderId);
	}

	/**
	 * 根據訂單 ID 查詢訂單明細（含關聯的商品和尺寸資訊）
	 * 使用 JOIN FETCH 優化，一次查詢獲取所有需要的資料
	 * @param orderId 訂單 ID
	 * @return 訂單明細列表（含關聯資料）
	 */
	@Transactional(readOnly = true)
	public List<OrderProductSizeVO> getByOrderIdWithDetails(Integer orderId) {
		return repository.findByOrderIdWithDetails(orderId);
	}

	/**
	 * 查詢所有訂單明細
	 * @return 所有訂單明細
	 */
	public List<OrderProductSizeVO> getAll() {
		return repository.findAll();
	}

	/**
	 * 根據 ID 查詢單筆訂單明細
	 * @param id 訂單明細 ID
	 * @return 訂單明細
	 */
	public OrderProductSizeVO getById(Integer id) {
		return repository.findById(id).orElse(null);
	}

	/**
	 * 新增訂單明細
	 * @param orderProductSizeVO 訂單明細物件
	 */
	@Transactional
	public void add(OrderProductSizeVO orderProductSizeVO) {
		repository.save(orderProductSizeVO);
	}
}
