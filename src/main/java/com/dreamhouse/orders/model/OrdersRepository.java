package com.dreamhouse.orders.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersRepository extends JpaRepository<OrdersVO, Integer>{

	@Query("SELECT ovo FROM OrdersVO ovo WHERE ovo.returnReason IS NOT NULL AND ovo.returnStatus IS NOT NULL")
	List<OrdersVO> findAllReturn();
}
