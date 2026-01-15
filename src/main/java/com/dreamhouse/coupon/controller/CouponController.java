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
import com.dreamhouse.emp.model.EmpService;
import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.memcoupon.model.MemCouponService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	CouponService coupSer;
	
	@Autowired
	MemCouponService memCoupSer;
	
	@Autowired
	EmpService empSer;
	
	@GetMapping("addCoupon")
	public String addCoupon(HttpSession session, ModelMap model) {
		CouponVO couponVO = new CouponVO();
		model.addAttribute("couponVO", couponVO);
				
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = empSer.findById(employeeId);
		model.addAttribute("employeeName", empVO.getName());
		return "back-end/coupon/coupon_add";
	}
	
	@PostMapping("insert")
	public String insert(@Valid CouponVO couponVO, BindingResult result, HttpSession session, ModelMap model) {
		if (result.hasErrors()) {
			System.out.println("驗證失敗的原因：");
	        result.getAllErrors().forEach(error -> System.out.println(error.toString()));
			
	        Integer employeeId = (Integer)session.getAttribute("employeeId");
			EmpVO empVO = empSer.findById(employeeId);
			model.addAttribute("employeeName", empVO.getName());
	        return "back-end/coupon/coupon_add";
		}

		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = (EmpVO)empSer.findById(employeeId);
		couponVO.setEmpVO(empVO);
		coupSer.addCoupon(couponVO);
		List<CouponVO> list = coupSer.getAll();
		model.addAttribute("couponList", list);
		return "back-end/coupon/coupon_list";
	}
	
	
	@GetMapping("getOneForUpdate")
	public String getOneForUpdate(@RequestParam("couponId") String couponId, HttpSession session, ModelMap model) {
		CouponVO couponVO = coupSer.getOneById(Integer.valueOf(couponId));
		model.addAttribute("couponVO", couponVO);
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = empSer.findById(employeeId);
		model.addAttribute("employeeName", empVO.getName());
		return "back-end/coupon/coupon_edit";
	}
	
	@PostMapping("updateCoupon")
	public String updateCoupon(@Valid CouponVO couponVO, BindingResult result, HttpSession session, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/coupon/coupon_edit";
		}
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = (EmpVO)empSer.findById(employeeId);
		couponVO.setEmpVO(empVO);
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
