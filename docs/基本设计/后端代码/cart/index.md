# 购物车管理 - 後端コード

## 包結構

```
com/example/cart/
├── controller/
│   └── CartController.java              # REST控制器，提供購物車CRUD + 結算API
├── service/
│   ├── CartService.java                 # 購物車管理業務接口定義
│   └── impl/
│       └── CartServiceImpl.java         # 購物車管理業務實現，含購物車/明細/結算邏輯
├── mapper/
│   ├── CartMapper.java                  # MyBatis Mapper接口，定義購物車表數據庫操作
│   └── resources/mapper/
│       └── CartMapper.xml               # MyBatis XML映射，含分頁/邏輯刪除/樂觀鎖SQL
└── model/
    ├── entity/
    │   ├── Cart.java                    # 購物車實體，映射carts表
    │   └── CartItem.java                # 購物車明細實體，映射cart_items表
    └── dto/
        ├── request/
        │   ├── CartAddItemRequest.java       # 添加商品請求參數
        │   ├── CartItemQuantityRequest.java  # 修改數量請求參數(樂觀鎖)
        │   ├── CartAddressRequest.java       # 選擇地址請求參數(樂觀鎖)
        │   ├── CartCheckoutRequest.java      # 結算請求參數(樂觀鎖)
        │   ├── CartItemDeleteRequest.java    # 刪除明細請求參數(樂觀鎖)
        │   └── CartDeleteRequest.java        # 清空購物車請求參數(樂觀鎖)
        └── response/
            ├── CartVO.java                  # 購物車完整響應(含明細+地址)
            ├── CartAddItemVO.java           # 添加商品響應VO
            ├── CartItemUpdateVO.java        # 更新數量響應VO(返回最新updatedAt)
            ├── CartAddressVO.java           # 選擇地址響應VO
            └── CartCheckoutVO.java          # 結算響應VO(返回訂單信息)
```

## コードファイル索引

### controller/

- [CartController](controller/CartController.md) — REST控制器，提供購物車CRUD + 結算API

### service/

- [CartService](service/CartService.md) — 購物車管理業務接口定義
- [CartServiceImpl](service/impl/CartServiceImpl.md) — 購物車管理業務實現，含購物車/明細/結算邏輯

### mapper/

- [CartMapper](mapper/CartMapper.md) — MyBatis Mapper接口，定義購物車表數據庫操作
- [CartMapperXML](mapper/resources/mapper/CartMapperXML.md) — MyBatis XML映射，含分頁、邏輯刪除、樂觀鎖SQL

### model/

#### entity

- [Cart](model/entity/Cart.md) — 購物車實體，映射carts表
- [CartItem](model/entity/CartItem.md) — 購物車明細實體，映射cart_items表

#### dto/request

- [CartAddItemRequest](model/dto/request/CartAddItemRequest.md) — 添加商品請求參數
- [CartItemQuantityRequest](model/dto/request/CartItemQuantityRequest.md) — 修改數量請求參數(樂觀鎖)
- [CartAddressRequest](model/dto/request/CartAddressRequest.md) — 選擇地址請求參數(樂觀鎖)
- [CartCheckoutRequest](model/dto/request/CartCheckoutRequest.md) — 結算請求參數(樂觀鎖)
- [CartItemDeleteRequest](model/dto/request/CartItemDeleteRequest.md) — 刪除明細請求參數(樂觀鎖)
- [CartDeleteRequest](model/dto/request/CartDeleteRequest.md) — 清空購物車請求參數(樂觀鎖)

#### dto/response

- [CartVO](model/dto/response/CartVO.md) — 購物車完整響應(含明細+地址)
- [CartAddItemVO](model/dto/response/CartAddItemVO.md) — 添加商品響應VO
- [CartItemUpdateVO](model/dto/response/CartItemUpdateVO.md) — 更新數量響應VO(返回最新updatedAt)
- [CartAddressVO](model/dto/response/CartAddressVO.md) — 選擇地址響應VO
- [CartCheckoutVO](model/dto/response/CartCheckoutVO.md) — 結算響應VO(返回訂單信息)
