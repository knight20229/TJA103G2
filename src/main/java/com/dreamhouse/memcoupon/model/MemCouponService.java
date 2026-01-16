package com.dreamhouse.memcoupon.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	public void addMemCoupon(List<CouponVO> activeCoupList, List<MemVO> activeMemList) {
		activeCoupList = coupSer.getActiveCoupon();
		activeMemList = memSer.findActiveMem();
		boolean needUpdateSendTime = false;
		if (activeCoupList.isEmpty() || activeMemList.isEmpty()) {
			System.out.println("----------成功執行--------------");
			return;
		} else {
			for (CouponVO couponVO : activeCoupList) {
				for (MemVO memVO : activeMemList) {
					MemCouponCompositeKey key = new MemCouponCompositeKey();
					key.setCouponId(couponVO.getCouponId());
					key.setMemberId(memVO.getMemberId());

					if (!memCoupRepo.existsById(key)) {
						MemCouponVO memCoup = new MemCouponVO();
						memCoup.setMemCouponKey(key);
						memCoup.setCouponVO(couponVO);
						memCoup.setMemVO(memVO);
						memCoup.setUseStatus(0);
						memCoupRepo.save(memCoup);
						System.out.println("----------成功執行2--------------");
						needUpdateSendTime = true;

					}
				}

				if (needUpdateSendTime) {
					couponVO.setSendTime(LocalDateTime.now());
					coupSer.updateSendTime(couponVO);
					System.out.println("----------成功執行3--------------");

				}

			}
		}
	}

	public List<MemCouponVO> getAll() {
		return memCoupRepo.findAll();
	}

	// 定期檢查所有會員的優惠券，若已過期則更新狀態
	@Scheduled(fixedRate = 60000) // 每分鐘檢查一次
	@Transactional
	public void updateExpiredCoupons() {
		List<MemCouponVO> allCoupons = memCoupRepo.findAll();
		LocalDate now = LocalDate.now();

		for (MemCouponVO mc : allCoupons) {
			// 1. 未使用且已過期 → 更新為已過期
			if (mc.getUseStatus() == MemCouponVO.STATUS_UNUSED && mc.getCouponVO().getEndDt().isBefore(now)) {
				mc.setUseStatus(MemCouponVO.STATUS_EXPIRED);
				memCoupRepo.save(mc);

			}

			// 2. 已使用但過期 → 保持已使用，不改成過期
			if (mc.getUseStatus() == MemCouponVO.STATUS_USED && mc.getCouponVO().getEndDt().isBefore(now)) {

			}
		}
	}

	// 查詢會員所有優惠券
	public List<MemCouponVO> findByMember(MemVO member) {
		return memCoupRepo.findByMemVO(member);
	}

	// 查詢可使用的券（未使用 + 未過期）
	public List<MemCouponVO> findAvailableCoupons(MemVO member) {
		return memCoupRepo.findByMemVO(member).stream().filter(mc -> mc.getUseStatus() == MemCouponVO.STATUS_UNUSED)
				.filter(mc -> mc.getCouponVO().getEndDt().isAfter(LocalDate.now())).collect(Collectors.toList());

	}

	// 查詢已使用或已過期的券（同一區塊顯示）
	public List<MemCouponVO> findUsedOrExpiredCoupons(MemVO member) {
		return memCoupRepo.findByMemVO(member).stream()
				.filter(mc -> mc.getUseStatus() == MemCouponVO.STATUS_USED
						|| mc.getUseStatus() == MemCouponVO.STATUS_EXPIRED
						|| mc.getCouponVO().getEndDt().isBefore(LocalDate.now()))
				.collect(Collectors.toList());

	}

}
