package com.dreamhouse.orderproductsize.model;

import java.sql.Timestamp;

import com.dreamhouse.orders.model.OrdersVO;
import com.dreamhouse.prod.model.ProdSizeConnectVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "order_product_size_mapping")
public class OrderProductSizeVO {
	private static final long serialVersionUID = 1L;

	private Integer orderProductSizeId;
	private OrdersVO orders;
	private ProdSizeConnectVO prodSizeConnect;
	private Integer quantity;
	private Double price;
	private Integer customProductId;
	private Boolean isCustom;
	private Timestamp createTime;
	
	public OrderProductSizeVO() {} // 無參數建構子
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_product_size_id", updatable = false)
	public Integer getOrderProductSizeId() {
		return orderProductSizeId;
	}
	public void setOrderProductSizeId(Integer orderProductSizeId) {
		this.orderProductSizeId = orderProductSizeId;
	}

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false, updatable = false)
	@NotNull
	public OrdersVO getOrders() {
		return orders;
	}
	public void setOrders(OrdersVO orders) {
		this.orders = orders;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_size_id")
	public ProdSizeConnectVO getProdSizeConnect() {
		return prodSizeConnect;
	}
	public void setProdSizeConnect(ProdSizeConnectVO prodSizeConnect) {
		this.prodSizeConnect = prodSizeConnect;
	}
	
	@Column(name = "quantity", nullable = false)
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "price", nullable = false)
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(name = "custom_product_id")
	public Integer getCustomProductId() {
		return customProductId;
	}
	public void setCustomProductId(Integer customProductId) {
		this.customProductId = customProductId;
	}
	
	@Column(name = "is_custom", nullable = false)
	public Boolean getIsCustom() {
		return isCustom;
	}
	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}
	
	@Column(name = "create_time", nullable = false, updatable = false)
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	
}
