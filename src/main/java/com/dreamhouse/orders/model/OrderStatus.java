package com.dreamhouse.orders.model;

public enum OrderStatus {

	PENDING_PAYMENT("待付款"), 
	PENDING_SHIPMENT("待出貨"), 
	SHIPPED("已出貨"), 
	COMPLETED("訂單完成"), 
	CANCELLED("訂單取消");

	private final String label;

	OrderStatus(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}