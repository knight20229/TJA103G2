package com.dreamhouse.prod.model;

import jakarta.persistence.*;
import com.dreamhouse.size.model.SizeVO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "PRODUCT_SIZE_CONNECT")
public class ProdSizeConnectVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer productSizeId; 
    private ProdVO prodVO;         // PRODUCT_ID (FK)
    private SizeVO sizeVO;         // SIZE_ID (FK)
    private Integer price;         
    private Integer stock = 0;         
    private Integer status = 0;        // (0: 無庫存 / 1: 有庫存)

    public ProdSizeConnectVO() { // 無參數建構子
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_SIZE_ID")
    public Integer getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(Integer productSizeId) {
        this.productSizeId = productSizeId;
    }

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID") 
    @NotNull(message="商品資料: 請勿空白")
    public ProdVO getProdVO() {
        return prodVO;
    }

    public void setProdVO(ProdVO prodVO) {
        this.prodVO = prodVO;
    }

    @ManyToOne
    @JoinColumn(name = "SIZE_ID") 
    @NotNull(message="尺寸規格: 請勿空白")
    public SizeVO getSizeVO() {
        return sizeVO;
    }

    public void setSizeVO(SizeVO sizeVO) {
        this.sizeVO = sizeVO;
    }

    @Column(name = "PRICE")
    @NotNull(message="價格: 請勿空白")
    @Min(value = 0, message = "價格: 不能小於{value}")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Column(name = "STOCK")
    @NotNull(message="庫存: 請勿空白")
    @Min(value = 0, message = "庫存: 不能小於{value}")
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
        this.status = (stock != null && stock > 0) ? 1 : 0;
    }
    
    @Column(name = "STATUS")
    @NotNull(message="狀態: 請勿空白")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}