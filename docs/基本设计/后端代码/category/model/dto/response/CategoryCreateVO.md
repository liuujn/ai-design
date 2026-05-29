# CategoryCreateVO

```java
package com.example.app.category.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CategoryCreateVO {
    private String id;
    private String name;
    private String description;
    private Integer sortOrder;
    private String createdAt;
}
```
