package com.dreamhouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.orders.model.OrdersService;
import com.dreamhouse.prod.model.ProdService;
import com.dreamhouse.promotions.model.PromotionsService;

import java.util.*;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	@Autowired
	MemService memSvc;
	@Autowired
	ProdService prodSvc;
	@Autowired
	OrdersService ordersSvc;
	
	@Autowired
	CouponService coupSer;
	@Autowired
	PromotionsService proSer;
	
	
    @GetMapping("/back-end")
    public String index(Model model) {
    	model.addAttribute("memCount", memSvc.findAllMembers().size());
    	model.addAttribute("prodCount", prodSvc.getAll().size());
    	
    	// 尚未開發的部分，先給固定數值或 0
        model.addAttribute("orderCount", ordersSvc.getAll().size());
        model.addAttribute("returnCount", ordersSvc.getAllReturn().size());

        model.addAttribute("CouponCount", coupSer.getAll().size());
        model.addAttribute("PromotionCount", proSer.getAll().size());
        
    	return "back-end/index"; 
    }
}