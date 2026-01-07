package com.dreamhouse.prod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.dreamhouse.prod.model.FrontProdService;

@Controller
public class FrontProdController {
	
	@Autowired
	private FrontProdService frontProdService;
	
	@GetMapping("/")
	public String home(ModelMap model) {
		model.addAttribute("products",frontProdService.getAllProd());
		return "index";
	}
}
