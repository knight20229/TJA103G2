package com.dreamhouse.cart.model;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String itemKey;
	private Integer productId;
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
	
	
	
	
	
	
	
}
