package com.dreamhouse.prod.dto;

import java.util.List;

public class FrontProd {
	private Integer productId;
    private String productName;
    private String imageBase64;
    private Integer displayPrice;
    private String hardness;
    private String material;
    private Boolean productType;
    private String description;
    private List<FrontProdSize> sizes;
    
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getImageBase64() {
		return imageBase64;
	}
	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}
	public Integer getDisplayPrice() {
		return displayPrice;
	}
	public void setDisplayPrice(Integer displayPrice) {
		this.displayPrice = displayPrice;
	}
	public String getHardness() {
		return hardness;
	}
	public void setHardness(String hardness) {
		this.hardness = hardness;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public Boolean getProductType() {
		return productType;
	}
	public void setProductType(Boolean productType) {
		this.productType = productType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<FrontProdSize> getSizes() {
		return sizes;
	}
	public void setSizes(List<FrontProdSize> sizes) {
		this.sizes = sizes;
	}
}
