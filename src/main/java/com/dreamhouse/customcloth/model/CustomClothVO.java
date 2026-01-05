package com.dreamhouse.customcloth.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.dreamhouse.customprod.model.CustomProductVO;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name ="CUSTOM_CLOTH")
public class CustomClothVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_CLOTH_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer customClothId;
	
	@Column(name ="CUSTOM_CLOTH_NAME")
	@NotEmpty(message="布料名稱:請勿空白")
	@Pattern(regexp = "^[\u4e00-\\u9fa5]{2,5}$", message = "布料名稱只能是中文,長度需在2到5")
	private String customClothName;
	
	@Column(name ="CUSTOM_CLOTH_PRICE")
	@NotNull(message="布料價格:請勿空白")
	@Min(value = 1000, message = "布料價格:不能小於{value}")
	@Max(value = 10000, message = "布料價格:不能大於{value}")
	private Integer customClothPrice;
	
	@OneToMany(mappedBy="clothVO")
	private Set<CustomProductVO> clothProducts = new HashSet<>();
	
	public CustomClothVO() {}	//無參數建構子

	public Integer getCustomClothId() {
		return customClothId;
	}

	public void setCustomClothId(Integer customClothId) {
		this.customClothId = customClothId;
	}

	public String getCustomClothName() {
		return customClothName;
	}

	public void setCustomClothName(String customClothName) {
		this.customClothName = customClothName;
	}

	public Integer getCustomClothPrice() {
		return customClothPrice;
	}

	public void setCustomClothPrice(Integer customClothPrice) {
		this.customClothPrice = customClothPrice;
	}

	public Set<CustomProductVO> getClothProducts() {
		return clothProducts;
	}

	public void setClothProducts(Set<CustomProductVO> clothProducts) {
		this.clothProducts = clothProducts;
	}

}