package com.dreamhouse.promotions.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("promotionsService")
public class PromotionsService {
	
	@Autowired
	PromotionsRepository repository;
	
	@Transactional
	public void addPromotions(PromotionsVO promotionsVO) {
		repository.save(promotionsVO);
	}
	
	@Transactional
	public void updatePromotions(PromotionsVO promotionsVO) {
		repository.save(promotionsVO);
		
	}
	
	
	public PromotionsVO getOneById(Integer promotionsId) {
		Optional<PromotionsVO> optional = repository.findById(promotionsId);
		return optional.orElse(null);
	}
	
	public List<PromotionsVO> getAll(){
		return repository.findAll();
	}
	
	public PromotionsVO getActivePromotions() {
		return repository.findActivePromotions(1, LocalDate.now());
	}
}
