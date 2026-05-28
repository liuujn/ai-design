# UserStatus

```java
package com.example.user.model.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("active", "正常"),
    INACTIVE("inactive", "禁用"),
    SUSPENDED("suspended", "挂起");

    private final String code;
    private final String label;

    UserStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static UserStatus fromCode(String code) {
        for (UserStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        throw new IllegalArgumentException("Invalid status: " + code);
    }

    public static boolean isValid(String code) {
        for (UserStatus s : values()) {
            if (s.code.equals(code)) return true;
        }
        return false;
    }

    public static void validateTransition(String currentStatus, String targetStatus) {
        if (currentStatus.equals(targetStatus)) return;
        if (SUSPENDED.code.equals(currentStatus) && ACTIVE.code.equals(targetStatus)) return;
        if (ACTIVE.code.equals(currentStatus) && INACTIVE.code.equals(targetStatus)) return;
        if (INACTIVE.code.equals(currentStatus) && ACTIVE.code.equals(targetStatus)) return;
        throw new IllegalStateException("Cannot transition from " + currentStatus + " to " + targetStatus);
    }
}
```
