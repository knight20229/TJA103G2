package com.dreamhouse.customprod.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customsize.model.CustomSizeVO;
import com.dreamhouse.mem.model.MemVO;

import jakarta.persistence.*;

@Entity
@Table(name ="CUSTOM_PRODUCT")
public class CustomProductVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_PRODUCT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customProductId;
	
	@Column(name ="HAS_BED_FRAME")
	private Boolean hasBedFrame;
	
	@Column(name ="CUSTOM_PRICE")
	private Integer customPrice;
		
	@Column(name ="CREATE_TIME")
	private LocalDateTime createTime;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_CLOTH_ID")
	private CustomClothVO clothVO;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_MATERIAL_ID")
	private CustomMaterialVO materialVO;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_FEATURE_ID")
	private CustomFeatureVO featureVO;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_SIZE_ID")
	private CustomSizeVO sizeVO;
	
	@ManyToOne
	@JoinColumn(name ="member_id")
	private MemVO memVO;

	public Integer getCustomProductId() {
		return customProductId;
	}

	public void setCustomProductId(Integer customProductId) {
		this.customProductId = customProductId;
	}

	public Boolean getHasBedFrame() {
		return hasBedFrame;
	}

	public void setHasBedFrame(Boolean hasBedFrame) {
		this.hasBedFrame = hasBedFrame;
	}

	public Integer getCustomPrice() {
		return customPrice;
	}

	public void setCustomPrice(Integer customPrice) {
		this.customPrice = customPrice;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public CustomClothVO getClothVO() {
		return clothVO;
	}

	public void setClothVO(CustomClothVO clothVO) {
		this.clothVO = clothVO;
	}

	public CustomMaterialVO getMaterialVO() {
		return materialVO;
	}

	public void setMaterialVO(CustomMaterialVO materialVO) {
		this.materialVO = materialVO;
	}

	public CustomFeatureVO getFeatureVO() {
		return featureVO;
	}

	public void setFeatureVO(CustomFeatureVO featureVO) {
		this.featureVO = featureVO;
	}

	public CustomSizeVO getSizeVO() {
		return sizeVO;
	}

	public void setSizeVO(CustomSizeVO sizeVO) {
		this.sizeVO = sizeVO;
	}
	
	public MemVO getMemVO() {
		return memVO;
	}

	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}
}
