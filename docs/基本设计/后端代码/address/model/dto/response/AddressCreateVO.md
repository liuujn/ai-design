# AddressCreateVO

```java
package com.example.app.address.model.dto.response;

import lombok.Data;

@Data
public class AddressCreateVO {
    private String id;
    private String addressType;
    private String recipientName;
    private String recipientPhone;
    private Boolean isDefault;
    private String createdAt;
}
```
