# ProductUpdateVO

```java
package com.example.app.product.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductUpdateVO {
    private String id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String status;
    private String updatedAt;
}
```
