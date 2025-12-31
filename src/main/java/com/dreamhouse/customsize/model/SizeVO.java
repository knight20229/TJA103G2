package com.dreamhouse.customsize.model;

import java.io.Serializable;
import java.util.*;

import com.dreamhouse.customprod.model.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name ="CUSTOM_SIZE")
public class SizeVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_SIZE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customSizeId;
	
	@Column(name ="CUSTOM_WIDTH")
	@NotNull(message = "客製寬度:請勿空白")
	@Min(value = 100, message = "客製寬度:不能小於{value}")
	@Min(value = 1000, message = "客製寬度:不能大於{value}")
	private Integer customWidth;
	
	@Column(name ="CUSTOM_LENGTH")
	@NotNull(message = "客製長度:請勿空白")
	@Min(value = 100, message = "客製長度:不能小於{value}")
	@Min(value = 1000, message = "客製長度:不能大於{value}")
	private Integer customLength;
	
	@Column(name ="SIZE_PRICE")
	@NotNull(message ="尺寸價格:請勿空白")
	@Min(value = 1000, message = "尺寸價格:不能小於{value}")
	@Min(value = 10000, message = "尺寸價格:不能大於{value}")
	private Integer sizePrice;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="sizeVo")
	@OrderBy("customSizeId")
	private Set<CustomProductVO> sizeproducts = new HashSet<CustomProductVO>();
	
	public SizeVO() {}	//無參數建構子

	public Integer getCustomId() {
		return customSizeId;
	}

	public void setCustomId(Integer customId) {
		this.customSizeId = customId;
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

	public Integer getSizePrice() {
		return sizePrice;
	}

	public void setSizePrice(Integer sizePrice) {
		this.sizePrice = sizePrice;
	}

	public void setCustomLength(Integer customLength) {
		this.customLength = customLength;
	}

	public Set<CustomProductVO> getProducts() {
		return sizeproducts;
	}

	public void setProducts(Set<CustomProductVO> products) {
		this.sizeproducts = products;
	}
}
