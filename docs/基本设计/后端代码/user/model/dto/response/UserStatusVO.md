# UserStatusVO

```java
package com.example.user.model.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserStatusVO {
    private String id;
    private String status;
    private LocalDateTime updatedAt;
}
```
