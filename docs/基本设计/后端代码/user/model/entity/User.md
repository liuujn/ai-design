# User

```java
package com.example.user.model.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String username;
    private String displayName;
    private String email;
    private String phone;
    private String passwordHash;
    private String status;
    private Boolean mfaEnabled;
    private String mfaSecret;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
```
