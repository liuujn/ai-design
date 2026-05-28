# 地址管理 - 后端代码

## 包结构

```
com/example/address/
├── controller/
│   └── AddressController.java        # REST控制器，提供地址CRUD + 设为默认的API端点
├── service/
│   ├── AddressService.java           # 地址管理业务接口定义
│   └── impl/
│       └── AddressServiceImpl.java   # 地址管理业务实现，含分页/乐观锁/默认地址逻辑
├── mapper/
│   ├── AddressMapper.java            # MyBatis Mapper接口，定义地址表数据库操作
│   └── resources/mapper/
│       └── AddressMapper.xml         # MyBatis XML映射，含分页/逻辑删除/乐观锁SQL
├── model/
│   ├── entity/
│   │   └── Address.java              # 地址实体，映射addresses表全部字段
│   ├── enums/
│   │   └── AddressType.java          # 地址类型枚举(shipping/billing)
│   └── dto/
│       ├── request/
│       │   ├── AddressCreateRequest.java  # 创建地址请求参数
│       │   ├── AddressListQuery.java      # 地址分页查询参数(类型/收件人/页码/每页大小)
│       │   ├── AddressUpdateRequest.java  # 更新地址请求参数，含乐观锁updatedAt
│       │   ├── AddressDeleteRequest.java  # 删除地址请求参数(乐观锁updatedAt)
│       │   └── AddressDefaultRequest.java # 设为默认地址请求参数(乐观锁updatedAt)
│       └── response/
│           ├── AddressListVO.java         # 地址列表响应VO
│           ├── AddressDetailVO.java       # 地址详情响应VO
│           ├── AddressCreateVO.java       # 创建地址响应VO
│           ├── AddressUpdateVO.java       # 更新地址响应VO(返回最新updatedAt)
│           └── AddressDefaultVO.java      # 设为默认地址响应VO
```

## 代码文件索引

### controller/

- [AddressController](controller/AddressController.md) — REST控制器，提供地址CRUD + 设为默认的API端点

### service/

- [AddressService](service/AddressService.md) — 地址管理业务接口定义
- [AddressServiceImpl](service/impl/AddressServiceImpl.md) — 地址管理业务实现，含分页、乐观锁、默认地址逻辑

### mapper/

- [AddressMapper](mapper/AddressMapper.md) — MyBatis Mapper接口，定义地址表数据库操作
- [AddressMapperXML](mapper/resources/mapper/AddressMapperXML.md) — MyBatis XML映射，含分页、逻辑删除、乐观锁SQL

### model/

#### entity

- [Address](model/entity/Address.md) — 地址实体，映射addresses表全部字段

#### enums

- [AddressType](model/enums/AddressType.md) — 地址类型枚举(shipping/billing)

#### dto/request

- [AddressCreateRequest](model/dto/request/AddressCreateRequest.md) — 创建地址请求参数
- [AddressListQuery](model/dto/request/AddressListQuery.md) — 地址分页查询参数(类型、收件人、页码、每页大小)
- [AddressUpdateRequest](model/dto/request/AddressUpdateRequest.md) — 更新地址请求参数，含乐观锁updatedAt
- [AddressDeleteRequest](model/dto/request/AddressDeleteRequest.md) — 删除地址请求参数(乐观锁updatedAt)
- [AddressDefaultRequest](model/dto/request/AddressDefaultRequest.md) — 设为默认地址请求参数(乐观锁updatedAt)

#### dto/response

- [AddressListVO](model/dto/response/AddressListVO.md) — 地址列表响应VO
- [AddressDetailVO](model/dto/response/AddressDetailVO.md) — 地址详情响应VO
- [AddressCreateVO](model/dto/response/AddressCreateVO.md) — 创建地址响应VO
- [AddressUpdateVO](model/dto/response/AddressUpdateVO.md) — 更新地址响应VO(返回最新updatedAt)
- [AddressDefaultVO](model/dto/response/AddressDefaultVO.md) — 设为默认地址响应VO
