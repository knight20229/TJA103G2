package com.dreamhouse.custommaterial.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.dreamhouse.customprod.model.CustomProductVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name ="CUSTOM_MATERIAL")
public class CustomMaterialVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_MATERIAL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customMaterialId;
	
	@Column(name ="CUSTOM_MATERIAL_NAME")
	@NotEmpty(message="材質名稱:請勿空白")
	@Pattern(regexp = "^[\u4e00-\\u9fa5]{2,5}$", message = "材質名稱只能是中文,長度需在2到5")
	private String customMaterialName;
	
	@Column(name ="CUSTOM_MATERIAL_PRICE")
	@NotNull(message="材質價格:請勿空白")
	@Min(value = 1000, message = "材質價格:不能小於{value}")
	@Max(value = 10000, message = "材質價格:不能大於{value}")
	private Integer customMaterialPrice;
	
	@OneToMany(mappedBy="materialVO")
	private Set<CustomProductVO> materialProducts =new HashSet<>();
	
	public CustomMaterialVO() {}	//無參數建構子

	public Integer getCustomMaterialId() {
		return customMaterialId;
	}

	public void setCustomMaterialId(Integer customMaterialId) {
		this.customMaterialId = customMaterialId;
	}

	public String getCustomMaterialName() {
		return customMaterialName;
	}

	public void setCustomMaterialName(String customMaterialName) {
		this.customMaterialName = customMaterialName;
	}

	public Integer getCustomMaterialPrice() {
		return customMaterialPrice;
	}

	public void setCustomMaterialPrice(Integer customMaterialPrice) {
		this.customMaterialPrice = customMaterialPrice;
	}

	public Set<CustomProductVO> getMaterialProducts() {
		return materialProducts;
	}

	public void setMaterialProducts(Set<CustomProductVO> materialProducts) {
		this.materialProducts = materialProducts;
	}

}