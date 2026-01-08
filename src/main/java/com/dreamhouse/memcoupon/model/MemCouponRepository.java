package com.dreamhouse.memcoupon.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dreamhouse.mem.model.MemVO;


public interface MemCouponRepository extends JpaRepository<MemCouponVO, MemCouponCompositeKey>{

    // 查詢某會員的所有優惠券
    List<MemCouponVO> findByMemVO(MemVO member);

    // 查詢未使用的券
    @Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
    List<MemCouponVO> findNotUsedCoupon(Integer useStatus0);

    // 查詢已使用的券
    @Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
    List<MemCouponVO> findUsedCoupon(Integer useStatus1);

    // 查詢已過期的券
    @Query(value = "select * from member_coupon_mapping where use_status=?1 order by coupon_update_time", nativeQuery = true)
    List<MemCouponVO> findExpiredCoupon(Integer useStatus2);

}
