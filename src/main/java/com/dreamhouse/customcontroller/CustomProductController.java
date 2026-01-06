package com.dreamhouse.customcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dreamhouse.customcloth.model.CustomClothService;
import com.dreamhouse.customfeature.model.CustomFeatureService;
import com.dreamhouse.custommaterial.model.CustomMaterialService;
import com.dreamhouse.customsize.model.CustomSizeService;

@Controller
@RequestMapping("/custom")
public class CustomProductController {
	
	@Autowired
	private CustomFeatureService featureSvc;
	
	@Autowired
	private CustomMaterialService materialSvc;
	
	@Autowired
	private CustomClothService clothSvc;
	
	@Autowired
	private CustomSizeService sizeSvc;
	
    @GetMapping("/customproduct")
    public String CustomProductpage(ModelMap model) {
    	model.addAttribute("featureList", featureSvc.getAll());
    	model.addAttribute("materialList", materialSvc.getAll());
    	model.addAttribute("clothList", clothSvc.getAll());
    	model.addAttribute("sizeList", sizeSvc.getAll());
    	
        return "front-end/custom/customproduct";
    }
    
}	