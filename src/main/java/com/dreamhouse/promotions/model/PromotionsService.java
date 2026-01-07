package com.dreamhouse.promotions.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
	
	// 開始促銷活動
	public void updateSendTime() {
		List<PromotionsVO> promoList = getActivePromotions();
		if (promoList.isEmpty()) {
			return;
		} else {
			for(PromotionsVO promotionsVO : promoList) {
				promotionsVO.setSendTime(LocalDateTime.now());
				repository.save(promotionsVO);
			}
		}
	}
	
	public List<PromotionsVO> getActivePromotions() {
		return repository.findActivePromotions(1, LocalDate.now());
	}
	
	// 給排程更新促銷活動狀態用
		public void updateState() {
			List<PromotionsVO> promoList = repository.findByState(1);
			for (PromotionsVO promotionsVO : promoList) {
				if (promotionsVO.getEndDt().equals(LocalDate.now())) {
					promotionsVO.setState(0);
					repository.save(promotionsVO);
				}
			}
		}
	
	
}
