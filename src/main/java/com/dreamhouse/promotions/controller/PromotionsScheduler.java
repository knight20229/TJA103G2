package com.dreamhouse.promotions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dreamhouse.promotions.model.PromotionsService;

@Component
public class PromotionsScheduler {
	
	@Autowired
	PromotionsService promoSer;

	// 開始促銷活動
	@Scheduled(cron = "0 1 0 * * ?")
	void startPromotions() {
		promoSer.updateSendTime();
		System.out.println("開始促銷活動");
	}
	
	
	// 結束日期=當下日期，將狀態更新為停用
	@Scheduled(cron = "0 3 0 * * ?")
	void updateState() {
		promoSer.updateState();
		System.out.println("更新成功");
	}
}
