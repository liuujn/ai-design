# 商品管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|商品列表|R|从商品表中查询商品信息|GET|/api/v1/products|
|商品详情|R|获取指定商品的详细信息|GET|/api/v1/products/{productId}|
|商品创建|C|创建新商品|POST|/api/v1/products|
|商品更新|U|更新指定商品信息|PUT|/api/v1/products/{productId}|
|商品删除|D|逻辑删除指定商品|DELETE|/api/v1/products/{productId}|
|商品状态变更|U|变更指定商品的上架/下架状态|PATCH|/api/v1/products/{productId}/status|

## 1. 商品列表API

商品列表API是从商品表中获取符合查询条件的商品信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/products
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|关键词|keyword|FALSE|String|-||LIKE（名称+描述）|
|商品分类|category|FALSE|String|-||完全匹配|
|商品状态|status|FALSE|String|-||完全匹配|
|页码|page|FALSE|Integer|-|1||
|每页条数|size|FALSE|Integer|-|20||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|商品列表|content|list|||
|商品列表|商品ID|id|String|||
|商品列表|商品名称|name|String|||
|商品列表|价格|price|BigDecimal|||
|商品列表|库存|stock|Integer|||
|商品列表|分类|category|String|||
|商品列表|商品状态|status|String||active/inactive|
|商品列表|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|商品列表|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|页码|page|Integer|||
|-|每页条数|size|Integer|||
|-|总条数|total|Integer|||
|-|总页数|totalPages|Integer|||

### 处理内容

1. 请求参数的验证
   - 执行请求参数的验证。
     - 商品状态：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为有效状态值|E9903|请输入有效的商品状态值。|

2. 从数据库获取数据
   - 目标表：`products`
   - 获取条件：
     - `is_deleted = 0`
     - 当参数中包含关键词时
       - `(name LIKE %:keyword% OR description LIKE %:keyword%)`
     - 当参数中包含分类时
       - `category = :category`
     - 当参数中包含状态时
       - `status = :status`
   - 排序条件：`created_at DESC`
   - 分页处理：根据 page 和 size 参数进行分页

3. 响应的生成
   - 处理成功时为正常系，返回分页后的商品列表及分页信息。
   - 获取件数为0件时为正常系，返回空列表。

## 2. 商品详情API

商品详情API是获取指定商品的详细信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/products/{productId}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|商品ID|productId|TRUE|String|-||路径参数|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|商品ID|id|String|||
|-|商品名称|name|String|||
|-|商品描述|description|String|||
|-|价格|price|BigDecimal|||
|-|库存|stock|Integer|||
|-|分类|category|String|||
|-|图片URL|imageUrl|String|||
|-|商品状态|status|String||active/inactive|
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 商品ID必填检查。

2. 从数据库获取数据
   - 目标表：`products`
   - 获取条件：`id = :productId` AND `is_deleted = 0`

3. 响应的生成
   - 商品不存在时返回错误消息 `E9404` 商品不存在。
   - 处理成功时为正常系，返回商品详细信息。

## 3. 商品创建API

商品创建API是创建新商品的端点。

### 请求

#### 请求URL

```text
POST /api/v1/products
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|商品名称|name|TRUE|String|-|||
|商品描述|description|FALSE|String|-|||
|价格|price|TRUE|BigDecimal|-||大于0|
|库存|stock|TRUE|Integer|-||大于等于0|
|分类|category|FALSE|String|-|||
|图片URL|imageUrl|FALSE|String|-|||
|商品状态|status|FALSE|String|-|active|active/inactive|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|商品ID|id|String|||
|-|商品名称|name|String|||
|-|价格|price|BigDecimal|||
|-|库存|stock|Integer|||
|-|商品状态|status|String|||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 商品名称：

     |检查内容|消息ID|消息|
     |:---|:---|:---|
     |必填检查|E9101|商品名称不能为空。|
     |字符数检查|E9102|商品名称长度应为1到200个字符。|

   - 价格：

     |检查内容|消息ID|消息|
     |:---|:---|:---|
     |必填检查|E9101|价格不能为空。|
     |范围检查|E9201|价格必须大于0。|

   - 库存：

     |检查内容|消息ID|消息|
     |:---|:---|:---|
     |必填检查|E9101|库存不能为空。|
     |范围检查|E9201|库存不能小于0。|

2. 业务处理
   - 生成商品ID（UUID）。
   - 设置创建者和更新者为当前操作者。
   - 向 `products` 表插入数据。

3. 响应的生成
   - 处理成功时为正常系，返回创建的商品信息。

## 4. 商品更新API

商品更新API是更新指定商品信息的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
PUT /api/v1/products/{productId}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|商品ID|productId|TRUE|String|-||路径参数|
|商品名称|name|TRUE|String|-|||
|商品描述|description|FALSE|String|-|||
|价格|price|TRUE|BigDecimal|-||大于0|
|库存|stock|TRUE|Integer|-||大于等于0|
|分类|category|FALSE|String|-|||
|图片URL|imageUrl|FALSE|String|-|||
|商品状态|status|FALSE|String|-||active/inactive|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|商品ID|id|String|||
|-|商品名称|name|String|||
|-|价格|price|BigDecimal|||
|-|库存|stock|Integer|||
|-|商品状态|status|String|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 同商品创建API的校验内容。
   - 乐观LOCK检查：从数据库获取当前 `updated_at`，与请求参数比较，不一致时返回 `E9409`。

2. 业务处理
   - 更新 `products` 表。
   - 条件：`id = :productId` AND `updated_at = :updatedAt` AND `is_deleted = 0`

3. 响应的生成
   - 处理成功时为正常系，返回更新后的商品信息。

## 5. 商品删除API

商品删除API是逻辑删除指定商品的端点。
使用乐观LOCK机制。

### 请求

#### 请求URL

```text
DELETE /api/v1/products/{productId}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|商品ID|productId|TRUE|String|-||路径参数|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 处理内容

1. 乐观LOCK检查（同商品更新API）。
2. 业务处理：逻辑删除 `products` 表：`is_deleted = 1`, `updated_at`, `updated_by`。
3. 响应的生成：返回删除成功。

## 6. 商品状态变更API

商品状态变更API是变更指定商品上架/下架状态的端点。
使用乐观LOCK机制。

### 请求

#### 请求URL

```text
PATCH /api/v1/products/{productId}/status
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|商品ID|productId|TRUE|String|-||路径参数|
|商品状态|status|TRUE|String|-||active/inactive|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|商品ID|id|String|||
|-|商品状态|status|String|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 商品ID必填检查。
   - 状态值有效性检查（active/inactive）。
   - 乐观LOCK检查。

2. 业务处理
   - 更新 `products` 表：`status`, `updated_at`, `updated_by`。
   - 条件：`id = :productId` AND `updated_at = :updatedAt` AND `is_deleted = 0`

3. 响应的生成
   - 处理成功时为正常系，返回更新后的状态和时间戳。
