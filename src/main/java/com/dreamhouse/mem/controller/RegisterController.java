package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.model.MemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mem")
public class RegisterController {

    @Autowired
    private MemService memService;

    // 顯示註冊頁面
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("memberVO", new MemVO());
        return "front-end/mem/register";
    }

    // 處理註冊表單送出
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("memberVO") MemVO memberVO,
                                  BindingResult result,
                                  Model model) {

        // 檢查驗證錯誤
        if (result.hasErrors()) {
            return "front-end/mem/register";
        }

        // 檢查帳號是否已存在
        if (memService.existsByAccount(memberVO.getAccount())) {
            model.addAttribute("errorMsg", "帳號已被使用");
            return "front-end/mem/register";
        }

        // 檢查 Email 是否已存在
        if (memService.existsByEmail(memberVO.getEmail())) {
            model.addAttribute("errorMsg", "Email已被使用");
            return "front-end/mem/register";
        }

        // 呼叫 Service 存入資料庫
        MemVO saved = memService.addMember(memberVO);
        model.addAttribute("success", "註冊成功，會員ID=" + saved.getMemberId());

        return "redirect:/mem/login"; // 註冊成功後導向登入頁
    }
}