package com.dreamhouse.customfeature.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomFeatureRepository extends JpaRepository<CustomFeatureVO, Integer> {
		
	//新增功能
	@Transactional
	@Modifying
	@Query(value= "INSERT INTO CUSTOM_FEATURE(CUSTOM_FEATURE_NAME, FEATURE_PRICE) VALUES (?1, ?2)", nativeQuery = true)
	void insertFeature(String customFeatureName, int customFeaturePrice);
	
	//刪除功能
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM CUSTOM_FEATURE WHERE CUSTOM_FEATURE_ID =?1", nativeQuery = true)
	void deleteFeature(int customFeatureId);
}
