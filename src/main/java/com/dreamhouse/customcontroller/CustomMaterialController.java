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

import com.dreamhouse.custommaterial.model.CustomMaterialService;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/material")
public class CustomMaterialController {
	
	@Autowired
	CustomMaterialService materialSvc;
	
	@GetMapping("addMaterial")
	public String addMaterial(ModelMap model) {
		CustomMaterialVO materialVO = new CustomMaterialVO();
		model.addAttribute("CustomMaterialVO", materialVO);
		return "back-custom/custom/addMaterial";
	}
	
	//新增材質
	@PostMapping("insert")
	public String insert(@Valid CustomMaterialVO materialVO, BindingResult result, ModelMap model) {
		if (result.hasErrors()){
			return "back-custom/custom/addMaterial";
		}
		
		materialSvc.addMaterial(materialVO);
		
		List<CustomMaterialVO> list =materialSvc.getAll();
		model.addAttribute("materialListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/custom/listAllMaterial";
	}
	
	//刪除材質
	@PostMapping("delete")
	public String deleteMaterial(@RequestParam("customMaterialId")String customMaterialId, ModelMap model) {
		materialSvc.deleteMaterial(Integer.valueOf(customMaterialId));
		List<CustomMaterialVO> list = materialSvc.getAll();
		model.addAttribute("materialListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/custom/listAllMaterial";
	}
}
