# UserStatusRequest

```java
package com.example.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserStatusRequest {
    @NotBlank(message = "E019")
    private String status;

    @NotBlank(message = "E018")
    private String updatedAt;
}
```
