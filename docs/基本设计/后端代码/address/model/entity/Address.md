# Address

```java
package com.example.app.address.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Address {
    private String id;
    private String userId;
    private String addressType;
    private String recipientName;
    private String recipientPhone;
    private String country;
    private String province;
    private String city;
    private String district;
    private String street;
    private String postalCode;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
```
