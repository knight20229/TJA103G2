package com.dreamhouse.customprod.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.dreamhouse.customcloth.model.ClothVO;
import com.dreamhouse.customfeature.model.FeatureVO;
import com.dreamhouse.custommaterial.model.MaterialVO;
import com.dreamhouse.customsize.model.SizeVO;

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
	private ClothVO clothVo;
	
	@Column(name ="HAS_BED_FRAME")
	private Boolean hasBedFrame;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_MATERIAL_ID")
	private MaterialVO materialVo;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_FEATURE_ID")
	private FeatureVO featureVo;
	
	@ManyToOne
	@JoinColumn(name ="CUSTOM_SIZE_ID")
	private SizeVO customSizeVo;
	
	@Column(name ="CUSTOM_PRICE")
	private Integer customPrice;
	
//	@ManyToOne
//	private MemVo memberId;
	
	@Column(name ="CREATE_TIME")
	private LocalDateTime createTime;
	
}
