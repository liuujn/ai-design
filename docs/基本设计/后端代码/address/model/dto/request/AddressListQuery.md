# AddressListQuery

```java
package com.example.app.address.model.dto.request;

import lombok.Data;

@Data
public class AddressListQuery {
    private String userId;
    private String addressType;
    private String recipientName;
    private int page = 1;
    private int size = 20;

    public int getOffset() { return (page - 1) * size; }
}
```
