# AddressDeleteRequest

```java
package com.example.app.address.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDeleteRequest {
    @NotBlank(message = "更新时间不能为空")
    private String updatedAt;
}
```
