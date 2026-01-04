package com.dreamhouse.customcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customprod.model.CustomProductService;
import com.dreamhouse.customsize.model.CustomSizeVO;

@Controller
@RequestMapping("/CustomProduct")
public class CustomProductController {
	
	@Autowired
	private CustomProductService productSvc;
	
	@GetMapping("/feature")
	public List<CustomFeatureVO> getAllFeature(){
		return productSvc.getAllFeature();
	}
	
	@GetMapping("/material")
	public List<CustomMaterialVO> getAllMaterial(){
		return productSvc.getAllMaterial();
	}
	
	@GetMapping("/cloth")
	public List<CustomClothVO> getAllCloth(){
		return productSvc.getAllCloth();
	}
	
	@GetMapping("/size")
	public List<CustomSizeVO> getAllSize(){
		return productSvc.getAllSize();	
	}
}
