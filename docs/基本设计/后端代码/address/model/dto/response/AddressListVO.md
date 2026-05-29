# AddressListVO

```java
package com.example.app.address.model.dto.response;

import lombok.Data;

@Data
public class AddressListVO {
    private String id;
    private String userId;
    private String addressType;
    private String recipientName;
    private String recipientPhone;
    private String fullAddress;
    private Boolean isDefault;
    private String createdAt;
    private String updatedAt;
}
```
