# 购物车管理API设计

## 模块概述

购物车管理模块提供用户的购物车操作功能，包括查看购物车、添加商品、修改数量、删除商品、选择地址和结算下单。每个用户只有一个活跃购物车。

## 关联模块

- **用户**：购物车归属于用户（user_id）
- **商品**：购物车明细关联商品（product_id）
- **地址**：购物车选择配送地址（address_id）
- **订单**：结算时生成订单（order）

## 基础路径

```
Base URL: http://localhost:9090/api/v1
```

## 通用说明

- 全API需要 `Authorization: Bearer <token>` 头
- 乐观锁：更新/删除需要 `updatedAt` 参数
- 响应格式统一为 JSON

## 终端点一览

| # | メソッド | URL | 説明 | 乐观锁 |
|---|---------|-----|------|--------|
| 1 | GET | /api/v1/carts/my | 获取当前用户的购物车（含明细） | - |
| 2 | POST | /api/v1/carts/items | 添加商品到购物车 | - |
| 3 | PUT | /api/v1/carts/items/{itemId} | 修改购物车明细数量 | YES |
| 4 | DELETE | /api/v1/carts/items/{itemId} | 删除购物车明细 | YES |
| 5 | PUT | /api/v1/carts/address | 选择配送地址 | YES |
| 6 | POST | /api/v1/carts/checkout | 结算（生成订单） | YES |
| 7 | DELETE | /api/v1/carts/my | 清空购物车 | YES |

---

## 1. 获取购物车

当前用户获取自己的活跃购物车（如果没有则新建空购物车）。

### 请求

```
GET /api/v1/carts/my
```

### 响应 200

```json
{
  "id": "cart-uuid",
  "userId": "user-uuid",
  "addressId": "addr-uuid",
  "status": "active",
  "totalAmount": 299.98,
  "remark": "",
  "items": [
    {
      "id": "item-uuid",
      "productId": "product-uuid",
      "productName": "无线蓝牙耳机",
      "quantity": 2,
      "unitPrice": 149.99,
      "subtotal": 299.98,
      "createdAt": "2026/05/28 10:30:00"
    }
  ],
  "address": {
    "id": "addr-uuid",
    "recipientName": "张三",
    "recipientPhone": "13800138000",
    "province": "广东省",
    "city": "深圳市",
    "district": "南山区",
    "street": "科技园路1号"
  },
  "createdAt": "2026/05/28 10:00:00",
  "updatedAt": "2026/05/28 10:30:00"
}
```

---

## 2. 添加商品到购物车

向当前用户的购物车添加商品。如果该商品已在购物车中，则增加数量。

### 请求

```
POST /api/v1/carts/items
```

### 请求体

```json
{
  "productId": "product-uuid",
  "quantity": 1
}
```

| 字段 | 型 | 必須 | 説明 |
|------|---|------|------|
| productId | String | YES | 商品ID |
| quantity | Integer | YES | 数量（≥1） |

### 响应 200

```json
{
  "id": "item-uuid",
  "productId": "product-uuid",
  "productName": "无线蓝牙耳机",
  "quantity": 1,
  "unitPrice": 149.99,
  "subtotal": 149.99
}
```

### 错误

| 状态码 | code | 说明 |
|--------|------|------|
| 400 | E9101 | 参数校验失败 |
| 404 | E9404 | 商品不存在 |

---

## 3. 修改购物车明细数量

### 请求

```
PUT /api/v1/carts/items/{itemId}
```

### 请求体

```json
{
  "quantity": 3,
  "updatedAt": "2026/05/28 10:30:00"
}
```

| 字段 | 型 | 必須 | 説明 |
|------|---|------|------|
| quantity | Integer | YES | 新数量（≥1） |
| updatedAt | String | YES | 乐观锁时间戳 |

### 响应 200

```json
{
  "id": "item-uuid",
  "productId": "product-uuid",
  "productName": "无线蓝牙耳机",
  "quantity": 3,
  "unitPrice": 149.99,
  "subtotal": 449.97,
  "updatedAt": "2026/05/28 10:35:00"
}
```

### 错误

| 状态码 | code | 说明 |
|--------|------|------|
| 400 | E9101 | 参数校验失败 |
| 404 | E9404 | 明细不存在 |
| 409 | E9409 | 数据已被其他用户修改 |

---

## 4. 删除购物车明细

### 请求

```
DELETE /api/v1/carts/items/{itemId}
```

### 请求体

```json
{
  "updatedAt": "2026/05/28 10:30:00"
}
```

### 响应 204

无内容。

### 错误

| 状态码 | code | 说明 |
|--------|------|------|
| 404 | E9404 | 明细不存在 |
| 409 | E9409 | 数据已被其他用户修改 |

---

## 5. 选择配送地址

### 请求

```
PUT /api/v1/carts/address
```

### 请求体

```json
{
  "addressId": "addr-uuid",
  "updatedAt": "2026/05/28 10:30:00"
}
```

| 字段 | 型 | 必須 | 説明 |
|------|---|------|------|
| addressId | String | YES | 地址ID |
| updatedAt | String | YES | 乐观锁时间戳 |

### 响应 200

```json
{
  "id": "cart-uuid",
  "addressId": "addr-uuid",
  "updatedAt": "2026/05/28 10:35:00"
}
```

---

## 6. 结算（生成订单）

将购物车转换为订单。购物车必须有商品和配送地址。

### 请求

```
POST /api/v1/carts/checkout
```

### 请求体

```json
{
  "remark": "请尽快发货",
  "updatedAt": "2026/05/28 10:30:00"
}
```

| 字段 | 型 | 必須 | 説明 |
|------|---|------|------|
| remark | String | NO | 订单备注 |
| updatedAt | String | YES | 乐观锁时间戳 |

### 响应 200

```json
{
  "orderId": "order-uuid",
  "orderNo": "202605280001",
  "totalAmount": 449.97,
  "status": "pending",
  "message": "下单成功"
}
```

### 错误

| 状态码 | code | 说明 |
|--------|------|------|
| 400 | E9201 | 购物车为空 |
| 400 | E9202 | 请先选择配送地址 |
| 409 | E9409 | 数据已被其他用户修改 |

---

## 7. 清空购物车

### 请求

```
DELETE /api/v1/carts/my
```

### 请求体

```json
{
  "updatedAt": "2026/05/28 10:30:00"
}
```

### 响应 204

### 错误

| 状态码 | code | 说明 |
|--------|------|------|
| 409 | E9409 | 数据已被其他用户修改 |

---

## 错误消息一览

| code | message |
|------|---------|
| E9101 | 参数校验失败：{詳細} |
| E9201 | 购物车为空，请先添加商品。 |
| E9202 | 请先选择配送地址。 |
| E9404 | 数据不存在。 |
| E9409 | 数据已被其他用户修改，请刷新后重试。 |
| E9501 | 库存不足：{商品名} 当前库存 {数量}。 |
