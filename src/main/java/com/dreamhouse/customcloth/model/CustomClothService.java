package com.dreamhouse.customcloth.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomClothService {
	
	@Autowired
	private CustomClothRepository repository;
	
	public void addCloth(CustomClothVO clothVO) {
		repository.save(clothVO);
	}
	
	public void deleteCloth(Integer cloth) {
		
	}
	
	public List<CustomClothVO> getAll(){
		return repository.findAll();
	}
}
