package com.dreamhouse.prod.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.dreamhouse.prod.dto.FrontProd;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontProdService {

	@Autowired
	private ProdRepository prodRes;
	
	public List<FrontProd> getAllProd() {
		
		List<ProdVO> products = prodRes.findAll();
		List<FrontProd> result = new ArrayList<>();
	
	for (ProdVO prod : products) {
		
		FrontProd dto = new FrontProd();
		
		dto.setProductId(prod.getProductId());
		dto.setProductName(prod.getProductName());
		
		if(prod.getImageData() != null) {
			dto.setImageBase64(
                    "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(prod.getImageData())
                );
		}
		
		int price = prod.getProdSizeConnects().stream()
				.mapToInt(ProdSizeConnectVO::getPrice)
				.min()
				.orElse(0);
		
		dto.setDisplayPrice(price);
		
		result.add(dto);
		
		}
	
	return result;
	}	
}