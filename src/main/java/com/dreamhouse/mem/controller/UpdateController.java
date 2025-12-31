package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.model.MemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
@RequestMapping("/mem")
public class UpdateController {

    @Autowired
    private MemService memService;

    // 顯示會員基本資料頁面
    @GetMapping("/my-account")
    public String showMemberInfo(HttpSession session, Model model) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/mem/login"; // 未登入 → 導回登入
        }

        MemVO member = memService.findById(memberId);
        model.addAttribute("memberVO", member);
        System.out.println(member);
        return "front-end/mem/my-account"; // 對應你的 Thymeleaf 頁面
    }

    // 更新會員資料
    @PostMapping("/update")
    public String updateMember(@ModelAttribute("memberVO") MemVO memberVO,
                               HttpSession session,
                               Model model) {
        Integer memberId = (Integer) session.getAttribute("memberId");
        if (memberId == null) {
            return "redirect:/mem/login"; // 未登入 → 導回登入
        }

        // 確保更新的是目前登入的會員
        MemVO storedMemVO = memService.findById(memberId);
        memberVO.setMemberId(memberId);
        memberVO.setAccount(storedMemVO.getAccount());
        memberVO.setPassword(storedMemVO.getPassword());
        

        // 更新時間設為現在
        Timestamp now = Timestamp.from(Instant.now());
        memberVO.setUpdatedTime(now);

        // 呼叫 Service 更新
        MemVO updated = memService.updateMember(memberVO);

        model.addAttribute("memberVO", updated);
        model.addAttribute("success", "更新成功！");

        return "front-end/mem/my-account"; // 更新後回到會員基本資料頁
    }
}