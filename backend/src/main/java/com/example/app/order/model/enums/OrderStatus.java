package com.example.app.order.model.enums;

import java.util.Set;

public enum OrderStatus {
    PENDING("pending", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    SHIPPED("shipped", "已发货"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String label;

    OrderStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    private static final Set<String> VALID_CODES = Set.of("pending", "confirmed", "shipped", "completed", "cancelled");

    public static boolean isValid(String code) {
        return code != null && VALID_CODES.contains(code);
    }

    public static void validateTransition(String currentStatus, String targetStatus) {
        if (!isValid(targetStatus)) {
            throw new IllegalArgumentException("无效的订单状态值: " + targetStatus);
        }
    }
}
