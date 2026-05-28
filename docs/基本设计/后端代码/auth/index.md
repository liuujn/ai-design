# 用户登录 - 后端代码

## 包结构

```
com/example/auth/
├── controller/AuthController.java     # 登录REST端点
├── service/
│   ├── AuthService.java               # 业务接口
│   └── impl/AuthServiceImpl.java      # 认证逻辑（bcrypt校验 + JWT生成）
├── model/dto/
│   ├── request/LoginRequest.java      # 登录请求参数
│   └── response/LoginResponse.java    # 登录响应（token+用户信息）
└── config/
    ├── JwtUtil.java                   # JWT令牌签发/解析
    ├── JwtAuthFilter.java             # Token请求拦截过滤器
    └── SecurityConfig.java            # Spring Security配置
```

## 文件列表

- [AuthController.java](controller/AuthController.md)
- [AuthService.java](service/AuthService.md)
- [AuthServiceImpl.java](service/impl/AuthServiceImpl.md)
- [LoginRequest.java](model/dto/request/LoginRequest.md)
- [LoginResponse.java](model/dto/response/LoginResponse.md)
- [JwtUtil.java](config/JwtUtil.md)
- [JwtAuthFilter.java](config/JwtAuthFilter.md)
- [SecurityConfig.java](config/SecurityConfig.md)
