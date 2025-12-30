package com.dreamhouse.prod.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamhouse.size.model.SizeVO;

@Service
public class ProdSizeConnectService {

    @Autowired
    private ProdSizeConnectRepository repository;

    public List<ProdSizeConnectVO> findByProductId(Integer productId) {
        return repository.findByProdVO_ProductId(productId);
    }

    @Transactional
    public void saveOrUpdate(Integer productId, Integer sizeId, Integer stock, Integer price) {
        ProdSizeConnectVO connect = repository.findByProductIdAndSizeId(productId, sizeId);

        if (connect == null) {
            connect = new ProdSizeConnectVO();
            
            ProdVO prodVO = new ProdVO();
            prodVO.setProductId(productId);
            connect.setProdVO(prodVO);
            
            SizeVO sizeVO = new SizeVO();
            sizeVO.setSizeId(sizeId);
            connect.setSizeVO(sizeVO);
        }

        connect.setStock(stock); 
        connect.setPrice(price);

        repository.save(connect);
    }
}