package com.dreamhouse.memcoupon.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface MemCouponRepository extends JpaRepository<MemCouponVO, MemCouponCompositeKey>{

	@Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
	List<MemCouponVO> findNotUsedCoupon(Integer useStatus0);
	
	@Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
	List<MemCouponVO> findUsedCoupon(Integer useStatus1);
	
	@Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
	List<MemCouponVO> findExpiredCoupon(Integer useStatus2);
	
}
