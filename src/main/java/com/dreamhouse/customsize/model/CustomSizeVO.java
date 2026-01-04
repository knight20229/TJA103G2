package com.dreamhouse.customsize.model;

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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name ="CUSTOM_SIZE")
public class CustomSizeVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_SIZE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customSizeId;
	
	@Column(name ="CUSTOM_WIDTH")
	@NotNull(message = "寬度:請勿空白")
	@Min(value = 100, message = "寬度:不能小於{value}")
	@Max(value = 1000, message = "寬度:不能大於{value}")
	private Integer customWidth;
	
	@Column(name ="CUSTOM_LENGTH")
	@NotNull(message = "長度:請勿空白")
	@Min(value = 100, message = "長度:不能小於{value}")
	@Max(value = 1000, message = "長度:不能大於{value}")
	private Integer customLength;
	
	@Column(name ="CUSTOM_SIZE_PRICE")
	@NotNull(message ="尺寸價格:請勿空白")
	@Min(value = 1000, message = "尺寸價格:不能小於{value}")
	@Max(value = 10000, message = "尺寸價格:不能大於{value}")
	private Integer customSizePrice;
	
	@OneToMany(mappedBy="sizeVO")
	private Set<CustomProductVO> sizeProducts = new HashSet<>();
	
	public CustomSizeVO() {}	//無參數建構子

	public Integer getCustomSizeId() {
		return customSizeId;
	}

	public void setCustomSizeId(Integer customSizeId) {
		this.customSizeId = customSizeId;
	}

	public Integer getCustomWidth() {
		return customWidth;
	}

	public void setCustomWidth(Integer customWidth) {
		this.customWidth = customWidth;
	}

	public Integer getCustomLength() {
		return customLength;
	}

	public void setCustomLength(Integer customLength) {
		this.customLength = customLength;
	}

	public Integer getCustomSizePrice() {
		return customSizePrice;
	}

	public void setCustomSizePrice(Integer customSizePrice) {
		this.customSizePrice = customSizePrice;
	}

	public Set<CustomProductVO> getSizeProducts() {
		return sizeProducts;
	}

	public void setSizeProducts(Set<CustomProductVO> sizeProducts) {
		this.sizeProducts = sizeProducts;
	}

}
