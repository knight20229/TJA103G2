package com.dreamhouse.prod.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdSizeConnectRepository extends JpaRepository<ProdSizeConnectVO, Integer> {
    // 根據「商品ID」找出該商品所有的尺寸與價格
    List<ProdSizeConnectVO> findByProdVO_ProductId(Integer productId);
    
    // 用於儲存更新：精確定位某一筆尺寸
    @Query("SELECT p FROM ProdSizeConnectVO p WHERE p.prodVO.productId = :productId AND p.sizeVO.sizeId = :sizeId")
    ProdSizeConnectVO findByProductIdAndSizeId(Integer productId, Integer sizeId);
}