# CartAddressRequest

```java
package com.example.app.cart.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartAddressRequest {
    @NotBlank(message = "地址ID不能为空")
    private String addressId;

    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
```
