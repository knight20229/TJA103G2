package com.dreamhouse.coupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.memcoupon.model.MemCouponService;

@Component
public class CouponScheduler {

	@Autowired
	MemCouponService memCoupSer;

	@Autowired
	CouponService coupSer;

	@Autowired
	MemService memSer;

	@Scheduled(cron = "0 21 19 * * ?")
	void sendCouponToMember() {
		// ●取得member
		List<MemVO> activeMemList = memSer.findActiveMem();

		// ●取得coupon
		List<CouponVO> activeCoupList = coupSer.getActiveCoupon();

		memCoupSer.addMemCoupon(activeCoupList, activeMemList);
		System.out.println("新增成功");
	}
	
	// 結束日期=當下日期，將狀態更新為停用
	@Scheduled(cron = "0 23 19 * * ?")
	void updateState() {
		coupSer.updateState();
		System.out.println("更新成功");
	}
}