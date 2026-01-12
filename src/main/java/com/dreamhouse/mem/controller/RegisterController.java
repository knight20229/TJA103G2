package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.model.MemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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

        if (result.hasErrors()) {
            return "front-end/mem/register";
        }

        if (memService.existsByAccount(memberVO.getAccount())) {
            model.addAttribute("errorMsg", "帳號已被使用");
            return "front-end/mem/register";
        }

        if (memService.existsByEmail(memberVO.getEmail())) {
            model.addAttribute("errorMsg", "Email已被使用");
            return "front-end/mem/register";
        }

        // 新增會員並產生驗證 token
        MemVO saved = memService.addMember(memberVO);
        // 假設 memService.addMember() 會回傳 token
        String toEmail = saved.getEmail();


        // 顯示成功訊息
        model.addAttribute("success", "註冊成功！驗證信已寄出至 " + toEmail + "，請前往信箱完成驗證。");
        model.addAttribute("memberVO", saved);

        return "front-end/mem/register";
    }

    // 驗證信連結
    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        boolean verified = memService.verifyEmail(token);
        if (verified) {
            redirectAttributes.addFlashAttribute("verified", true);
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "驗證失敗或連結已過期");
        }
        return "redirect:/mem/login";
    }

    @PostMapping("/resend-verification")
    public String resend(@RequestParam("email") String email, Model model) {
        System.out.println("[DEBUG] 前端傳入的 email: " + email);

        boolean resent = memService.resendVerification(email);

        if (resent) {
            model.addAttribute("success", "驗證信已重新寄出！");
            MemVO mem = memService.findByEmail(email); // 查回會員
            model.addAttribute("memberVO", mem);       // 放進 model，避免 Thymeleaf 報錯
        } else {
            model.addAttribute("errorMsg", "帳號已驗證或不存在");
            model.addAttribute("memberVO", new MemVO()); // 至少放一個空物件
        }

        return "front-end/mem/register"; // 停留在註冊頁面
    }


}