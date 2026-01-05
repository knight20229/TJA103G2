package com.dreamhouse.promotions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.promotions.model.PromotionsService;
import com.dreamhouse.promotions.model.PromotionsVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/promotions")
public class PromotionsController {
	
	@Autowired
	PromotionsService proSer;
	
	
	@GetMapping("addPromotions")
	public String addPromotions(ModelMap model) {
		PromotionsVO promotionsVO = new PromotionsVO();
		model.addAttribute("promotionsVO", promotionsVO);
		return "back-end/promotions/promotions_add";
	}
	
	@PostMapping("insert")
	public String insert(@Valid PromotionsVO promotionsVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/promotions/promotions_add";
		}
		
		proSer.addPromotions(promotionsVO);
		List<PromotionsVO> list = proSer.getAll();
		model.addAttribute("promotionsList", list);
		return "back-end/promotions/promotions_list";
	}
	
	
	@GetMapping("getOneForUpdate")
	public String getOneForUpdate(@RequestParam("promotionsId") String promotionsId, ModelMap model) {
		PromotionsVO promotionsVO = proSer.getOneById(Integer.valueOf(promotionsId));
		model.addAttribute("promotionsVO", promotionsVO);
		return "back-end/promotions/promotions_edit";
	}
	
	@PostMapping("updatePromotions")
	public String updatePromotions(@Valid PromotionsVO promotionsVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/promotions/promotions_edit";
		}
		
		proSer.updatePromotions(promotionsVO);
		promotionsVO = proSer.getOneById(Integer.valueOf(promotionsVO.getPromotionsId()));
		model.addAttribute("promotionsVO", promotionsVO);
		return "back-end/promotions/promotions_viewOne";
	}
	
	
	@GetMapping("deletePromotions")
	public String deletePromotions(@RequestParam("promotionsId") String promotionsId, ModelMap model) {
		proSer.deletePromotions(Integer.valueOf(promotionsId));
		List<PromotionsVO> list = proSer.getAll();
		model.addAttribute("promotionsList", list);
		return "back-end/promotions/promotions_list";
	}
	
	@GetMapping("getAllPromotions")
	public String getAllPromotions(ModelMap model) {
		List<PromotionsVO> list = proSer.getAll();
		model.addAttribute("promotionsList", list);
		return "back-end/promotions/promotions_list";
	}
	
	@GetMapping("viewPromotions")
	public String getOneById(@RequestParam("promotionsId") String promotionsId, ModelMap model) {
		PromotionsVO promotionsVO = proSer.getOneById(Integer.valueOf(promotionsId));
		model.addAttribute("promotionsVO", promotionsVO);
		return "back-end/promotions/promotions_viewOne";
	}
	
}
