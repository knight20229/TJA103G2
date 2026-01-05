package com.dreamhouse.coupon.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public void deleteCoupon(Integer couponId) {
		if(repository.existsById(couponId)) {
			repository.deleteById(couponId);
		}
	}
	
	public CouponVO getOneById(Integer couponId) {
		Optional<CouponVO> optional = repository.findById(couponId);
		return optional.orElse(null);
	}
	
	public List<CouponVO> getAll(){
		return repository.findAll();
	}
	
	// 發送優惠券用
	public List<CouponVO> getActiveCoupon(){
		return repository.findActiveCoupon(1, LocalDate.now());
	}
}

