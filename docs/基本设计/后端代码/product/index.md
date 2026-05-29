# 商品管理 - 後端コード

## 包結構

```
com/example/product/
├── controller/
│   └── ProductController.java        # REST控制器，提供商品CRUD + 狀態管理的API端點
├── service/
│   ├── ProductService.java           # 商品管理業務接口定義
│   └── impl/
│       └── ProductServiceImpl.java   # 商品管理業務實現，含分頁/樂觀鎖/狀態校驗
├── mapper/
│   ├── ProductMapper.java            # MyBatis Mapper接口，定義商品表數據庫操作
│   └── resources/mapper/
│       └── ProductMapper.xml         # MyBatis XML映射，含分頁/邏輯刪除/樂觀鎖SQL
├── model/
│   ├── entity/
│   │   └── Product.java              # 商品實體，映射products表全部字段
│   ├── enums/
│   │   └── ProductStatus.java        # 商品狀態枚舉(active/inactive)
│   └── dto/
│       ├── request/
│       │   ├── ProductCreateRequest.java  # 創建商品請求參數
│       │   ├── ProductListQuery.java      # 商品分頁查詢參數(關鍵詞/分類/狀態/頁碼/每頁大小)
│       │   ├── ProductUpdateRequest.java  # 更新商品請求參數，含樂觀鎖updatedAt
│       │   ├── ProductDeleteRequest.java  # 刪除商品請求參數(樂觀鎖updatedAt)
│       │   └── ProductStatusRequest.java  # 修改商品狀態請求參數
│       └── response/
│           ├── ProductListVO.java         # 商品列表響應VO
│           ├── ProductDetailVO.java       # 商品詳情響應VO
│           ├── ProductCreateVO.java       # 創建商品響應VO
│           ├── ProductUpdateVO.java       # 更新商品響應VO(返回最新updatedAt)
│           └── ProductStatusVO.java       # 修改狀態響應VO(返回最新狀態和時間)
```

## コードファイル索引

### controller/

- [ProductController](controller/ProductController.md) — REST控制器，提供商品CRUD + 狀態管理的API端點

### service/

- [ProductService](service/ProductService.md) — 商品管理業務接口定義
- [ProductServiceImpl](service/impl/ProductServiceImpl.md) — 商品管理業務實現，含分頁、樂觀鎖、狀態校驗

### mapper/

- [ProductMapper](mapper/ProductMapper.md) — MyBatis Mapper接口，定義商品表數據庫操作
- [ProductMapperXML](mapper/resources/mapper/ProductMapperXML.md) — MyBatis XML映射，含分頁、邏輯刪除、樂觀鎖SQL

### model/

#### entity

- [Product](model/entity/Product.md) — 商品實體，映射products表全部字段

#### enums

- [ProductStatus](model/enums/ProductStatus.md) — 商品狀態枚舉(active/inactive)

#### dto/request

- [ProductCreateRequest](model/dto/request/ProductCreateRequest.md) — 創建商品請求參數
- [ProductListQuery](model/dto/request/ProductListQuery.md) — 商品分頁查詢參數(關鍵詞、分類、狀態、頁碼、每頁大小)
- [ProductUpdateRequest](model/dto/request/ProductUpdateRequest.md) — 更新商品請求參數，含樂觀鎖updatedAt
- [ProductDeleteRequest](model/dto/request/ProductDeleteRequest.md) — 刪除商品請求參數(樂觀鎖updatedAt)
- [ProductStatusRequest](model/dto/request/ProductStatusRequest.md) — 修改商品狀態請求參數

#### dto/response

- [ProductListVO](model/dto/response/ProductListVO.md) — 商品列表響應VO
- [ProductDetailVO](model/dto/response/ProductDetailVO.md) — 商品詳情響應VO
- [ProductCreateVO](model/dto/response/ProductCreateVO.md) — 創建商品響應VO
- [ProductUpdateVO](model/dto/response/ProductUpdateVO.md) — 更新商品響應VO(返回最新updatedAt)
- [ProductStatusVO](model/dto/response/ProductStatusVO.md) — 修改狀態響應VO(返回最新狀態和時間)
