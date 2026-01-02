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

import com.dreamhouse.customsize.model.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/size")
public class CustomSizeController {

	@Autowired
	CustomSizeService sizeSvc;
	
	@GetMapping("addSize")
	public String addSize(ModelMap model) {
		CustomSizeVO sizeVO = new CustomSizeVO();
		model.addAttribute("sizeVO", sizeVO);
		return "back-custom/custom/addCustomSize";
	}
	
	//新增尺寸
	@PostMapping("insert")
	public String insert(@Valid CustomSizeVO sizeVO, BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			return "back-custom/custom/addCustomSize";
		}
		
		sizeSvc.addSize(sizeVO);
		
		List<CustomSizeVO> list = sizeSvc.getAll();
		model.addAttribute("sizeListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/custom/listAllCustomSize";
	}
	
	//刪除尺寸
	@PostMapping("delete")
	public String delete(@RequestParam("customSizeId")String customSizeId, ModelMap model) {
		sizeSvc.deleteSize(Integer.valueOf(customSizeId));
		List<CustomSizeVO> list = sizeSvc.getAll();
		model.addAttribute("sizeListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-custom/custom/listAllCustomSize";
	}
}
