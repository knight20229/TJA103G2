package com.dreamhouse.customprod.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customsize.model.CustomSizeVO;

public interface CustomProductRepository extends JpaRepository<CustomProductVO, Integer> {
	
	@Query(value =" select f from CustomFeatureVO f")
	List <CustomFeatureVO> findAllFeature();
	
	@Query(value =" select m from CustomMaterialVO m")
	List <CustomMaterialVO> findAllMaterial();
	
	@Query(value =" select c from CustomClothVO c")
	List <CustomClothVO> findAllCloth();
	
	@Query(value =" select s from CustomSizeVO s")
	List<CustomSizeVO> findAllSize();
}