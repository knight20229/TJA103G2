package com.dreamhouse.promotions.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PromotionsRepository extends JpaRepository<PromotionsVO, Integer> {

	@Query(value = "select * from promotions where state=?1 and start_dt<=?2 and end_dt>=?2 order by promotions_id", nativeQuery = true)
	List<PromotionsVO> findActivePromotions(Integer state1, LocalDate today);

	@Query(value = "select * from promotions where state=?1 order by promotions_id", nativeQuery = true)
	List<PromotionsVO> findByState(Integer state1);

}