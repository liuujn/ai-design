# UserListQuery

```java
package com.example.user.model.dto.request;

import lombok.Data;

@Data
public class UserListQuery {
    private String keyword;
    private String status;
    private int page = 1;
    private int size = 20;

    public int getOffset() {
        return (page - 1) * size;
    }
}
```
