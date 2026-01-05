package com.dreamhouse.orders.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.dreamhouse.orderproductsize.model.OrderProductSizeVO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "orders")
public class OrdersVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer orderId;
	private Integer memberId;
	private Double amount;
	private Double discount;
	private Double payment;
	private String orderStatus;
	private String receivingName;
	private String receivingAddress;
	private String receivingPhone;
	private Integer promotionId;
	private Integer couponId;
	private String paymentMethod;
	private String paymentStatus;
	private String returnReason;
	private String returnText;
	private String returnStatus;
	private String refusalReason;
	private Timestamp orderCreateTime;
	private Timestamp orderUpdateTime;
	private Timestamp paymentCreateTime;
	private Timestamp returnCreateTime;
	private Timestamp returnApproveTime;
	private List<OrderProductSizeVO> orderproductsize = new ArrayList<>();
	
	public OrdersVO() {} // 無參數建構子

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@Column(name = "member_id", nullable = false)
	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	
	@Column(name = "amount", nullable = false)
	@NotNull
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name = "discount")
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@Column(name = "payment", nullable = false)
	@NotNull
	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}
	
	@Column(name = "order_status", nullable = false)
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	@Column(name = "receiving_name", nullable = false)
	@NotBlank(message="收件人姓名: 請勿空白")
	public String getReceivingName() {
		return receivingName;
	}

	public void setReceivingName(String receivingName) {
		this.receivingName = receivingName;
	}
	
	@Column(name = "receiving_address", nullable = false)
	@NotBlank(message="收件人地址: 請勿空白")
	public String getReceivingAddress() {
		return receivingAddress;
	}

	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}

	@Column(name = "receiving_phone", nullable = false)
	@NotBlank(message="收件人電話: 請勿空白")
	public String getReceivingPhone() {
		return receivingPhone;
	}

	public void setReceivingPhone(String receivingPhone) {
		this.receivingPhone = receivingPhone;
	}

	@Column(name = "promotions_id")
	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name = "coupon_id")
	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	@Column(name = "payment_method")
	@NotNull(message="付款方式: 請選擇一種付款方式")
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	@Column(name = "payment_status", nullable = false)
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	
	@Column(name = "return_reason")
	public String getReturnReason() {
//		if (returnReason == null) {
//			returnReason = "無退貨";
//		}
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	
	@Column(name = "return_text")
	public String getReturnText() {
		return returnText;
	}

	public void setReturnText(String returnText) {
		this.returnText = returnText;
	}
	
	@Column(name = "return_status")
	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}
	
	@Column(name = "refusal_reason", length = 100)
	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}
	
	@Column(name = "orderCreate_time", insertable = false, updatable = false)
	public Timestamp getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Timestamp orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	
	@Column(name = "orderUpdate_time", insertable = false)
	public Timestamp getOrderUpdateTime() {
		return orderUpdateTime;
	}

	public void setOrderUpdateTime(Timestamp orderUpdateTime) {
		this.orderUpdateTime = orderUpdateTime;
	}
	
	@Column(name = "paymentCreate_time", insertable = false, updatable = false)
	public Timestamp getPaymentCreateTime() {
		return paymentCreateTime;
	}

	public void setPaymentCreateTime(Timestamp paymentCreateTime) {
		this.paymentCreateTime = paymentCreateTime;
	}

	@Column(name = "returnCreate_time", insertable = false, updatable = false)
	public Timestamp getReturnCreateTime() {
		return returnCreateTime;
	}

	public void setReturnCreateTime(Timestamp returnCreateTime) {
		this.returnCreateTime = returnCreateTime;
	}
	
	@Column(name = "returnApprove_time", insertable = false, updatable = false)
	public Timestamp getReturnApproveTime() {
		return returnApproveTime;
	}

	public void setReturnApproveTime(Timestamp returnApproveTime) {
		this.returnApproveTime = returnApproveTime;
	}

	@OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	public List<OrderProductSizeVO> getOrderproductsize() {
		return orderproductsize;
	}

	public void setOrderproductsize(List<OrderProductSizeVO> orderproductsize) {
		this.orderproductsize = orderproductsize;
	}
}

