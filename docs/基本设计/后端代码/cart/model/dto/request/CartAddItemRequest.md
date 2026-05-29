# CartAddItemRequest

```java
package com.example.app.cart.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartAddItemRequest {
    @NotBlank(message = "商品ID不能为空")
    private String productId;

    @Min(value = 1, message = "数量必须大于0")
    private Integer quantity;
}
```
