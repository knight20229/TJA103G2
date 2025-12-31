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
public class LoginController {

    @Autowired
    private MemService memService;

    // 顯示登入頁面
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm()); // LoginForm 是一個 DTO
        return "front-end/mem/login"; // 對應 login.html / login.jsp
    }

    // 處理登入提交
    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") LoginForm loginForm,
                               @RequestParam String captcha,
                               HttpSession session,
                               Model model) {
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("errorMsg", "驗證碼錯誤"); // 統一使用 errorMsg
            return "front-end/mem/login";
        }

        // 呼叫 Service 檢查帳號密碼
        MemVO member = memService.findByAccountAndPassword(
                loginForm.getAccount(), loginForm.getPassword());

        if (member == null) {
            model.addAttribute("errorMsg", "帳號或密碼錯誤"); // 同樣使用 errorMsg
            return "front-end/mem/login";
        }

        // 登入成功 → 更新最後登入時間
        memService.updateLastLogin(member.getMemberId());

        // 存入 Session（只存必要資訊）
        session.setAttribute("memberId", member.getMemberId());

        // 檢查是否有來源頁面
        String location = (String) session.getAttribute("location");
        if (location != null) {
            session.removeAttribute("location");
            return "redirect:" + location;
        }

        return "redirect:/mem/my-account";
    }
}



