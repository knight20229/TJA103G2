package com.dreamhouse.cart.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service("cartService")
public class CartService {
	
	@Autowired
	RedisTemplate<String, Object> redisTemp;

	public List<CartItemDTO> getAllCartItems(Integer memberId) {
		String cartKey = new StringBuilder().append("cart").append(":").append(memberId).toString();

		List<Object> value = redisTemp.opsForHash().values(cartKey);
		List<CartItemDTO> cartItems = new ArrayList();
		for (Object obj : value) {
			CartItemDTO item = (CartItemDTO) obj;
			cartItems.add(item);
		}
		return cartItems;
	}
	
	public void removeItem(String itemKey, Integer memberId) {
		String cartKey = new StringBuilder().append("cart").append(":").append(memberId).toString();
		redisTemp.opsForHash().delete(cartKey, itemKey);
		
	}
	
//	public void updateQuantity(Integer memberId, String itemKey, Integer newQty) {
//        String cartKey = new StringBuilder().append("cart").append(":").append(memberId).toString();
//        CartItemDTO item = (CartItemDTO) redisTemp.opsForHash().get(cartKey, itemKey);
//        if (item != null) {
//            item.setQuantity(newQty);
//            redisTemp.opsForHash().put(cartKey, itemKey, item);
//        }
//    }
	

}
