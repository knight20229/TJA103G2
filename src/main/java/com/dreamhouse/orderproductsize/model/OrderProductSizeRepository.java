package com.dreamhouse.orderproductsize.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductSizeRepository extends JpaRepository<OrderProductSizeVO, Integer> {

	// 根據訂單 ID 查詢訂單明細
	@Query("SELECT ops FROM OrderProductSizeVO ops WHERE ops.orders.orderId = :orderId")
	List<OrderProductSizeVO> findByOrderId(@Param("orderId") Integer orderId);

	// 根據訂單 ID 查詢訂單明細（含關聯資料，使用 JOIN FETCH 優化效能）
	@Query("SELECT ops FROM OrderProductSizeVO ops " +
			"LEFT JOIN FETCH ops.prodSizeConnect psc " +
			"LEFT JOIN FETCH psc.prodVO " +
			"LEFT JOIN FETCH psc.sizeVO " +
			"WHERE ops.orders.orderId = :orderId")
	List<OrderProductSizeVO> findByOrderIdWithDetails(@Param("orderId") Integer orderId);

}
