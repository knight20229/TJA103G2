package com.dreamhouse.memcoupon.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamhouse.coupon.model.CouponRepository;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.mem.model.MemRepository;
import com.dreamhouse.mem.model.MemVO;

@Service("memCouponMappingService")
public class MemCouponService {

	@Autowired
	MemCouponRepository memCoupRepo;

	@Autowired
	CouponRepository coupRepo;

	@Autowired
	MemRepository memRepo;

	@Transactional
	public void addMemCoupon(List<CouponVO> activeCoupList, Integer memId) {
		// ●取得所有memberId
		Optional<MemVO> optional = memRepo.findById(memId);
		MemVO mem = optional.orElse(null);

		// ●取得coupon state==1 && startDt==now()，並新增會員優惠券
		for (CouponVO couponVO : activeCoupList) {

			MemCouponCompositeKey key = new MemCouponCompositeKey();
			key.setMemberId(mem.getMemberId());
			MemCouponVO memCoup = new MemCouponVO();
			memCoup.setCouponId(couponVO);
			memCoup.setMemCouponKey(key);
			memCoup.setUseStatus(0);
			memCoupRepo.save(memCoup);
		}
	}

}
