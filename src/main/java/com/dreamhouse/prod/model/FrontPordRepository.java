package com.dreamhouse.prod.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FrontPordRepository extends JpaRepository<ProdVO, Integer>{
	List<ProdVO> findByDescriptionContaining(String keyword);
}
