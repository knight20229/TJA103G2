package com.dreamhouse.prod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.prod.model.FrontProdService;

@Controller
public class FrontProdController {

    @Autowired
    private FrontProdService frontProdService;

    // 首頁，顯示全部商品
    @GetMapping("/")
    public String home(ModelMap model) {
        model.addAttribute(
            "products",
            frontProdService.getAllProd(null, null)
        );
        return "front-end/index"; 
        
    }

    // 價格篩選
    @GetMapping("/price")
    public String price(
            @RequestParam(required=false) Integer minPrice,
            @RequestParam(required=false) Integer maxPrice,
            ModelMap model) {

        model.addAttribute(
            "products",
            frontProdService.getAllProd(minPrice, maxPrice)
        );

        return "front-end/index"; 
    }
}
