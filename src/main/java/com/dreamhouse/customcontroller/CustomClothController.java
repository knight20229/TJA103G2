package com.dreamhouse.customcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dreamhouse.customcloth.model.CustomClothService;
import com.dreamhouse.customcloth.model.CustomClothVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/Cloth")
public class CustomClothController {

	@Autowired
	CustomClothService clothSvc;
	
	@GetMapping("addCloth")
	public String addCloth(ModelMap model) {
		CustomClothVO clothVO = new CustomClothVO();
		model.addAttribute("clothVO", clothVO);
		return "back-custom/custom/addCloth";
	}
	
	//新增布料
	@PostMapping("insert")
	public String insert(@Valid CustomClothVO clothVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "back-custom/custom/addCloth";
		}
		
		clothSvc.addCloth(clothVO);
		
		List<CustomClothVO> list = clothSvc.getAll();
		model.addAttribute("clothListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/custom/listAllCloth";
	}
	
	//刪除布料
	@PostMapping("delete")
	public String delete(@RequestParam("customClothId")String customClothId, ModelMap model) {
		clothSvc.deleteCloth(Integer.valueOf(customClothId));
		List<CustomClothVO> list = clothSvc.getAll();
		model.addAttribute("clothListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/custom/listAllCloth";
	}
}