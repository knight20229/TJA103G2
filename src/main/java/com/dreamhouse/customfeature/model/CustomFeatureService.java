package com.dreamhouse.customfeature.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomFeatureService {

	@Autowired
	private CustomFeatureRepository repository;
	
	public void addFeature(CustomFeatureVO featureVO) {
		repository.save(featureVO);
	}
	
	public void deleteFeature(Integer customFeatureId) {
		if(repository.existsById(customFeatureId))
		   repository.deleteById(customFeatureId);
	}
	
	public List<CustomFeatureVO> getAll(){
		return repository.findAll();
	}
	
	public CustomFeatureVO getOneById(Integer customFeatureId) {
		Optional<CustomFeatureVO> optional = repository.findById(customFeatureId);
		return optional.orElse(null);
		
	}
}
