package com.dreamhouse.customcloth.model;

import java.util.List;
import java.util.Optional;

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
		if(repository.existsById(cloth))
			repository.deleteById(cloth);
	}
	
	public List<CustomClothVO> getAll(){
		return repository.findAll();
	}
	
	public CustomClothVO getOneById(Integer customClothId) {
		Optional<CustomClothVO> optional = repository.findById(customClothId);
		return optional.orElse(null);
		
	}
}
