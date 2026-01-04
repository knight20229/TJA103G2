package com.dreamhouse.custommaterial.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomMaterialService {

	@Autowired
	CustomMaterialRepository repository;
	
	public void addMaterial(CustomMaterialVO materialVO) {
		repository.save(materialVO);
	}
	
	public void deleteMaterial(Integer customMaterialId) {
		if(repository.existsById(customMaterialId))
			repository.deleteById(customMaterialId);
	}
	
	public List<CustomMaterialVO> getAll(){
		return repository.findAll();
	}
}