package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MailService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/mem")
public class RegisterController {

    @Autowired
    private MemService memService;

    @Autowired
    private TemplateEngine templateEngine; // 用來渲染驗證信 HTML

    @Autowired
    private MailService emailService; // 你需要自己建立一個寄信的 Service

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
        String token = saved.getVerificationToken();
 // 假設 memService.addMember() 會回傳 token
        String toEmail = saved.getEmail();

        // 建立 Thymeleaf context
        Context context = new Context();
        context.setVariable("verifyUrl", "http://localhost:8080/mem/verify?token=" + token);

        // 渲染 verify.html 模板
        String htmlContent = templateEngine.process("front-end/mem/email_verify", context);

        // 寄送 HTML 信件
        try {
            emailService.sendHtmlMail(toEmail, "帳號驗證", htmlContent);
        } catch (MessagingException e) {
            // 可以記錄 log 或顯示錯誤訊息
            e.printStackTrace();
            model.addAttribute("errorMsg", "寄送驗證信失敗，請稍後再試");
        }


        // 顯示成功訊息
        model.addAttribute("success", "註冊成功！驗證信已寄出至 " + toEmail + "，請前往信箱完成驗證。");
        model.addAttribute("memberVO", new MemVO());

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

    // 重寄驗證信
    @PostMapping("/resend-verification")
    public String resend(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        boolean resent = memService.resendVerification(email);
        if (resent) {
            redirectAttributes.addFlashAttribute("success", "驗證信已重新寄出！");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "帳號已驗證或不存在");
        }
        return "redirect:/mem/login";
    }
}