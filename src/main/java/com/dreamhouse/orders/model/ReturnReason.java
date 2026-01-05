package com.dreamhouse.orders.model;

public enum ReturnReason {

    DEFECTIVE("商品瑕疵"),
    WRONG_SIZE("尺寸不合"),
    WRONG_ITEM("寄錯商品"),
    OTHER("其他");

    private final String label;

    ReturnReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
