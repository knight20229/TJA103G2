package com.dreamhouse.memcoupon.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import com.dreamhouse.coupon.model.CouponVO;
import com.dreamhouse.mem.model.MemVO;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table (name = "member_coupon_mapping")
public class MemCouponVO {
	
	private MemCouponCompositeKey memCouponKey;
	private Integer useStatus;
	private LocalDateTime couponUpdateTime;
	private CouponVO couponId;
	private MemVO memberId;
	private CouponVO couponVO;
	private MemVO memVO;
	
	@EmbeddedId
	public MemCouponCompositeKey getMemCouponKey() {
		return memCouponKey;
	}

	public void setMemCouponKey(MemCouponCompositeKey memCouponKey) {
		this.memCouponKey = memCouponKey;
	}

	@Column(name = "use_status")
	public Integer getUseStatus() {
		return useStatus;
	}
	
	public void setUseStatus(Integer useStatus) {
		this.useStatus = useStatus;
	}
	
	@Column(name = "coupon_update_time", updatable = false, insertable = false)
	public LocalDateTime getCouponUpdateTime() {
		return couponUpdateTime;
	}
	
	public void setCouponUpdateTime(LocalDateTime couponUpdateTime) {
		this.couponUpdateTime = couponUpdateTime;
	}

	
	@ManyToOne
	@JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id")
	@MapsId("couponId") // 對應複合主鍵中的 couponId
	public CouponVO getCouponId() {
		return couponId;
	}

	public void setCouponId(CouponVO couponId) {
		this.couponId = couponId;
	}

	@ManyToOne
	@JoinColumn(name = "member_id", referencedColumnName = "member_id")
	@MapsId("memberId") // 對應複合主鍵中的 memberId
	public MemVO getMemberId() {
		return memberId;
	}

	public void setMemberId(MemVO memberId) {
		this.memberId = memberId;
	}
	
	
	
}
