package com.dreamhouse.prod.dto;

public class FrontProd {
	private Integer productId;
    private String productName;
    private String imageBase64;
    private Integer displayPrice;
    
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
}
