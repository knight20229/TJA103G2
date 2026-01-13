package com.dreamhouse.orders.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<OrdersVO, Integer> {

	@Query("SELECT ovo FROM OrdersVO ovo WHERE ovo.returnReason IS NOT NULL AND ovo.returnStatus IS NOT NULL")
	List<OrdersVO> findAllReturn();

	// 依會員查詢自己的所有訂單
	@Query("SELECT ovo FROM OrdersVO ovo WHERE ovo.memberId = :memberId ORDER BY ovo.orderCreateTime DESC")
	List<OrdersVO> findByMemberId(@Param("memberId") Integer memberId);

	Optional<OrdersVO> findByOrderIdAndMemberId(Integer orderId, Integer memberId);

	// 根據綠界交易編號查詢訂單
	Optional<OrdersVO> findByMerchantTradeNo(String merchantTradeNo);

	@Query("""
		    SELECT ovo FROM OrdersVO ovo
		    WHERE (:memberId IS NULL OR ovo.memberId = :memberId)
		    AND (:orderStatus IS NULL OR :orderStatus = '' OR ovo.orderStatus = :orderStatus)
		    AND (:startDate IS NULL OR ovo.orderCreateTime >= :startDate)
		    AND (:endDate IS NULL OR ovo.orderCreateTime <= :endDate)
		    ORDER BY ovo.orderCreateTime DESC
		""")
		List<OrdersVO> findOrdersByConditions(@Param("memberId") Integer memberId,
		        @Param("startDate") LocalDate startDate,
		        @Param("endDate") LocalDate endDate,
		        @Param("orderStatus") String orderStatus);
}
