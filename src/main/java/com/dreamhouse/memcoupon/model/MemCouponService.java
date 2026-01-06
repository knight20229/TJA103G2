package com.dreamhouse.memcoupon.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;

@Service("memCouponMappingService")
public class MemCouponService {

	@Autowired
	MemCouponRepository memCoupRepo;

	@Autowired
	CouponService coupSer;

	@Autowired
	MemService memSer;

	@Transactional
	public void addMemCoupon(List<CouponVO> activeCoupList, List<MemVO> memList) {
		MemCouponCompositeKey key = new MemCouponCompositeKey();
		// ●取得所有會員狀態=1的memberId
		memList = memSer.findActiveMem();
		for (MemVO memVO : memList) {
			key.setMemberId(memVO.getMemberId());
		}

		
		// ●取得coupon state==1 && startDt==now()，並新增會員優惠券
		for (CouponVO couponVO : activeCoupList) {
			MemCouponVO memCoup = new MemCouponVO();
			memCoup.setCouponId(couponVO);
			memCoup.setMemCouponKey(key);
			memCoup.setUseStatus(0);
			memCoupRepo.save(memCoup);
		}
	}

	
	public List<MemCouponVO> getAll(){
		return memCoupRepo.findAll();
	}
}
