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

	@Scheduled(cron = "0 43 15 * * ?")
	void sendCouponToMember() {
		// ●取得所有memberId
		List<MemVO> memList = memSer.findActiveMem();

		// ●取得coupon state==1 && startDt==now()，並新增會員優惠券
		List<CouponVO> activeCoupList = coupSer.getActiveCoupon();

		memCoupSer.addMemCoupon(activeCoupList, memList);
		coupSer.updateSendTime(activeCoupList);
		System.out.println("新增成功");
	}
}