package com.dreamhouse.prod.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FrontProdRepository extends JpaRepository<ProdVO, Integer>{
	List<ProdVO> findByProductNameContaining(String prodName); //使用資料庫product_name模糊查詢
}
