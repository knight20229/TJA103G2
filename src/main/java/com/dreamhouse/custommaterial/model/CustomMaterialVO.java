package com.dreamhouse.custommaterial.model;

import java.io.*;
import java.util.*;

import com.dreamhouse.customprod.model.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name ="CUSTOM_MATERIAL")
public class CustomMaterialVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_MATERIAL_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer materialId;
	
	@Column(name ="CUSTOM_MATERIAL_NAME")
	@NotEmpty(message="材質名稱:請勿空白")
	@Pattern(regexp = "^[(\u4e00-\\u9fa5)]{2,5}$", message = "材質名稱只能是中文,長度需在2到5")
	private String materName;
	
	@Column(name ="MATERIAL_PRICE")
	@NotNull(message="材質價格:請勿空白")
	@Min(value = 1000, message = "材質價格:不能小於{value}")
	@Max(value = 10000, message = "材質價格:不能大於{value}")
	private Integer price;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="customMaterialVo")
	@OrderBy("customMaterialId")
	private Set<CustomProductVO> materialproducts = new HashSet<>();
	
	public CustomMaterialVO() {}	//無參數建構子

	public Integer getMaterialId() {
		return materialId;
	}

	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}

	public String getMaterName() {
		return materName;
	}

	public void setMaterName(String materName) {
		this.materName = materName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Set<CustomProductVO> getMaterialproducts() {
		return materialproducts;
	}

	public void setMaterialproducts(Set<CustomProductVO> materialproducts) {
		this.materialproducts = materialproducts;
	}
}