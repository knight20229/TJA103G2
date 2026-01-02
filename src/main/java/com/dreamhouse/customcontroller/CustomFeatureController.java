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

import com.dreamhouse.customfeature.model.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/feature")
public class CustomFeatureController {

	@Autowired
	CustomFeatureService featureSvc;
	
	@GetMapping("addFeature")
	public String addFeature(ModelMap model) {
		CustomFeatureVO featVO = new CustomFeatureVO();
		model.addAttribute("featVO", featVO );
		return "back-custom/custom/addFeature";
	}
	
	//新增功能
	@PostMapping("insert")
	public String insert(@Valid CustomFeatureVO featVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()){
			return "back-custom/custom/addFeature";
		}
		
		featureSvc.addFeature(featVO);
		
		List<CustomFeatureVO> list = featureSvc.getAll();
		model.addAttribute("featureListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/custom/listAllFeature";  //redirect:避免表單重複提交
	}
	
	//刪除功能
	@PostMapping("delete")
	public String delete(@RequestParam("customFeatureId")String customFeatureId, ModelMap model) {
		featureSvc.deleteFeature(Integer.valueOf(customFeatureId));
		List<CustomFeatureVO> list = featureSvc.getAll();
		model.addAttribute("featureListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-custom/custom/listFeature";
	}

}