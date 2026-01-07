package com.dreamhouse.coupon.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamhouse.memcoupon.model.MemCouponService;

@Service("couponService")
public class CouponService {
	
	@Autowired
	CouponRepository repository;
	
	
	@Transactional
	public void addCoupon(CouponVO couponVO) {
		repository.save(couponVO);
	}
	
	@Transactional
	public void updateCoupon(CouponVO couponVO) {
		repository.save(couponVO);
	}
	
	
	public CouponVO getOneById(Integer couponId) {
		Optional<CouponVO> optional = repository.findById(couponId);
		return optional.orElse(null);
	}
	
	public List<CouponVO> getAll(){
		return repository.findAll();
	}
	
	// 發送優惠券
	public List<CouponVO> getActiveCoupon(){
		return repository.findActiveCoupon(1, LocalDate.now());
	}
	
	public void updateSendTime(CouponVO couponVO){
		repository.save(couponVO);
	}
	
	
	// 給排程更新優惠券狀態用
	public void updateState() {
		List<CouponVO> coupList = repository.findByState(1);
		for (CouponVO couponVO : coupList) {
			if (couponVO.getEndDt().equals(LocalDate.now())) {
				couponVO.setState(0);
				repository.save(couponVO);
			}
		}
	}
}

