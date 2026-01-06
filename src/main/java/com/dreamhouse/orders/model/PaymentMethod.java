package com.dreamhouse.orders.model;

public enum PaymentMethod {

    CREDIT_CARD("信用卡"),
    BANK_TRANSFER("匯款");

    private final String label;

    PaymentMethod(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
