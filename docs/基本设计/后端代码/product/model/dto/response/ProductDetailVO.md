# ProductDetailVO

```java
package com.example.app.product.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDetailVO {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private String imageUrl;
    private String status;
    private String createdAt;
    private String updatedAt;
}
```
