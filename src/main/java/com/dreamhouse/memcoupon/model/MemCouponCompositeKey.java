package com.dreamhouse.memcoupon.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class MemCouponCompositeKey implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer couponId;
	private Integer memberId;
	
	
	public Integer getCouponId() {
		return couponId;
	}
	
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	
	public Integer getMemberId() {
		return memberId;
	}
	
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	
	public MemCouponCompositeKey() {
		super();
	}
	
	public MemCouponCompositeKey(Integer couponId, Integer memberId) {
		super();
		this.couponId = couponId;
		this.memberId = memberId;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(couponId, memberId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemCouponCompositeKey other = (MemCouponCompositeKey) obj;
		return Objects.equals(couponId, other.couponId) && Objects.equals(memberId, other.memberId);
	}
	
	
	

}
