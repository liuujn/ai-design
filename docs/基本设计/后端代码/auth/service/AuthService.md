# AuthService

```java
package com.example.auth.service;

import com.example.auth.model.dto.request.LoginRequest;
import com.example.auth.model.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
```
