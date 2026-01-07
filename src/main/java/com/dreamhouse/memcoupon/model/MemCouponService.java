package com.dreamhouse.memcoupon.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamhouse.coupon.model.CouponService;
import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;

@Service("memCouponMappingService")
public class MemCouponService {

	@Autowired
	MemCouponRepository memCoupRepo;

	@Autowired
	CouponService coupSer;

	@Autowired
	MemService memSer;

	@Transactional
	public void addMemCoupon(List<CouponVO> activeCoupList, List<MemVO> activeMemList) {
		activeCoupList = coupSer.getActiveCoupon();
    	activeMemList = memSer.findActiveMem();
    	boolean needUpdateSendTime = false;
        if (activeCoupList.isEmpty() || activeMemList.isEmpty()) {
        	System.out.println("----------成功執行--------------");
        	return;
        } else {
        	for (CouponVO couponVO : activeCoupList) {
        		for (MemVO memVO : activeMemList) {
        			MemCouponCompositeKey key = new MemCouponCompositeKey();
        			key.setCouponId(couponVO.getCouponId());
        			key.setMemberId(memVO.getMemberId());
        			
        			if (!memCoupRepo.existsById(key)) {
        				MemCouponVO memCoup = new MemCouponVO();
        				memCoup.setMemCouponKey(key);
        				memCoup.setCouponVO(couponVO); 
        				memCoup.setMemVO(memVO);
        				memCoup.setUseStatus(0);
        				memCoupRepo.save(memCoup);
        				System.out.println("----------成功執行2--------------");
        				needUpdateSendTime = true;
        				
        			}
        		}
        		
        		if (needUpdateSendTime) {
        			couponVO.setSendTime(LocalDateTime.now());
        			coupSer.updateSendTime(couponVO);
        			System.out.println("----------成功執行3--------------");
        			
        		}

        	}
        }
	}

	
	public List<MemCouponVO> getAll(){
		return memCoupRepo.findAll();
	}
}
