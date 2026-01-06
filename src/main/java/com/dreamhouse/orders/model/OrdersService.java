package com.dreamhouse.orders.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdersService {

	@Autowired
	private OrdersRepository ordersrepo;

	@Transactional
	public void addOrder(OrdersVO ordersVO) {
		ordersrepo.save(ordersVO);
	}

	public void updateOrder(OrdersVO ordersVO) {
		ordersrepo.save(ordersVO);
	}
	
	public void updateReturnStatus(Integer orderId, ReturnStatus returnStatus, RefusalReason refusalReason) {
		LocalDateTime now = LocalDateTime.now();
		Timestamp ts = Timestamp.valueOf(now);
		
        OrdersVO ordersVO = new OrdersVO();

        ordersVO.setOrderId(orderId);

        // 存 VO 時用 label
        ordersVO.setReturnStatus(returnStatus.getLabel());

        if (returnStatus == ReturnStatus.REJECTED && refusalReason != null) {
            ordersVO.setRefusalReason(refusalReason.getLabel());
        }
        
        if (returnStatus == ReturnStatus.RETURNING) {
            // RETURNING 狀態，更新退貨創建時間與審核時間
            ordersVO.setReturnCreateTime(ts);
            ordersVO.setReturnApproveTime(ts);
        } else if (returnStatus == ReturnStatus.REJECTED) {
            // REJECTED 狀態，只更新審核時間
            ordersVO.setReturnApproveTime(ts);
        }

        ordersrepo.save(ordersVO);
    }

	public void delete(Integer orderId) {
		if(ordersrepo.existsById(orderId)) {
			ordersrepo.deleteById(orderId);
		}
			
	}

	public OrdersVO getByOrderID(Integer orderId) {
		Optional<OrdersVO> optional = ordersrepo.findById(orderId);
		return optional.orElse(null); //  如果值存在就回傳其值，沒找到回傳 null
	}

	public List<OrdersVO> getAll() {
		return ordersrepo.findAll();
	}
	
	public List<OrdersVO> getAllReturn() {
		return ordersrepo.findAllReturn();
	}

}
