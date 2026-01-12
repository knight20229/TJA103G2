package com.dreamhouse.customprod.model;

public class CustomProductDTO {
	private Integer clothId;
    private Integer featureId;
    private Integer sizeId;
    private Integer materialId;
    private Boolean hasBedFrame;
    private Integer quantity;
    private Integer price;
    private Integer memberId;
    
    
	public Integer getClothId() {
		return clothId;
	}
	
	public void setClothId(Integer clothId) {
		this.clothId = clothId;
	}
	
	public Integer getFeatureId() {
		return featureId;
	}
	
	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}
	
	public Integer getSizeId() {
		return sizeId;
	}
	
	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}
	
	public Integer getMaterialId() {
		return materialId;
	}
	
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	
	public Boolean getHasBedFrame() {
		return hasBedFrame;
	}
	
	public void setHasBedFrame(Boolean hasBedFrame) {
		this.hasBedFrame = hasBedFrame;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getMemberId() {
		return memberId;
	}
	
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
    
    
}
