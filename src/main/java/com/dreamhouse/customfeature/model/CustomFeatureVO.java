package com.dreamhouse.customfeature.model;

import java.io.*;
import java.util.*;

import com.dreamhouse.customprod.model.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table (name ="CUSTOM_FEATURE")
public class CustomFeatureVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_FEATURE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customFeatureId;
	
	@Column(name ="CUSTOM_FEATURE_NAME")
	@NotEmpty(message="功能名稱:請勿空白")
	@Pattern(regexp = "^[(\u4e00-\\u9fa5)]{2,5}$", message = "功能名稱只能是中文,長度需在2到5")
	private String customFeatureName;
	
	@Column(name ="CUSTOM_FEATURE_PRICE")
	@NotNull(message="功能價格:請勿空白")
	@Min(value = 1000, message = "功能價格:不能小於{value}")
	@Max(value = 10000, message = "功能價格:不能大於{value}")
	private Integer customFeaturePrice;
	
	
	public CustomFeatureVO() {}	//無參數建構子

	public Integer getCustomFeatureId() {
		return customFeatureId;
	}

	public void setCustomFeatureId(Integer customFeatureId) {
		this.customFeatureId = customFeatureId;
	}

	public String getCustomFeatureName() {
		return customFeatureName;
	}

	public void setCustomFeatureName(String customFeatureName) {
		this.customFeatureName = customFeatureName;
	}

	public Integer getCustomFeaturePrice() {
		return customFeaturePrice;
	}

	public void setCustomFeaturePrice(Integer customFeaturePrice) {
		this.customFeaturePrice = customFeaturePrice;
	}

}
