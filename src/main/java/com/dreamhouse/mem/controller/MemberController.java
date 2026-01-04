package com.dreamhouse.mem.controller;

import com.dreamhouse.mem.model.MemVO;
import com.dreamhouse.mem.model.MemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/mem")
public class MemberController {

    @Autowired
    private MemService memService;

    // 顯示全部會員列表
    @GetMapping("/list")
    public String listMembers(Model model) {
        List<MemVO> members = memService.findAllMembers();
        model.addAttribute("members", members);
        return "back-end/mem/mem_management"; // 對應 Thymeleaf 頁面
    }

    // 查詢單一會員 (給 AJAX 用)
    @GetMapping("/{id}")
    @ResponseBody
    public MemVO getMember(@PathVariable("id") Integer memberId) {
        return memService.findById(memberId);
    }

    // 更新會員狀態 (啟用/停用)
    @PostMapping("/{id}/status")
    @ResponseBody
    public String updateStatus(@PathVariable("id") Integer memberId,
                               @RequestParam("status") int status) {
        memService.updateStatus(memberId, status);
        return "success";
    }
}
