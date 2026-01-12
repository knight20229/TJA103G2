package com.dreamhouse.cart.model;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String itemKey;
	private Integer productId;
	private Integer productSizeId;  // 商品尺寸ID（結帳時建立訂單明細用）
	private Integer customProductId; // 客製化商品ID（客製化商品用）
	private Integer sizeId;          // 尺寸ID（顯示用）
	private String sizeName;         // 尺寸名稱（顯示用）
	private String productName;
	private Integer quantity;
	private Integer price;
	
	
	public String getItemKey() {
		return itemKey;
	}
	
	public void setItemKey(String itemKey) {
		this.itemKey = itemKey;
	}
	
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

	public Integer getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(Integer productSizeId) {
		this.productSizeId = productSizeId;
	}

	public Integer getSizeId() {
		return sizeId;
	}

	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}

	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}

	public Integer getCustomProductId() {
		return customProductId;
	}

	public void setCustomProductId(Integer customProductId) {
		this.customProductId = customProductId;
	}
}