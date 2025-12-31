package com.dreamhouse.customcloth.model;

import java.io.Serializable;
import java.util.*;

import com.dreamhouse.customprod.model.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name ="CUSTOM_CLOTH")
public class ClothVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="CUSTOM_CLOTH_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)	//Primary Key 值由identity 自動產生
	private Integer cloth;
	
	@Column(name ="CUSTOM_CLOTH_NAME")
	@NotEmpty(message="布料名稱:請勿空白")
	@Pattern(regexp = "^[(\u4e00-\\u9fa5)]{2,5}$", message = "布料名稱只能是中文,長度需在2到5")
	private String clothName;
	
	@Column(name ="CLOTH_PRICE")
	@NotNull(message="布料價格:請勿空白")
	@Min(value = 1000, message = "布料價格:不能小於{value}")
	@Min(value = 10000, message = "布料價格:不能大於{value}")
	private Integer price;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="clothVo")
	@OrderBy("customClothId")
	private Set<CustomProductVO> clothproducts = new HashSet<CustomProductVO>();
	
	public ClothVO() {}	//無參數建構子

	public Integer getCloth() {
		return cloth;
	}

	public void setCloth(Integer cloth) {
		this.cloth = cloth;
	}

	public String getClothName() {
		return clothName;
	}

	public void setClothName(String clothName) {
		this.clothName = clothName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Set<CustomProductVO> getClothproducts() {
		return clothproducts;
	}

	public void setClothproducts(Set<CustomProductVO> clothproducts) {
		this.clothproducts = clothproducts;
	}
}