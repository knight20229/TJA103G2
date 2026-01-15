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

import com.dreamhouse.emp.model.EmpService;
import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.promotions.model.PromotionsService;
import com.dreamhouse.promotions.model.PromotionsVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/promotions")
public class PromotionsController {
	
	@Autowired
	PromotionsService proSer;
	
	@Autowired
	EmpService empSer;
	
	
	@GetMapping("addPromotions")
	public String addPromotions(HttpSession session, ModelMap model) {
		PromotionsVO promotionsVO = new PromotionsVO();
		model.addAttribute("promotionsVO", promotionsVO);
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = empSer.findById(employeeId);
		model.addAttribute("employeeName", empVO.getName());
		return "back-end/promotions/promotions_add";
	}
	
	@PostMapping("insert")
	public String insert(@Valid PromotionsVO promotionsVO, BindingResult result, HttpSession session, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/promotions/promotions_add";
		}
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = (EmpVO)empSer.findById(employeeId);
		promotionsVO.setEmpVO(empVO);
		proSer.addPromotions(promotionsVO);
		List<PromotionsVO> list = proSer.getAll();
		model.addAttribute("promotionsList", list);
		return "back-end/promotions/promotions_list";
	}
	
	
	@GetMapping("getOneForUpdate")
	public String getOneForUpdate(@RequestParam("promotionsId") String promotionsId, HttpSession session, ModelMap model) {
		PromotionsVO promotionsVO = proSer.getOneById(Integer.valueOf(promotionsId));
		model.addAttribute("promotionsVO", promotionsVO);
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = empSer.findById(employeeId);
		model.addAttribute("employeeName", empVO.getName());
		return "back-end/promotions/promotions_edit";
	}
	
	@PostMapping("updatePromotions")
	public String updatePromotions(@Valid PromotionsVO promotionsVO, BindingResult result, HttpSession session, ModelMap model) {
		if (result.hasErrors()) {
			return "back-end/promotions/promotions_edit";
		}
		
		Integer employeeId = (Integer)session.getAttribute("employeeId");
		EmpVO empVO = (EmpVO)empSer.findById(employeeId);
		promotionsVO.setEmpVO(empVO);
		proSer.updatePromotions(promotionsVO);
		promotionsVO = proSer.getOneById(Integer.valueOf(promotionsVO.getPromotionsId()));
		model.addAttribute("promotionsVO", promotionsVO);
		return "back-end/promotions/promotions_viewOne";
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
