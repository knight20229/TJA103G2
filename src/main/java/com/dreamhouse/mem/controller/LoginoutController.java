package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.dto.LoginForm;
import com.dreamhouse.mem.model.MemService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mem")
public class LoginoutController {

    @Autowired
    private MemService memService;

    // 顯示登入頁面
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        // 如果沒有 loginForm，才新增，避免覆蓋掉 flash attributes
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "front-end/mem/login";
    }

    // 處理登入提交
    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") LoginForm loginForm,
                               @RequestParam String captcha,
                               HttpSession session,
                               Model model) {
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("errorMsg", "驗證碼錯誤");
            return "front-end/mem/login";
        }

        try {
            MemVO member = memService.findByAccountAndPassword(
                    loginForm.getAccount(), loginForm.getPassword());

            if (member == null) {
                model.addAttribute("errorMsg", "帳號或密碼錯誤");
                return "front-end/mem/login";
            }

            memService.updateLastLogin(member.getMemberId());
            session.setAttribute("memberId", member.getMemberId());

            String location = (String) session.getAttribute("location");
            if (location != null) {
                session.removeAttribute("location");
                return "redirect:" + location;
            }

            return "redirect:/";

        } catch (IllegalStateException e) {
            if ("UNVERIFIED".equals(e.getMessage())) {
                model.addAttribute("errorMsg", "UNVERIFIED");
            } else {
                model.addAttribute("errorMsg", "登入失敗");
            }
            return "front-end/mem/login";
        }
    }

    // 處理登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}