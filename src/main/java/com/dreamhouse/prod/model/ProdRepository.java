package com.dreamhouse.prod.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProdRepository extends JpaRepository<ProdVO, Integer> {
    
    @Query("SELECT DISTINCT p.material FROM ProdVO p WHERE p.material IS NOT NULL")
    List<String> findDistinctMaterials();
}
