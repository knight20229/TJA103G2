package com.dreamhouse.emp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
		session.setAttribute("employeeId", employee.getEmployeeId());

		// 檢查是否有來源頁面
		String location = (String) session.getAttribute("location");
		if (location != null) {
			session.removeAttribute("location");
			return "redirect:" + location;
		}

		return "redirect:/bacl-end";
	}

	// 處理登出
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 清除所有 Session 資料
		session.invalidate();

		// 導回登入頁面
		return "redirect:/emp/login";
	}
	
////-------------下面的要測試員工登入後的功能再打開，不然會每次執行都要登入----------
////	檢查是否登入
//	@Component
//	public class LoginInterceptor implements HandlerInterceptor {
//	    @Override
//	    public boolean preHandle(HttpServletRequest request,
//	                             HttpServletResponse response,
//	                             Object handler) throws Exception {
//	        HttpSession session = request.getSession(false);
//
//	        if (session == null || session.getAttribute("employeeId") == null) {
//	            response.sendRedirect("/emp/login");
//	            return false;
//	        }
//	        return true;
//	    }
//	}
////	
////	註冊Interceptor並確認需要登入的路徑
//	@Configuration
//	public class WebConfig implements WebMvcConfigurer {
//	    @Autowired
//	    private LoginInterceptor loginInterceptor;
//
//	    @Override
//	    public void addInterceptors(InterceptorRegistry registry) {
//	        registry.addInterceptor(loginInterceptor)
//	                .addPathPatterns("/admin/**", "/back-end/**", "/prod/**") // 後台路徑都要檢查
//	                .excludePathPatterns("/emp/login", "/emp/logout", "/css/**", "/js/**", "/images/**");
//	    }
//	}


}