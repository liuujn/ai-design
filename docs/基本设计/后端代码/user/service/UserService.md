# UserService

```java
package com.example.user.service;

import com.example.user.model.dto.request.*;
import com.example.user.model.dto.response.*;

public interface UserService {
    PageResult<UserListVO> list(UserListQuery query);
    UserDetailVO detail(String id);
    UserCreateVO create(UserCreateRequest request, String operatorId);
    UserUpdateVO update(String id, UserUpdateRequest request, String operatorId);
    void delete(String id, UserDeleteRequest request, String operatorId);
    UserStatusVO updateStatus(String id, UserStatusRequest request, String operatorId);
}
```
