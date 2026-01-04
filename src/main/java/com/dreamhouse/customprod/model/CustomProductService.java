package com.dreamhouse.customprod.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dreamhouse.customfeature.model.*;
import com.dreamhouse.custommaterial.model.*;
import com.dreamhouse.customcloth.model.*;
import com.dreamhouse.customsize.model.*;

@Service
public class CustomProductService {

	@Autowired
	private CustomFeatureRepository featureRepository;
	
	@Autowired
	private CustomSizeRepository sizeRepository;
	
	@Autowired
	private CustomClothRepository clothRepository;
	
	@Autowired
	private CustomMaterialRepository materialRepository;
	
	public List<CustomFeatureVO> getAllFeature(){
		return featureRepository.findAll();
	}
	
	public List<CustomSizeVO> getAllSize(){
		return sizeRepository.findAll();
	}
	
	public List<CustomClothVO> getAllCloth(){
		return clothRepository.findAll();
	}
	
	public List<CustomMaterialVO> getAllMaterial(){
		return materialRepository.findAll();
	}
}