# 商品分類管理 - 後端コード

## 包結構

```
com/example/category/
├── controller/
│   └── CategoryController.java        # REST控制器，提供分類CRUD + 全件取得API
├── service/
│   ├── CategoryService.java           # 分類管理業務接口定義
│   └── impl/
│       └── CategoryServiceImpl.java   # 分類管理業務實現，含分頁/樂觀鎖/狀態校驗
├── mapper/
│   ├── CategoryMapper.java            # MyBatis Mapper接口，定義分類表數據庫操作
│   └── resources/mapper/
│       └── CategoryMapper.xml         # MyBatis XML映射，含分頁/邏輯刪除/樂觀鎖SQL
└── model/
    ├── entity/
    │   └── Category.java              # 分類實體，映射categories表全部字段
    └── dto/
        ├── request/
        │   ├── CategoryCreateRequest.java  # 創建分類請求參數
        │   ├── CategoryListQuery.java      # 分類分頁查詢參數(關鍵詞/狀態/頁碼/每頁大小)
        │   ├── CategoryUpdateRequest.java  # 更新分類請求參數，含樂觀鎖updatedAt
        │   └── CategoryDeleteRequest.java  # 刪除分類請求參數(樂觀鎖updatedAt)
        └── response/
            ├── CategoryListVO.java         # 分類列表響應VO
            ├── CategoryDetailVO.java       # 分類詳情響應VO
            ├── CategoryCreateVO.java       # 創建分類響應VO
            ├── CategoryUpdateVO.java       # 更新分類響應VO(返回最新updatedAt)
            ├── CategoryStatusVO.java       # 修改狀態響應VO(返回最新狀態和時間)
            └── CategorySimpleVO.java       # 簡易分類VO(id+name)
```

## コードファイル索引

### controller/

- [CategoryController](controller/CategoryController.md) — REST控制器，提供分類CRUD + 全件取得API

### service/

- [CategoryService](service/CategoryService.md) — 分類管理業務接口定義
- [CategoryServiceImpl](service/impl/CategoryServiceImpl.md) — 分類管理業務實現，含分頁、樂觀鎖、狀態校驗

### mapper/

- [CategoryMapper](mapper/CategoryMapper.md) — MyBatis Mapper接口，定義分類表數據庫操作
- [CategoryMapperXML](mapper/resources/mapper/CategoryMapperXML.md) — MyBatis XML映射，含分頁、邏輯刪除、樂觀鎖SQL

### model/

#### entity

- [Category](model/entity/Category.md) — 分類實體，映射categories表全部字段

#### dto/request

- [CategoryCreateRequest](model/dto/request/CategoryCreateRequest.md) — 創建分類請求參數
- [CategoryListQuery](model/dto/request/CategoryListQuery.md) — 分類分頁查詢參數(關鍵詞、狀態、頁碼、每頁大小)
- [CategoryUpdateRequest](model/dto/request/CategoryUpdateRequest.md) — 更新分類請求參數，含樂觀鎖updatedAt
- [CategoryDeleteRequest](model/dto/request/CategoryDeleteRequest.md) — 刪除分類請求參數(樂觀鎖updatedAt)

#### dto/response

- [CategoryListVO](model/dto/response/CategoryListVO.md) — 分類列表響應VO
- [CategoryDetailVO](model/dto/response/CategoryDetailVO.md) — 分類詳情響應VO
- [CategoryCreateVO](model/dto/response/CategoryCreateVO.md) — 創建分類響應VO
- [CategoryUpdateVO](model/dto/response/CategoryUpdateVO.md) — 更新分類響應VO(返回最新updatedAt)
- [CategorySimpleVO](model/dto/response/CategorySimpleVO.md) — 簡易分類VO(id+name)
