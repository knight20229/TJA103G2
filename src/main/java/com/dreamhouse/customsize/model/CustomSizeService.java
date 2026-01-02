package com.dreamhouse.customsize.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomSizeService {
	
	@Autowired
	CustomSizeRepository repository;
	
	public void addSize(CustomSizeVO sizeVO) {
		repository.save(sizeVO);
	}
	
	public void deleteSize(Integer customSizeId) {
		if(repository.existsById(customSizeId))
			repository.deleteById(customSizeId);
	}
	
	public List<CustomSizeVO> getAll(){
		return repository.findAll();
	}
}
