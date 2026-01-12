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
import com.dreamhouse.customprod.model.CustomProductRepository;
import com.dreamhouse.customprod.model.CustomProductVO;

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

	@Autowired
	private CustomProductRepository customProdRepo;
	

	public List<CustomFeatureVO> getAllFeature() {
		return featureRepository.findAllFeature();
	}

	public List<CustomMaterialVO> getAllMaterial() {
		return materialRepository.findAllMaterial();
	}

	public List<CustomClothVO> getAllCloth() {
		return clothRepository.findAllCloth();
	}

	public List<CustomSizeVO> getAllSize() {
		return sizeRepository.findAllSize();
	}

	public void addCustomProd(CustomProductVO customProd) {

		customProdRepo.save(customProd);
	}

	public List<CustomProductVO> getAll() {
		return customProdRepo.findAll();
	}

	public Integer getCount() {
		List<CustomProductVO> customProdList = customProdRepo.findAll();
		return customProdList.size();
	}
}