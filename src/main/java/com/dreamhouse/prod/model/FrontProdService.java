package com.dreamhouse.prod.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamhouse.prod.dto.FrontProd;

@Service
public class FrontProdService {

    @Autowired
    private ProdRepository prodRes;


    public List<FrontProd> getAllProd(Integer minPrice, Integer maxPrice) {
        List<ProdVO> products = prodRes.findAll(); 
        List<FrontProd> result = new ArrayList<>();

        for (ProdVO prod : products) {
            FrontProd dto = new FrontProd();

            dto.setProductId(prod.getProductId());
            dto.setProductName(prod.getProductName());

            // 設定圖片
            if (prod.getImageData() != null) {
                dto.setImageBase64(
                    "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(prod.getImageData())
                );
            }

            // 取得最低價格
            int price = prod.getProdSizeConnects().stream()
                    .mapToInt(ProdSizeConnectVO::getPrice)
                    .min()
                    .orElse(0);
            dto.setDisplayPrice(price);

            // 價格篩選
            if ((minPrice == null || price >= minPrice) &&
                (maxPrice == null || price <= maxPrice)) {
                result.add(dto);
            }
        }

        return result;
    }
}
