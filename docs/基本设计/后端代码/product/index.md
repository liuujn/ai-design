# 商品管理 - 后端代码

## 包结构

```
com/example/product/
├── controller/
│   └── ProductController.java        # REST控制器，提供商品CRUD + 状态管理的API端点
├── service/
│   ├── ProductService.java           # 商品管理业务接口定义
│   └── impl/
│       └── ProductServiceImpl.java   # 商品管理业务实现，含分页/乐观锁/状态校验
├── mapper/
│   ├── ProductMapper.java            # MyBatis Mapper接口，定义商品表数据库操作
│   └── resources/mapper/
│       └── ProductMapper.xml         # MyBatis XML映射，含分页/逻辑删除/乐观锁SQL
├── model/
│   ├── entity/
│   │   └── Product.java              # 商品实体，映射products表全部字段
│   ├── enums/
│   │   └── ProductStatus.java        # 商品状态枚举(active/inactive)
│   └── dto/
│       ├── request/
│       │   ├── ProductCreateRequest.java  # 创建商品请求参数
│       │   ├── ProductListQuery.java      # 商品分页查询参数(关键词/分类/状态/页码/每页大小)
│       │   ├── ProductUpdateRequest.java  # 更新商品请求参数，含乐观锁updatedAt
│       │   ├── ProductDeleteRequest.java  # 删除商品请求参数(乐观锁updatedAt)
│       │   └── ProductStatusRequest.java  # 修改商品状态请求参数
│       └── response/
│           ├── ProductListVO.java         # 商品列表响应VO
│           ├── ProductDetailVO.java       # 商品详情响应VO
│           ├── ProductCreateVO.java       # 创建商品响应VO
│           ├── ProductUpdateVO.java       # 更新商品响应VO(返回最新updatedAt)
│           └── ProductStatusVO.java       # 修改状态响应VO(返回最新状态和时间)
```

## 代码文件索引

### controller/

- [ProductController](controller/ProductController.md) — REST控制器，提供商品CRUD + 状态管理的API端点

### service/

- [ProductService](service/ProductService.md) — 商品管理业务接口定义
- [ProductServiceImpl](service/impl/ProductServiceImpl.md) — 商品管理业务实现，含分页、乐观锁、状态校验

### mapper/

- [ProductMapper](mapper/ProductMapper.md) — MyBatis Mapper接口，定义商品表数据库操作
- [ProductMapperXML](mapper/resources/mapper/ProductMapperXML.md) — MyBatis XML映射，含分页、逻辑删除、乐观锁SQL

### model/

#### entity

- [Product](model/entity/Product.md) — 商品实体，映射products表全部字段

#### enums

- [ProductStatus](model/enums/ProductStatus.md) — 商品状态枚举(active/inactive)

#### dto/request

- [ProductCreateRequest](model/dto/request/ProductCreateRequest.md) — 创建商品请求参数
- [ProductListQuery](model/dto/request/ProductListQuery.md) — 商品分页查询参数(关键词、分类、状态、页码、每页大小)
- [ProductUpdateRequest](model/dto/request/ProductUpdateRequest.md) — 更新商品请求参数，含乐观锁updatedAt
- [ProductDeleteRequest](model/dto/request/ProductDeleteRequest.md) — 删除商品请求参数(乐观锁updatedAt)
- [ProductStatusRequest](model/dto/request/ProductStatusRequest.md) — 修改商品状态请求参数

#### dto/response

- [ProductListVO](model/dto/response/ProductListVO.md) — 商品列表响应VO
- [ProductDetailVO](model/dto/response/ProductDetailVO.md) — 商品详情响应VO
- [ProductCreateVO](model/dto/response/ProductCreateVO.md) — 创建商品响应VO
- [ProductUpdateVO](model/dto/response/ProductUpdateVO.md) — 更新商品响应VO(返回最新updatedAt)
- [ProductStatusVO](model/dto/response/ProductStatusVO.md) — 修改状态响应VO(返回最新状态和时间)
