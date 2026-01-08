package com.dreamhouse.mem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dreamhouse.mem.dto.LoginForm;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.memcoupon.model.MemCouponService;
import com.dreamhouse.memcoupon.model.MemCouponVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mem")
@SessionAttributes("verifyCode")
public class MemCouponController {

    @Autowired
    private MemService memService;

    @Autowired
    private MemCouponService memCouponService;

    @GetMapping("/my-coupon")
    public String showMemberCoupons(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/mem/login"; // 未登入 → 導回登入
        }

        MemVO member = memService.findById(memberId);

        // 可使用的券
        List<MemCouponVO> availableCoupons = memCouponService.findAvailableCoupons(member);
        // 已使用 / 已過期的券
        List<MemCouponVO> usedOrExpiredCoupons = memCouponService.findUsedOrExpiredCoupons(member);

        model.addAttribute("memberVO", member);
        model.addAttribute("availableCoupons", availableCoupons);
        model.addAttribute("usedOrExpiredCoupons", usedOrExpiredCoupons);


        return "front-end/mem/coupons"; // 對應 Thymeleaf 頁面
    }

    
}