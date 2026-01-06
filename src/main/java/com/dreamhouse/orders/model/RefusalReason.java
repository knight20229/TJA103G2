package com.dreamhouse.orders.model;

public enum RefusalReason {

    USED("商品已使用"),
    DAMAGED("商品毀損"),
    CUSTOMIZED("客製化商品"),
    EXPIRED("超過鑑賞期"),
    PERSONAL("個人因素");

    private final String label;

    RefusalReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
