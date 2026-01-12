package com.dreamhouse.cart.model;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dreamhouse.prod.model.ProdService;
import com.dreamhouse.prod.model.ProdSizeConnectService;
import com.dreamhouse.prod.model.ProdVO;
import com.dreamhouse.size.model.SizeService;

@Service("standardProdCartService")
public class StandardProdCartService implements CartStrategy{
	@Autowired
	private ProdService prodSer;
	@Autowired
	private ProdSizeConnectService prodSizeConnSer;
	@Autowired
	SizeService sizeSer;
	@Autowired
	private RedisTemplate<String, Object> redisTemp;
	

	@Override
	public void addToCart(Integer productId, Integer quantity, Integer price, Integer memberId) {
		// 刪除商品、查詢購物車列表等功能請參閱 CartService
		
		
		ProdVO prodVO = prodSer.getOneProd(productId);
		String cartKey = new StringBuilder("cart").append(":").append(memberId).toString();
		String itemKey = new StringBuilder("STD").append(":").append(productId).toString();
		
		CartItemDTO itemDTO = new CartItemDTO();
        itemDTO.setItemKey(itemKey);
        itemDTO.setProductId(productId);
        itemDTO.setProductName(prodVO.getProductName()); // 假設從前端傳來或從 DB 查出
        itemDTO.setPrice(price);
        itemDTO.setQuantity(quantity);
        
        
        redisTemp.opsForHash().put(cartKey, itemKey, itemDTO);
        redisTemp.expire(cartKey, 7, TimeUnit.DAYS);
	}
	
	
	
	
}
