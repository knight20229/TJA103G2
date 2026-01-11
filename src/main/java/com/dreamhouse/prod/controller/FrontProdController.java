package com.dreamhouse.prod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.prod.dto.FrontProd;
import com.dreamhouse.prod.model.FrontProdService;

@Controller
public class FrontProdController {

    @Autowired
    private FrontProdService frontProdService;

    // 首頁，顯示全部商品
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", frontProdService.getAllProd());
        return "front-end/index"; 
    }
    
    //搜尋商品
    @GetMapping("/search")
    public String searchProds(@RequestParam("product") String prodName, Model model) {
        model.addAttribute("products", frontProdService.searchProd(prodName));
        return "front-end/index";
    }
    
    //商品頁面
    @GetMapping("/product/{id}")
    public String oneProd(@PathVariable("id")Integer productId, Model model) {
    	FrontProd product = frontProdService.getOneProdWithSizes(productId);
    	if(product == null) {
    		return "front-end/index";
    	}
    	model.addAttribute("product", product);
    	return "front-end/prod/prod";
    }
}
