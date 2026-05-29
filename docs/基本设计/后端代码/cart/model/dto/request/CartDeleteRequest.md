# CartDeleteRequest

```java
package com.example.app.cart.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartDeleteRequest {
    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
```
