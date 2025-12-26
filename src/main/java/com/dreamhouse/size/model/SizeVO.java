package com.dreamhouse.size.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT_SIZE")
public class SizeVO  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer sizeId;
    private Integer width;
    private Integer length;
    private LocalDateTime createTime;
    
    public SizeVO() { // 無參數建構子
    	
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIZE_ID")
	public Integer getSizeId() {
		return sizeId;
	}

	public void setSizeId(Integer sizeId) {
		this.sizeId = sizeId;
	}

    @Column(name = "WIDTH")
    @NotNull(message = "寬度: 請勿空白")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

    @Column(name = "LENGTH")
    @NotNull(message = "寬度: 請勿空白")
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

    @Column(name = "CREATE_TIME", insertable = false, updatable = false)
	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

}
