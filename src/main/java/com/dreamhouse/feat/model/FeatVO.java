package com.dreamhouse.feat.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.*;

import com.dreamhouse.prod.model.ProdVO;

@Entity
@Table(name = "PRODUCT_FEATURE")
public class FeatVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer featureId;
    private String featureName;
    private LocalDateTime createTime;
    private Set<ProdVO> prods = new HashSet<>(); // 多對多關聯

    public FeatVO() { // 無參數建構子
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEATURE_ID")
    public Integer getFeatureId() {
        return this.featureId;
    }

    public void setFeatureId(Integer featureId) {
        this.featureId = featureId;
    }

    @Column(name = "FEATURE_NAME")
    @NotEmpty(message="商品功能: 請勿空白") 
    public String getFeatureName() {
        return this.featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @Column(name = "CREATE_TIME", insertable = false, updatable = false)
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    @ManyToMany(mappedBy = "features") 
    public Set<ProdVO> getProds() {
        return prods;
    }

    public void setProds(Set<ProdVO> prods) {
        this.prods = prods;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatVO featVO = (FeatVO) o;
        return Objects.equals(featureId, featVO.featureId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId);
    }
}