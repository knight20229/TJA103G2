package com.dreamhouse.coupon.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<CouponVO, Integer>{
	
	@Query(value = "select * from coupon where state=?1 and start_dt=?2 order by coupon_id", nativeQuery = true)
	List<CouponVO> findActiveCoupon(Integer state1, LocalDate startDt);
	
	@Query(value = "select * from coupon where state=?1 order by coupon_id", nativeQuery = true)
	List<CouponVO> findByState(Integer state1);
	
	
}
