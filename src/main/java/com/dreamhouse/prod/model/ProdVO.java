package com.dreamhouse.prod.model;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.feat.model.FeatVO;

@Entity
@Table(name = "PRODUCT") 
public class ProdVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer productId; 
    private String productName;
    private Boolean productType; // 0:床墊 ; 1:床架
    private byte[] imageData; 
    private Boolean status = false; 
    private String material; 
    private String description; 
    private LocalDateTime onDate; 
    private LocalDateTime offDate; 
    private LocalDateTime createTime; 
    private LocalDateTime updateTime;
    private List<ProdSizeConnectVO> prodSizeConnects = new ArrayList<>();
    private List<FeatVO> features = new ArrayList<>();
    
    public ProdVO() {} // 無參數建構子
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "PRODUCT_ID") 
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Column(name = "PRODUCT_NAME", length = 255)
    @NotEmpty(message="商品名稱: 請勿空白") 
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name = "PRODUCT_TYPE", columnDefinition = "TINYINT")
    @NotNull(message="商品類型: 請勿空白") 
    public Boolean getProductType() {
        return productType;
    }

    public void setProductType(Boolean productType) {
        this.productType = productType;
    }

    @Lob 
    @Column(name = "IMAGE_DATA", columnDefinition = "LONGBLOB") 
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @Column(name = "STATUS", columnDefinition = "TINYINT")
    @NotNull(message="商品狀態: 請勿空白") 
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Column(name = "MATERIAL", length = 100)
    @NotEmpty(message="商品材質: 請勿空白") 
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Lob
    @Column(name = "DESCRIPTION", columnDefinition = "LONGTEXT") 
    @NotEmpty(message="商品描述: 請勿空白") 
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "ON_DATE") 
    @NotNull(message="上架日期: 請勿空白") 
    public LocalDateTime getOnDate() {
        return onDate;
    }

    public void setOnDate(LocalDateTime onDate) {
        this.onDate = onDate;
    }

    @Column(name = "OFF_DATE") 
    public LocalDateTime getOffDate() {
        return offDate;
    }

    public void setOffDate(LocalDateTime offDate) {
        this.offDate = offDate;
    }

    @Column(name = "CREATE_TIME", insertable = false, updatable = false) 
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_TIME", insertable = false, updatable = false) 
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "prodVO", orphanRemoval = true)
    public List<ProdSizeConnectVO> getProdSizeConnects() {
        return prodSizeConnects;
    }

    public void setProdSizeConnects(List<ProdSizeConnectVO> prodSizeConnects) {
        this.prodSizeConnects = prodSizeConnects;
    }
    
    @ManyToMany
    @JoinTable(
        name = "PRODUCT_FEATURE_MAP", 
        joinColumns = @JoinColumn(name = "PRODUCT_ID"),
        inverseJoinColumns = @JoinColumn(name = "FEATURE_ID")
    )
    public List<FeatVO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatVO> features) {
        this.features = features;
    }
}