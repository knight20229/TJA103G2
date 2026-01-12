package com.dreamhouse.custommaterial.model;

import java.util.List;
import java.util.Optional;

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
	
	public CustomMaterialVO getOneById(Integer customMaterialId) {
		Optional<CustomMaterialVO> optional = repository.findById(customMaterialId);
		return optional.orElse(null);
		
	}
}