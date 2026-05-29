# ProductStatus

```java
package com.example.app.product.model.enums;

import java.util.Set;

public enum ProductStatus {
    ACTIVE("active", "上架"),
    INACTIVE("inactive", "下架");

    private final String code;
    private final String label;

    ProductStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() { return code; }
    public String getLabel() { return label; }

    private static final Set<String> VALID_CODES = Set.of("active", "inactive");

    public static boolean isValid(String code) {
        return code != null && VALID_CODES.contains(code);
    }

    public static void validateTransition(String currentStatus, String targetStatus) {
        if (!isValid(targetStatus)) {
            throw new IllegalArgumentException("无效的商品状态值: " + targetStatus);
        }
    }
}
```
