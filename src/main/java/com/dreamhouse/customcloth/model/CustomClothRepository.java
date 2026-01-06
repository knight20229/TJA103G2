package com.dreamhouse.customcloth.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomClothRepository extends JpaRepository<CustomClothVO, Integer>{
	
	@Query(value =" select c from CustomClothVO c")
	List <CustomClothVO> findAllCloth();
}
