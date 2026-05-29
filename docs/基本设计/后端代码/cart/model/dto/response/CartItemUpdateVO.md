# CartItemUpdateVO

```java
package com.example.app.cart.model.dto.response;

import lombok.Data;

@Data
public class CartItemUpdateVO {
    private String id;
    private String productId;
    private String productName;
    private Integer quantity;
    private String unitPrice;
    private String subtotal;
    private String updatedAt;
}
```
