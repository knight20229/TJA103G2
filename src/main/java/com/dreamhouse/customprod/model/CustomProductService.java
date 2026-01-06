package com.dreamhouse.customprod.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamhouse.customcloth.model.CustomClothRepository;
import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureRepository;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialRepository;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customsize.model.CustomSizeRepository;
import com.dreamhouse.customsize.model.CustomSizeVO;


@Service("customProductService")
public class CustomProductService {
	
	@Autowired
	private CustomFeatureRepository featureRepository;
	
	@Autowired
	private CustomMaterialRepository materialRepository;
	
	@Autowired
	private CustomClothRepository clothRepository;
	
	@Autowired
	private CustomSizeRepository sizeRepository;
	
	public List <CustomFeatureVO> getAllFeature(){
	 return featureRepository.findAllFeature();
	}
	
	public List <CustomMaterialVO> getAllMaterial(){
		 return materialRepository.findAllMaterial();
	}
	
	public List <CustomClothVO> getAllCloth(){
		 return clothRepository.findAllCloth();
	}
	
	public List <CustomSizeVO> getAllSize(){
		 return sizeRepository.findAllSize();
	}
}