package com.dreamhouse.cart.model;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.dreamhouse.prod.model.ProdService;
import com.dreamhouse.prod.model.ProdSizeConnectRepository;
import com.dreamhouse.prod.model.ProdSizeConnectService;
import com.dreamhouse.prod.model.ProdSizeConnectVO;
import com.dreamhouse.size.model.SizeService;

@Service("standardProdCartService")
public class StandardProdCartService implements CartStrategy{
	@Autowired
	ProdService prodSer;
	@Autowired
	ProdSizeConnectService prodSizeConnSer;
	@Autowired
	ProdSizeConnectRepository prodSizeConnectRepo;
	@Autowired
	SizeService sizeSer;
	@Autowired
	RedisTemplate<String, Object> redisTemp;

	@Override
	public void addToCart(Integer productId, Integer sizeId, Integer quantity, Integer price, Integer memberId) {
		// 查詢商品尺寸信息，獲取 productSizeId
		ProdSizeConnectVO psc = prodSizeConnectRepo.findByProductIdAndSizeId(productId, sizeId);
		if (psc == null) {
			throw new RuntimeException("商品尺寸不存在");
		}

		// 檢查庫存
		if (psc.getStock() < quantity) {
			throw new RuntimeException("庫存不足");
		}

		Integer productSizeId = psc.getProductSizeId();
		String cartKey = "cart:" + memberId;
		String itemKey = "STD:" + productSizeId;  // 使用 productSizeId 而非 productId

		// 設置 CartItemDTO
		CartItemDTO itemDTO = new CartItemDTO();
		itemDTO.setItemKey(itemKey);
		itemDTO.setProductId(productId);
		itemDTO.setProductSizeId(productSizeId);  // 新增
		itemDTO.setSizeId(sizeId);                // 新增
		// 組合尺寸名稱（width x length）
		String sizeName = psc.getSizeVO().getWidth() + " x " + psc.getSizeVO().getLength() + " cm";
		itemDTO.setSizeName(sizeName);  // 新增
		itemDTO.setProductName(psc.getProdVO().getProductName());
		itemDTO.setPrice(price);
		itemDTO.setQuantity(quantity);

		// 儲存到 Redis
		redisTemp.opsForHash().put(cartKey, itemKey, itemDTO);
		redisTemp.expire(cartKey, 7, TimeUnit.DAYS);
	}
	
	
	
	
}