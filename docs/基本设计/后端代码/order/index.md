# 订单管理 - 后端代码

## 包结构

```
com/example/order/
├── controller/
│   └── OrderController.java        # REST控制器，提供订单CRUD + 状态管理的API端点
├── service/
│   ├── OrderService.java           # 订单管理业务接口定义
│   └── impl/
│       └── OrderServiceImpl.java   # 订单管理业务实现，含分页/乐观锁/状态流转/明细管理
├── mapper/
│   ├── OrderMapper.java            # MyBatis Mapper接口，定义订单表及明细表数据库操作
│   └── resources/mapper/
│       └── OrderMapper.xml         # MyBatis XML映射，含分页/关联查询/乐观锁SQL
├── model/
│   ├── entity/
│   │   ├── Order.java              # 订单实体，映射orders表全部字段
│   │   └── OrderItem.java          # 订单明细实体，映射order_items表全部字段
│   ├── enums/
│   │   └── OrderStatus.java        # 订单状态枚举(pending/confirmed/shipped/completed/cancelled)
│   └── dto/
│       ├── request/
│       │   ├── OrderCreateRequest.java   # 创建订单请求参数(含明细列表)
│       │   ├── OrderListQuery.java       # 订单分页查询参数(关键词/状态/页码/每页大小)
│       │   ├── OrderUpdateRequest.java   # 更新订单请求参数，含乐观锁updatedAt
│       │   ├── OrderDeleteRequest.java   # 删除订单请求参数(乐观锁updatedAt)
│       │   └── OrderStatusRequest.java   # 修改订单状态请求参数
│       └── response/
│           ├── OrderListVO.java          # 订单列表响应VO
│           ├── OrderDetailVO.java        # 订单详情响应VO(含明细列表)
│           ├── OrderCreateVO.java        # 创建订单响应VO
│           ├── OrderUpdateVO.java        # 更新订单响应VO(返回最新updatedAt)
│           └── OrderStatusVO.java        # 修改状态响应VO(返回最新状态和时间)
```

## 代码文件索引

### controller/

- [OrderController](controller/OrderController.md) — REST控制器，提供订单CRUD + 状态管理的API端点

### service/

- [OrderService](service/OrderService.md) — 订单管理业务接口定义
- [OrderServiceImpl](service/impl/OrderServiceImpl.md) — 订单管理业务实现，含分页、乐观锁、状态流转、明细管理

### mapper/

- [OrderMapper](mapper/OrderMapper.md) — MyBatis Mapper接口，定义订单表及明细表数据库操作
- [OrderMapperXML](mapper/resources/mapper/OrderMapperXML.md) — MyBatis XML映射，含分页、关联查询、乐观锁SQL

### model/

#### entity

- [Order](model/entity/Order.md) — 订单实体，映射orders表全部字段
- [OrderItem](model/entity/OrderItem.md) — 订单明细实体，映射order_items表全部字段

#### enums

- [OrderStatus](model/enums/OrderStatus.md) — 订单状态枚举(pending/confirmed/shipped/completed/cancelled)

#### dto/request

- [OrderCreateRequest](model/dto/request/OrderCreateRequest.md) — 创建订单请求参数(含明细列表)
- [OrderListQuery](model/dto/request/OrderListQuery.md) — 订单分页查询参数(关键词、状态、页码、每页大小)
- [OrderUpdateRequest](model/dto/request/OrderUpdateRequest.md) — 更新订单请求参数，含乐观锁updatedAt
- [OrderDeleteRequest](model/dto/request/OrderDeleteRequest.md) — 删除订单请求参数(乐观锁updatedAt)
- [OrderStatusRequest](model/dto/request/OrderStatusRequest.md) — 修改订单状态请求参数

#### dto/response

- [OrderListVO](model/dto/response/OrderListVO.md) — 订单列表响应VO
- [OrderDetailVO](model/dto/response/OrderDetailVO.md) — 订单详情响应VO(含明细列表)
- [OrderCreateVO](model/dto/response/OrderCreateVO.md) — 创建订单响应VO
- [OrderUpdateVO](model/dto/response/OrderUpdateVO.md) — 更新订单响应VO(返回最新updatedAt)
- [OrderStatusVO](model/dto/response/OrderStatusVO.md) — 修改状态响应VO(返回最新状态和时间)
