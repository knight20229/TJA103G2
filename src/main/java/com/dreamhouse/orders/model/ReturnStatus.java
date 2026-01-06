package com.dreamhouse.orders.model;

public enum ReturnStatus {

    APPLYING("申請中"),
    RETURNING("退貨中"),
    COMPLETED("退貨完成"),
    REJECTED("退貨拒絕");

    private final String label;

    ReturnStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
