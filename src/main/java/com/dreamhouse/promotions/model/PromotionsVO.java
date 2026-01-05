package com.dreamhouse.promotions.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


@Entity
@Table(name = "promotions")
public class PromotionsVO {

	private Integer promotionsId;
	private Integer employeeId;
//	private EmpVO employeeId;
	private String name;
	private String type;
	private Integer promotionsValue;
	private Integer state;
	private String narrate;
	private LocalDate startDt;
	private LocalDate endDt;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;

	
	@Id
	@Column(name = "promotions_id", updatable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPromotionsId() {
		return promotionsId;
	}

	public void setPromotionsId(Integer promotionsId) {
		this.promotionsId = promotionsId;
	}

	@Column(name = "employee_id")
	@NotNull(message = "請輸入員工編號")
	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
	
//	@ManyToOne
//	@JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
//	@NotNull(message = "請輸入員工編號")
//	public EmpVO getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(EmpVO employeeId) {
//		this.employeeId = employeeId;
//	}

	@Column(name = "`name`", length = 50)
	@NotEmpty(message = "請輸入促銷活動名稱")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$", message = "促銷活動名稱: 只能是中、英文字母、數字和_ , 且長度必需在2到20之間")
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

	@Column(name = "promotions_value")
	@NotNull(message = "請輸入折扣數值")
	@Min(value = 1, message = "折扣數值必須是正整數")
	public Integer getPromotionsValue() {
		return promotionsValue;
	}

	public void setPromotionsValue(Integer promotionsValue) {
		this.promotionsValue = promotionsValue;
	}

	@Column(name = "state")
	@NotNull(message = "請選擇啟用狀態")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Column(name = "narrate", length = 30)
	@NotEmpty(message = "請輸促銷活動說明")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_%)]{2,30}$", message = "促銷活動說明: 只能是中、英文字母、數字和_ , 且長度必需在2到30之間")
	public String getNarrate() {
		return narrate;
	}

	public void setNarrate(String narrate) {
		this.narrate = narrate;
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
	
}
