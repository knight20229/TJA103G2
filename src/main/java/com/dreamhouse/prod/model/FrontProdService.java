package com.dreamhouse.prod.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamhouse.prod.dto.FrontProd;
import com.dreamhouse.prod.dto.FrontProdSize;

@Service
public class FrontProdService {

    @Autowired
    private FrontProdRepository frontRes;

    //取得資料送給前端畫面
    public List<FrontProd> getAllProd() {
        List<ProdVO> products = frontRes.findAll();
        return prodList(products);
    }

    //首頁名稱模糊查詢
    public List<FrontProd> searchProd(String prodName) {
        List<ProdVO> products = frontRes.findByProductNameContaining(prodName);
        return prodList(products);
    }

    //取得單一產品資料
    public FrontProd getOneProd(Integer productId) {
        ProdVO prod = frontRes.findById(productId).orElse(null);
        if (prod == null) return null;

        return FrontDTO(prod);
    }
    
    //透過ID取得產品資料
    public FrontProd getOneProdWithSizes(Integer productId) {
        ProdVO prod = frontRes.findById(productId).orElse(null);
        if (prod == null) return null;

        FrontProd dto = FrontDTO(prod);

        // 再取得尺寸列表
        List<FrontProdSize> sizes = prod.getProdSizeConnects().stream()
                .filter(connect -> connect.getSizeVO() != null
                                   && connect.getSizeVO().getWidth() != null
                                   && connect.getSizeVO().getLength() != null)
                .map(connect -> {
                	FrontProdSize size = new FrontProdSize();
                	size.setSizeId(connect.getProductSizeId());  // 設置 ProdSizeConnect 的 ID
                	size.setSizeText("長:" + connect.getSizeVO().getWidth() + "公分" + " \t " + "寬:" + connect.getSizeVO().getLength() + " 公分 ");
                	size.setPrice(connect.getPrice());
                	size.setStock(connect.getStock());
                	return size;
                }).toList();

        dto.setSizes(sizes);

        return dto;
    }

    private List<FrontProd> prodList(List<ProdVO> products) {
        List<FrontProd> result = new ArrayList<>();
        for (ProdVO prod : products) {
            result.add(FrontDTO(prod));
        }
        return result;
    }

    //資料回傳前段並設定屬性
    private FrontProd FrontDTO(ProdVO prod) {
        FrontProd dto = new FrontProd();

        dto.setProductId(prod.getProductId());
        dto.setProductName(prod.getProductName());
        dto.setProductType(prod.getProductType());
        dto.setDescription(prod.getDescription());

        // 軟硬度判斷
        String hardnessDesc = prod.getDescription() != null ? prod.getDescription().toLowerCase() : "";
        if (hardnessDesc.contains("偏軟") || hardnessDesc.contains("soft")) dto.setHardness("soft");
        else if (hardnessDesc.contains("適中") || hardnessDesc.contains("medium")) dto.setHardness("medium");
        else if (hardnessDesc.contains("較硬") || hardnessDesc.contains("hard")) dto.setHardness("hard");
        else dto.setHardness("");

        // 材質判斷
        String materialDesc = prod.getMaterial() != null ? prod.getMaterial().toLowerCase() : "";
        if (materialDesc.contains("乳膠")) dto.setMaterial("乳膠");
        else if (materialDesc.contains("彈簧")) dto.setMaterial("彈簧");
        else if (materialDesc.contains("棉")) dto.setMaterial("棉");
        else dto.setMaterial("");

        // 圖片轉 Base64
        if (prod.getImageData() != null) {
            dto.setImageBase64(
                    "data:image/jpeg;base64," +
                    Base64.getEncoder().encodeToString(prod.getImageData())
            );
        }

        // 首頁產品最低價格
        int price = prod.getProdSizeConnects().stream()
                .mapToInt(ProdSizeConnectVO::getPrice)
                .min()
                .orElse(0);
        dto.setDisplayPrice(price);

        return dto;
    }
}

