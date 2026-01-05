package com.dreamhouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.prod.model.ProdService;


import java.util.*;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	@Autowired
	MemService memSvc;
	@Autowired
	ProdService prodSvc;
	
	
    @GetMapping("/back-end")
    public String index(Model model) {
    	model.addAttribute("memCount", memSvc.findAllMembers().size());
    	model.addAttribute("prodCount", prodSvc.getAll().size());
    	
    	// 尚未開發的部分，先給固定數值或 0
        model.addAttribute("orderCount", 0);
        model.addAttribute("actCount", 0);
        model.addAttribute("unreadCount", 0);
        
    	return "back-end/index"; 
    }
}

