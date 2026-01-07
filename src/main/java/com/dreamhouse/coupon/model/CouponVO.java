package com.dreamhouse.coupon.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.dreamhouse.emp.model.EmpVO;
import com.dreamhouse.memcoupon.model.MemCouponVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name ="coupon")
public class CouponVO {
	
	
	private Integer couponId;
	private EmpVO empVO;
	private String name;
	private String type;
	private Integer couponValue;
	private Integer state;
	private Integer standard;
	private LocalDate startDt;
	private LocalDate endDt;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	private Set<MemCouponVO> memCoup;
	
	
	@Id
	@Column(name = "coupon_id", updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getCouponId() {
		return couponId;
	}
	
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	
	@ManyToOne
	@JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
	@NotNull(message = "請輸入員工編號")
	public EmpVO getEmpVO() {
		return empVO;
	}

	public void setEmpVO(EmpVO empVO) {
		this.empVO = empVO;
	}


	@Column(name = "`name`", length = 20)
	@NotEmpty(message = "請輸入優惠券名稱")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$", message = "優惠券名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到20之間")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "`type`")
	@NotEmpty(message = "請選擇折扣類型")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "coupon_value")
	@NotNull(message = "請輸入折扣數值")
	@Min(value = 1, message = "折扣數值必須是正整數")
	public Integer getCouponValue() {
		return couponValue;
	}
	
	public void setCouponValue(Integer couponValue) {
		this.couponValue = couponValue;
	}
	
	@Column(name = "state")
	@NotNull(message = "請選擇啟用狀態")
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	@Column(name = "standard")
	@NotNull(message = "請輸入使用門檻")
	@Min(value = 1, message = "使用門檻必須是正整數")
	public Integer getStandard() {
		return standard;
	}
	
	public void setStandard(Integer standard) {
		this.standard = standard;
	}
	
	@Column(name = "start_dt")
	@NotNull(message = "請輸入開始日期")
	@Future(message = "日期必須在今日(不含)之後")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public LocalDate getStartDt() {
		return startDt;
	}
	
	public void setStartDt(LocalDate startDt) {
		this.startDt = startDt;
	}
	
	@Column(name = "end_dt")
	@NotNull(message = "請輸入結束日期")
	@Future(message = "日期必須在今日(不含)之後")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public LocalDate getEndDt() {
		return endDt;
	}
	
	public void setEndDt(LocalDate endDt) {
		this.endDt = endDt;
	}
	
	@Column(name = "create_time", insertable = false, updatable = false)
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "update_time", insertable = false, updatable = false)
	public LocalDateTime getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(mappedBy = "couponId")
	public Set<MemCouponVO> getMemCoup() {
		return memCoup;
	}

	public void setMemCoup(Set<MemCouponVO> memCoup) {
		this.memCoup = memCoup;
	}
	

	
}
