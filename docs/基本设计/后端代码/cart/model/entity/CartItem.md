# CartItem

```java
package com.example.app.cart.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CartItem {
    private String id;
    private String cartId;
    private String productId;
    private String productName;
    private Integer quantity;
    private java.math.BigDecimal unitPrice;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
```
