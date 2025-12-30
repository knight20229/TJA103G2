package com.dreamhouse.prod.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProdRepository extends JpaRepository<ProdVO, Integer> {
    // 取得所有材質
    @Query("SELECT DISTINCT p.material FROM ProdVO p WHERE p.material IS NOT NULL")
    List<String> findDistinctMaterials();
    
    // 上架日期
    @Modifying
    @Query("UPDATE ProdVO p SET p.status = true WHERE p.status = false " +
           "AND p.onDate <= :now " + 
           "AND (p.offDate IS NULL OR p.offDate > :now)")
    int updateStatusToActive(@Param("now") LocalDateTime now);
    // 下架日期
    @Modifying
    @Query("UPDATE ProdVO p SET p.status = false WHERE p.status = true " +
           "AND p.offDate IS NOT NULL " + 
    	   "AND p.offDate <= :now ")
    int updateStatusToInactive(@Param("now") LocalDateTime now);
}
