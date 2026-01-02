package com.dreamhouse.customprod.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.dreamhouse.customcloth.model.CustomClothVO;
import com.dreamhouse.customfeature.model.CustomFeatureVO;
import com.dreamhouse.custommaterial.model.CustomMaterialVO;
import com.dreamhouse.customsize.model.CustomSizeVO;

import jakarta.persistence.*;

@Entity
@Table(name ="CUSTOM_PRODUCT")
public class CustomProductVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_PRODUCT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customProductId;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_CLOTH_ID")
	private CustomClothVO customClothVo;
	
	@Column(name ="HAS_BED_FRAME")
	private Boolean hasBedFrame;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_MATERIAL_ID")
	private CustomMaterialVO customMaterialVo;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_FEATURE_ID")
	private CustomFeatureVO customFeatureVo;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_SIZE_ID")
	private CustomSizeVO customSizeVo;
	
	@Column(name ="CUSTOM_PRICE")
	private Integer customPrice;
	
//	@ManyToOne
//	private MemVo memberId;
	
	@Column(name ="CREATE_TIME")
	private LocalDateTime createTime;
	
}
