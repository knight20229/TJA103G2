package com.dreamhouse.prod.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProdRepository extends JpaRepository<ProdVO, Integer> {
    // 取得所有材質
    @Query("SELECT DISTINCT p.material FROM ProdVO p WHERE p.material IS NOT NULL")
    List<String> findDistinctMaterials();
    // 自動上架
    @Modifying
    @Query("UPDATE ProdVO p SET p.status = true WHERE p.status = false " +
           "AND p.onDate <= CURRENT_TIMESTAMP " +  // 用資料庫時間
           "AND (p.offDate IS NULL OR p.offDate > CURRENT_TIMESTAMP)")
    int updateStatusToActive();
    // 自動下架
    @Modifying
    @Query("UPDATE ProdVO p SET p.status = false WHERE p.status = true " +
           "AND p.offDate IS NOT NULL " + 
           "AND p.offDate <= CURRENT_TIMESTAMP") // 用資料庫時間
    int updateStatusToInactive();
    
    // 新增商品：檢查名稱是否存在
    boolean existsByProductName(String productName);

    // 編輯商品：檢查名稱是否存在，但排除目前這筆 ID
    boolean existsByProductNameAndProductIdNot(String productName, Integer productId);
}
