package com.dreamhouse.prod.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdSizeConnectRepository extends JpaRepository<ProdSizeConnectVO, Integer> {
    // 根據「商品ID」找出該商品所有的尺寸與價格
    List<ProdSizeConnectVO> findByProdVO_ProductId(Integer productId);
}