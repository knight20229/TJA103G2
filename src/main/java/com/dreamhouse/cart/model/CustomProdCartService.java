package com.dreamhouse.cart.model;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dreamhouse.customcloth.model.CustomClothService;
import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureService;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialService;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customprod.model.CustomProductDTO;
import com.dreamhouse.customprod.model.CustomProductService;
import com.dreamhouse.customprod.model.CustomProductVO;
import com.dreamhouse.customsize.model.CustomSizeService;
import com.dreamhouse.customsize.model.CustomSizeVO;
import com.dreamhouse.mem.model.MemService;
import com.dreamhouse.mem.model.MemVO;

@Service("customProdCartService")
public class CustomProdCartService implements CartStrategy {

	@Autowired
	private CustomProductService customProdSer;

	@Autowired
	private CustomClothService customClothSer;

	@Autowired
	private CustomFeatureService customFeatSer;

	@Autowired
	private CustomSizeService customSizeSer;

	@Autowired
	private CustomMaterialService customMaterSer;

	@Autowired
	private MemService memSer;

	@Autowired
	RedisTemplate<String, Object> redisTemp;

	@Override
	public void addToCart(Integer customProductId, Integer quantity, Integer price, Integer memberId) {
		

		// 將剛新增的客製化商品存到redis
		String cartKey = new StringBuilder("cart").append(":").append(memberId).toString();
		String itemKey = new StringBuilder("CUST").append(":").append(customProductId).toString();

		MemVO memVO = memSer.findById(memberId);
		CartItemDTO itemDTO = new CartItemDTO();
		itemDTO.setItemKey(itemKey);
		itemDTO.setProductId(customProductId);
		itemDTO.setProductName(memVO.getName() + "的客製化商品"); 
		itemDTO.setPrice(price);
		itemDTO.setQuantity(quantity);

		redisTemp.opsForHash().put(cartKey, itemKey, itemDTO);
		redisTemp.expire(cartKey, 7, TimeUnit.DAYS);

	}

}
