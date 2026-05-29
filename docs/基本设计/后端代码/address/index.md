# 地址管理 - 後端コード

## 包結構

```
com/example/address/
├── controller/
│   └── AddressController.java        # REST控制器，提供地址CRUD + 預設地址設定API
├── service/
│   ├── AddressService.java           # 地址管理業務接口定義
│   └── impl/
│       └── AddressServiceImpl.java   # 地址管理業務實現，含分頁/樂觀鎖/預設地址
├── mapper/
│   ├── AddressMapper.java            # MyBatis Mapper接口，定義地址表數據庫操作
│   └── resources/mapper/
│       └── AddressMapper.xml         # MyBatis XML映射，含分頁/邏輯刪除/樂觀鎖SQL
└── model/
    ├── entity/
    │   └── Address.java              # 地址實體，映射addresses表全部字段
    ├── enums/
    │   └── AddressType.java          # 地址類型枚舉(shipping/billing)
    └── dto/
        ├── request/
        │   ├── AddressCreateRequest.java  # 創建地址請求參數
        │   ├── AddressUpdateRequest.java  # 更新地址請求參數，含樂觀鎖updatedAt
        │   ├── AddressDeleteRequest.java  # 刪除地址請求參數(樂觀鎖updatedAt)
        │   ├── AddressListQuery.java      # 地址分頁查詢參數(用戶ID/頁碼/每頁大小)
        │   └── AddressDefaultRequest.java # 設定默認地址請求參數
        └── response/
            ├── AddressListVO.java         # 地址列表響應VO
            ├── AddressDetailVO.java       # 地址詳情響應VO
            ├── AddressCreateVO.java       # 創建地址響應VO
            ├── AddressUpdateVO.java       # 更新地址響應VO(返回最新updatedAt)
            └── AddressDefaultVO.java      # 設定默認地址響應VO
```

## コードファイル索引

### controller/

- [AddressController](controller/AddressController.md) — REST控制器，提供地址CRUD + 預設地址設定API

### service/

- [AddressService](service/AddressService.md) — 地址管理業務接口定義
- [AddressServiceImpl](service/impl/AddressServiceImpl.md) — 地址管理業務實現，含分頁、樂觀鎖、預設地址

### mapper/

- [AddressMapper](mapper/AddressMapper.md) — MyBatis Mapper接口，定義地址表數據庫操作
- [AddressMapperXML](mapper/resources/mapper/AddressMapperXML.md) — MyBatis XML映射，含分頁、邏輯刪除、樂觀鎖SQL

### model/

#### entity

- [Address](model/entity/Address.md) — 地址實體，映射addresses表全部字段

#### enums

- [AddressType](model/enums/AddressType.md) — 地址類型枚舉(shipping/billing)

#### dto/request

- [AddressCreateRequest](model/dto/request/AddressCreateRequest.md) — 創建地址請求參數
- [AddressUpdateRequest](model/dto/request/AddressUpdateRequest.md) — 更新地址請求參數，含樂觀鎖updatedAt
- [AddressDeleteRequest](model/dto/request/AddressDeleteRequest.md) — 刪除地址請求參數(樂觀鎖updatedAt)
- [AddressListQuery](model/dto/request/AddressListQuery.md) — 地址分頁查詢參數(用戶ID/頁碼/每頁大小)
- [AddressDefaultRequest](model/dto/request/AddressDefaultRequest.md) — 設定默認地址請求參數

#### dto/response

- [AddressListVO](model/dto/response/AddressListVO.md) — 地址列表響應VO
- [AddressDetailVO](model/dto/response/AddressDetailVO.md) — 地址詳情響應VO
- [AddressCreateVO](model/dto/response/AddressCreateVO.md) — 創建地址響應VO
- [AddressUpdateVO](model/dto/response/AddressUpdateVO.md) — 更新地址響應VO(返回最新updatedAt)
- [AddressDefaultVO](model/dto/response/AddressDefaultVO.md) — 設定默認地址響應VO
