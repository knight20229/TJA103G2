package com.dreamhouse.custommaterial.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomMaterialRepository extends JpaRepository<CustomMaterialVO, Integer>{
	
	@Query(value =" select m from CustomMaterialVO m")
	List <CustomMaterialVO> findAllMaterial();
}