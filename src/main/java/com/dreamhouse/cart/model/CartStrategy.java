package com.dreamhouse.cart.model;

public interface CartStrategy {
	public void addToCart(Integer productId, Integer quantity, Integer price, Integer memberId);
}
