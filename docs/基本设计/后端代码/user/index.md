# 用户管理 - 后端代码

## 包结构

```
com/example/user/
├── controller/
│   ├── UserController.java        # REST控制器，提供用户CRUD + 状态管理的API端点
│   └── SecurityUtils.java         # 通过ThreadLocal获取当前操作用户ID的工具类
├── service/
│   ├── UserService.java           # 用户管理业务接口定义
│   └── impl/
│       ├── UserServiceImpl.java   # 用户管理业务实现，含分页/乐观锁/密码加密/状态校验
│       └── BusinessException.java # 携带错误码的业务异常类
├── mapper/
│   ├── UserMapper.java            # MyBatis Mapper接口，定义用户表数据库操作
│   └── resources/mapper/
│       └── UserMapper.xml         # MyBatis XML映射，含分页/逻辑删除/乐观锁SQL
├── model/
│   ├── entity/
│   │   └── User.java              # 用户实体，映射users表全部字段
│   ├── enums/
│   │   └── UserStatus.java        # 用户状态枚举(active/inactive/suspended)及状态转换校验
│   └── dto/
│       ├── request/
│       │   ├── UserCreateRequest.java  # 创建用户请求参数，含用户名校验/密码强度校验
│       │   ├── UserListQuery.java      # 用户分页查询参数(关键词/状态/页码/每页大小)
│       │   ├── UserUpdateRequest.java  # 更新用户请求参数，含乐观锁updatedAt
│       │   ├── UserStatusRequest.java  # 修改用户状态请求参数
│       │   └── UserDeleteRequest.java  # 删除用户请求参数(乐观锁updatedAt)
│       └── response/
│           ├── PageResult.java         # 通用分页结果包装
│           ├── UserListVO.java         # 用户列表响应VO
│           ├── UserDetailVO.java       # 用户详情响应VO
│           ├── UserCreateVO.java       # 创建用户响应VO
│           ├── UserUpdateVO.java       # 更新用户响应VO(返回最新updatedAt)
│           └── UserStatusVO.java       # 修改状态响应VO(返回最新状态和时间)
└── config/
    ├── WebMvcConfig.java            # Web MVC配置(CORS) + BCryptPasswordEncoder Bean
    ├── MyBatisConfig.java           # MyBatis配置(数据源/驼峰映射/日志/XML位置)
    └── GlobalExceptionHandler.java  # 全局异常处理(业务异常/参数校验/系统异常)
```

## 代码文件索引

### controller/

- [UserController](controller/UserController.md) — REST控制器，提供用户CRUD + 状态管理的API端点
- [SecurityUtils](controller/SecurityUtils.md) — 通过ThreadLocal获取当前操作用户ID的工具类

### service/

- [UserService](service/UserService.md) — 用户管理业务接口定义
- [UserServiceImpl](service/impl/UserServiceImpl.md) — 用户管理业务实现，含分页、乐观锁、密码加密、状态校验
- [BusinessException](service/impl/BusinessException.md) — 携带错误码的业务异常类

### mapper/

- [UserMapper](mapper/UserMapper.md) — MyBatis Mapper接口，定义用户表数据库操作
- [UserMapperXML](mapper/resources/mapper/UserMapperXML.md) — MyBatis XML映射，含分页、逻辑删除、乐观锁SQL

### model/

#### entity

- [User](model/entity/User.md) — 用户实体，映射users表全部字段

#### enums

- [UserStatus](model/enums/UserStatus.md) — 用户状态枚举(active/inactive/suspended)及状态转换校验

#### dto/request

- [UserCreateRequest](model/dto/request/UserCreateRequest.md) — 创建用户请求参数，含用户名校验、密码强度校验
- [UserListQuery](model/dto/request/UserListQuery.md) — 用户分页查询参数(关键词、状态、页码、每页大小)
- [UserUpdateRequest](model/dto/request/UserUpdateRequest.md) — 更新用户请求参数，含乐观锁updatedAt
- [UserStatusRequest](model/dto/request/UserStatusRequest.md) — 修改用户状态请求参数
- [UserDeleteRequest](model/dto/request/UserDeleteRequest.md) — 删除用户请求参数(乐观锁updatedAt)

#### dto/response

- [PageResult](model/dto/response/PageResult.md) — 通用分页结果包装
- [UserListVO](model/dto/response/UserListVO.md) — 用户列表响应VO
- [UserDetailVO](model/dto/response/UserDetailVO.md) — 用户详情响应VO
- [UserCreateVO](model/dto/response/UserCreateVO.md) — 创建用户响应VO
- [UserUpdateVO](model/dto/response/UserUpdateVO.md) — 更新用户响应VO(返回最新updatedAt)
- [UserStatusVO](model/dto/response/UserStatusVO.md) — 修改状态响应VO(返回最新状态和时间)

### config/

- [WebMvcConfig](config/WebMvcConfig.md) — Web MVC配置(CORS) + BCryptPasswordEncoder Bean
- [MyBatisConfig](config/MyBatisConfig.md) — MyBatis配置(数据源、驼峰映射、日志、XML位置)
- [GlobalExceptionHandler](config/GlobalExceptionHandler.md) — 全局异常处理(业务异常、参数校验、系统异常)
