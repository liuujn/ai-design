# CategoryListQuery

```java
package com.example.app.category.model.dto.request;

import lombok.Data;

@Data
public class CategoryListQuery {
    private String keyword;
    private String status;
    private Integer page = 1;
    private Integer size = 20;

    public int getOffset() {
        return (page - 1) * size;
    }
}
```
