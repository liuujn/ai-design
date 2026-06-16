```java
package com.example.app.menu.model.dto.response;

import lombok.Data;

@Data
public class MenuVO {
    private String id;
    private String parentId;
    private String label;
    private String icon;
    private String pageTitle;
    private String pageSrc;
    private Integer sortOrder;
    private Boolean isDivider;
    private String status;
    private String createdAt;
    private String updatedAt;
}
```
---
