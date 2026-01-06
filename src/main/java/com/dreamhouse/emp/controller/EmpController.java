package com.dreamhouse.emp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.mem.dto.LoginForm;
import com.dreamhouse.emp.model.EmpService;

@Controller
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	private EmpService empService;

	// 顯示登入頁面
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginForm", new LoginForm()); // LoginForm 是一個 DTO
		return "back-end/emp/login"; // 對應 login.html / login.jsp
	}

	// 處理登入提交
	@PostMapping("/login")
	public String processLogin(@ModelAttribute("loginForm") LoginForm loginForm, HttpSession session, Model model) {

		// 呼叫 Service 檢查帳號密碼
		EmpVO employee = empService.findByAccountAndPassword(loginForm.getAccount(), loginForm.getPassword());

		if (employee == null) {
			model.addAttribute("errorMsg", "帳號或密碼錯誤"); // 同樣使用 errorMsg
			return "back-end/emp/login";
		}

		// 存入 Session（只存必要資訊）
		session.setAttribute("memberId", employee.getEmployeeId());

		// 檢查是否有來源頁面
		String location = (String) session.getAttribute("location");
		if (location != null) {
			session.removeAttribute("location");
			return "redirect:" + location;
		}

		return "redirect:/admin/mem/list";
	}

	// 處理登出
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 清除所有 Session 資料
		session.invalidate();

		// 導回登入頁面
		return "redirect:/emp/login";
	}

}
