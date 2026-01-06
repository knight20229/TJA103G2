package com.dreamhouse.orders.model;

public enum PaymentStatus {

    UNPAID("未付款"),
    PAID("已付款"),
    FAILED("付款失敗");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
