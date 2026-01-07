package com.dreamhouse.coupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.memcoupon.model.MemCouponService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	CouponService coupSer;
	
	@Autowired
	MemCouponService memCoupSer;
	
	@GetMapping("addCoupon")
	public String addCoupon(ModelMap model) {
		CouponVO couponVO = new CouponVO();
		model.addAttribute("couponVO", couponVO);
		return "back-end/coupon/coupon_add";
	}
	
	@PostMapping("insert")
	public String insert(@Valid CouponVO couponVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/coupon/coupon_add";
		}
		
		coupSer.addCoupon(couponVO);
		List<CouponVO> list = coupSer.getAll();
		model.addAttribute("couponList", list);
		return "back-end/coupon/coupon_list";
	}
	
	
	@GetMapping("getOneForUpdate")
	public String getOneForUpdate(@RequestParam("couponId") String couponId, ModelMap model) {
		CouponVO couponVO = coupSer.getOneById(Integer.valueOf(couponId));
		model.addAttribute("couponVO", couponVO);
		return "back-end/coupon/coupon_edit";
	}
	
	@PostMapping("updateCoupon")
	public String updateCoupon(@Valid CouponVO couponVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/coupon/coupon_edit";
		}
		
		coupSer.updateCoupon(couponVO);
		couponVO = coupSer.getOneById(Integer.valueOf(couponVO.getCouponId()));
		model.addAttribute("couponVO", couponVO);
		return "back-end/coupon/coupon_viewOne";
	}
	
	
	
	@GetMapping("getAllCoupon")
	public String getAllCoupon(ModelMap model) {
		List<CouponVO> list = coupSer.getAll();
		model.addAttribute("couponList", list);
		return "back-end/coupon/coupon_list";
	}
	
	@GetMapping("viewCoupon")
	public String getOneById(@RequestParam("couponId") String couponId, ModelMap model) {
		CouponVO couponVO = coupSer.getOneById(Integer.valueOf(couponId));
		model.addAttribute("couponVO", couponVO);
		return "back-end/coupon/coupon_viewOne";
	}
	
	
}
