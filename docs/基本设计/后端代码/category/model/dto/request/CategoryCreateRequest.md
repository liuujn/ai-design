# CategoryCreateRequest

```java
package com.example.app.category.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度应为1到100个字符")
    private String name;

    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;

    private Integer sortOrder;
}
```
