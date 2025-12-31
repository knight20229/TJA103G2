package com.dreamhouse.customfeature.model;

import java.io.*;
import java.util.*;

import com.dreamhouse.customprod.model.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table (name ="CUSTOM_FEATURE")
public class FeatureVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_FEATURE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer featureId;
	
	@Column(name ="CUSTOM_FEATURE_NAME")
	@NotEmpty(message="功能名稱:請勿空白")
	@Pattern(regexp = "^[(\u4e00-\\u9fa5)]{2,5}$", message = "功能名稱只能是中文,長度需在2到5")
	private String featureName;
	
	@Column(name ="FEATURE_PRICE")
	@NotNull(message="功能價格:請勿空白")
	@Min(value = 1000, message = "功能價格:不能小於{value}")
	@Min(value = 10000, message = "功能價格:不能大於{value}")
	private Integer price;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="featureVo")
	@OrderBy("customProductId")
	private Set<CustomProductVO> featureproducts = new HashSet<CustomProductVO>();
	
	public FeatureVO() {}	//無參數建構子

	public Integer getFeatureId() {
		return featureId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Set<CustomProductVO> getFeatureproducts() {
		return featureproducts;
	}

	public void setFeatureproducts(Set<CustomProductVO> featureproducts) {
		this.featureproducts = featureproducts;
	}
}
