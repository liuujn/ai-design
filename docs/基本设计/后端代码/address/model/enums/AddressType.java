package com.example.app.address.model.enums;

import java.util.Set;

public enum AddressType {
    SHIPPING("shipping", "收货地址"),
    BILLING("billing", "账单地址");

    private final String code;
    private final String label;

    AddressType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    private static final Set<String> VALID_CODES = Set.of("shipping", "billing");

    public static boolean isValid(String code) {
        return code != null && VALID_CODES.contains(code);
    }
}
