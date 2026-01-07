package com.dreamhouse.mem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dreamhouse.mem.dto.LoginForm;
import com.dreamhouse.mem.model.MemService;

@Controller
@RequestMapping("/mem/forgot-password")
@SessionAttributes("verifyCode")
public class ForgetPasswordController {

    @Autowired
    private MemService memService;

    // Step 0: 顯示忘記密碼頁面
    @GetMapping
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("step", 1);
        return "front-end/mem/forgot-password";
    }

    // Step 1: 寄送驗證碼
    @PostMapping("/send")
    public String sendCode(@RequestParam("email") String email, Model model) {
        String code = String.valueOf((int) (Math.random() * 900000 + 100000));
        boolean ok = memService.sendVerificationCode(email, code);

        if (ok) {
            model.addAttribute("email", email);
            model.addAttribute("verifyCode", code);
            model.addAttribute("successMsg", "驗證碼已寄送到您的信箱");
            model.addAttribute("step", 1);
        } else {
            model.addAttribute("errorMsg", "信箱不存在");
            model.addAttribute("step", 1);
        }
        return "front-end/mem/forgot-password";
    }

    // Step 2: 驗證碼
    @PostMapping("/verify")
    public String verifyCode(@RequestParam("email") String email,
                             @RequestParam("code") String inputCode,
                             @ModelAttribute("verifyCode") String verifyCode,
                             Model model,
                             SessionStatus sessionStatus) {
        if (inputCode.equals(verifyCode)) {
            // ✅ 驗證成功後清除 Session 裡的驗證碼，確保一次性
            sessionStatus.setComplete();

            model.addAttribute("email", email);
            model.addAttribute("successMsg", "驗證成功，請設定新密碼");
            model.addAttribute("step", 2);
        } else {
            model.addAttribute("email", email);
            model.addAttribute("errorMsg", "驗證碼錯誤");
            model.addAttribute("step", 1);
        }
        return "front-end/mem/forgot-password";
    }

    // Step 3: 重設密碼
    @PostMapping("/reset")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {
        if (newPassword.length() < 8) {
        	model.addAttribute("email", email);
            model.addAttribute("errorMsg", "密碼至少需要 8 碼");
            model.addAttribute("step", 2);
            return "front-end/mem/forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
        	model.addAttribute("email", email);
            model.addAttribute("errorMsg", "兩次密碼不一致");
            model.addAttribute("step", 2);
            return "front-end/mem/forgot-password";
        }

        boolean ok = memService.resetPassword(email, newPassword);
        if (ok) {
            model.addAttribute("resetSuccess", true);
            model.addAttribute("step", 2);
            return "front-end/mem/forgot-password";
        } else {
            model.addAttribute("errorMsg", "更新密碼失敗，帳號不存在");
            model.addAttribute("step", 1);
            return "front-end/mem/forgot-password";
        }
    }
}