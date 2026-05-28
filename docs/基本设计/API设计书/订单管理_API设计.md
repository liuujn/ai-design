# 订单管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|订单列表|R|从订单主表中查询订单信息|GET|/api/v1/orders|
|订单详情|R|获取指定订单及明细信息|GET|/api/v1/orders/{orderId}|
|订单创建|C|创建新订单及明细|POST|/api/v1/orders|
|订单更新|U|更新指定订单及明细|PUT|/api/v1/orders/{orderId}|
|订单状态变更|U|变更指定订单状态|PATCH|/api/v1/orders/{orderId}/status|
|订单删除|D|逻辑删除指定订单|DELETE|/api/v1/orders/{orderId}|

## 1. 订单列表API

订单列表API是从订单主表中获取符合查询条件的订单信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/orders
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|订单编号|orderNo|FALSE|String|-||LIKE|
|客户姓名|customerName|FALSE|String|-||LIKE|
|订单状态|status|FALSE|String|-||完全匹配|
|开始日期|startDate|FALSE|Date|yyyy/MM/dd||订单创建日期 >=|
|结束日期|endDate|FALSE|Date|yyyy/MM/dd||订单创建日期 <=|
|页码|page|FALSE|Integer|-|1||
|每页条数|size|FALSE|Integer|-|20||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|订单列表|orders|list|||
|订单列表|订单ID|id|String|||
|订单列表|订单编号|orderNo|String|||
|订单列表|客户姓名|customerName|String|||
|订单列表|订单总金额|totalAmount|Decimal|12,2||
|订单列表|订单状态|status|String||pending/confirmed/shipped/completed/cancelled|
|订单列表|备注|remark|String|||
|订单列表|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|订单列表|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|页码|page|Integer|||
|-|每页条数|size|Integer|||
|-|总条数|total|Integer|||
|-|总页数|totalPages|Integer|||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 订单编号：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为允许的字符|E9916|请输入不包含禁止字符的订单编号。|

     - 客户姓名：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为允许的字符|E9916|请输入不包含禁止字符的客户姓名。|

     - 订单状态：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为有效状态值|E9903|请输入有效的订单状态值。|

     - 开始日期/结束日期：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为有效日期(yyyy/MM/dd)|E9906|请输入有效日期。|

   - 1.2 当 startDate 和 endDate 都指定时，校验 startDate <= endDate。

2. 从数据库获取数据
   - 目标表：`orders`
   - 获取项目：
     - `id`, `order_no`, `customer_name`, `total_amount`, `status`, `remark`, `created_at`, `updated_at`
   - 获取条件：
     - 以下条件使用AND连接进行查询
     - `is_deleted = false`
     - 当参数中包含订单编号且输入了值时
       - `order_no LIKE %参数的订单编号%`
     - 当参数中包含客户姓名且输入了值时
       - `customer_name LIKE %参数的客户姓名%`
     - 当参数中包含订单状态且输入了值时
       - `status = 参数的订单状态`
     - 当参数中包含开始日期且输入了值时
       - `created_at >= 参数的开始日期`
     - 当参数中包含结束日期且输入了值时
       - `created_at <= 参数的结束日期 23:59:59`
   - 排序条件：
     - `created_at` 降序
   - 分页处理：
     - 根据 page 和 size 参数进行分页

3. 响应的生成
   - 处理成功时为正常系，返回分页后的订单列表及分页信息。
   - 获取件数为0件时为正常系，返回空列表。
   - 处理失败时为异常系，返回错误消息。

## 2. 订单详情API

订单详情API是获取指定订单及其明细信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/orders/{orderId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|订单ID|orderId|TRUE|String|-||路径参数|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|订单ID|id|String|||
|-|订单编号|orderNo|String|||
|-|客户姓名|customerName|String|||
|-|订单总金额|totalAmount|Decimal|12,2||
|-|订单状态|status|String|||
|-|备注|remark|String|||
|-|明细列表|items|list|||
|明细列表|明细ID|id|String|||
|明细列表|商品名称|itemName|String|||
|明细列表|数量|quantity|Integer|||
|明细列表|单价|unitPrice|Decimal|10,2||
|明细列表|小计|subtotal|Decimal|12,2||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 订单ID：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|订单ID不能为空。|

2. 从数据库获取数据
   - 订单主表：
     - 目标表：`orders`
     - 获取条件：
       - `id = :orderId`
       - `is_deleted = false`
   - 订单明细表：
     - 目标表：`order_items`
     - 获取条件：
       - `order_id = :orderId`
       - `is_deleted = false`
     - 排序条件：
       - `created_at` 升序

3. 响应的生成
   - 订单不存在时返回错误消息 `E9404` 订单不存在。
   - 处理成功时为正常系，返回订单信息及明细列表。
   - 处理失败时为异常系，返回错误消息。

## 3. 订单创建API

订单创建API是创建新订单及其明细的端点。

### 请求

#### 请求URL

```text
POST /api/v1/orders
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|客户姓名|customerName|TRUE|String|-|||
|备注|remark|FALSE|String|-|||
|明细列表|items|TRUE|list|||至少1条|
|明细-商品名称|items[].itemName|TRUE|String|-|||
|明细-数量|items[].quantity|TRUE|Integer|-||最小值1|
|明细-单价|items[].unitPrice|TRUE|Decimal|10,2||最小值0|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|订单ID|id|String|||
|-|订单编号|orderNo|String|||
|-|客户姓名|customerName|String|||
|-|订单总金额|totalAmount|Decimal|12,2||
|-|订单状态|status|String|||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 客户姓名：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|客户姓名不能为空。|
       |字符数检查|E9102|客户姓名长度应为1到100个字符。|

     - 明细列表：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|订单明细不能为空。|
       |最少条数检查|E9904|订单明细至少需要1条。|

       - 明细-商品名称：

         |检查内容|消息ID|消息|
         |:---|:---|:---|
         |必填检查|E9101|商品名称不能为空。|
         |字符数检查|E9102|商品名称长度应为1到200个字符。|

       - 明细-数量：

         |检查内容|消息ID|消息|
         |:---|:---|:---|
         |必填检查|E9101|数量不能为空。|
         |范围检查|E9905|数量必须大于等于1。|

       - 明细-单价：

         |检查内容|消息ID|消息|
         |:---|:---|:---|
         |必填检查|E9101|单价不能为空。|
         |范围检查|E9905|单价必须大于等于0。|

2. 业务处理
   - 生成订单ID（UUID）。
   - 生成订单编号（规则：ORD + yyyyMMdd + 6位序列号）。
   - 计算各明细小计：`subtotal = quantity * unit_price`。
   - 计算订单总金额：`totalAmount = sum(subtotal)`。
   - 订单状态初始值：`pending`。
   - 向 `orders` 表插入订单主数据。
   - 向 `order_items` 表批量插入明细数据。
   - 使用事务确保订单主表和明细表数据一致性。

3. 响应的生成
   - 处理成功时为正常系，返回创建的订单信息。
   - 处理失败时为异常系，返回错误消息，事务回滚。

## 4. 订单更新API

订单更新API是更新指定订单及其明细的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
PUT /api/v1/orders/{orderId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|订单ID|orderId|TRUE|String|-||路径参数|
|客户姓名|customerName|TRUE|String|-|||
|备注|remark|FALSE|String|-|||
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|
|明细列表|items|TRUE|list|||至少1条|
|明细-商品名称|items[].itemName|TRUE|String|-|||
|明细-数量|items[].quantity|TRUE|Integer|-||最小值1|
|明细-单价|items[].unitPrice|TRUE|Decimal|10,2||最小值0|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|订单ID|id|String|||
|-|订单编号|orderNo|String|||
|-|客户姓名|customerName|String|||
|-|订单总金额|totalAmount|Decimal|12,2||
|-|订单状态|status|String|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。（同订单创建API）
   - 1.2 乐观LOCK检查。
     - 从数据库获取当前 `updated_at`。
     - 与请求参数中的 `updatedAt` 比较。
     - 不一致时返回 `E9409` 数据已被其他用户修改，请刷新后重试。

2. 业务处理
   - 更新 `orders` 表：
     - 更新项目：`customer_name`, `remark`, `updated_at`, `updated_by`
     - 条件：`id = :orderId` AND `updated_at = :updatedAt`
   - 删除原有明细：逻辑删除 `order_items` 中原有数据（`is_deleted = true`）。
   - 重新插入明细数据到 `order_items`。
   - 重新计算总金额并更新。
   - 使用事务确保数据一致性。

3. 响应的生成
   - 处理成功时为正常系，返回更新后的订单信息。
   - 处理失败时为异常系，返回错误消息，事务回滚。

## 5. 订单状态变更API

订单状态变更API是变更指定订单状态的端点。

### 请求

#### 请求URL

```text
PATCH /api/v1/orders/{orderId}/status
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|订单ID|orderId|TRUE|String|-||路径参数|
|订单状态|status|TRUE|String|-||pending/confirmed/shipped/completed/cancelled|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|订单ID|id|String|||
|-|订单编号|orderNo|String|||
|-|订单状态|status|String|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 订单状态：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|订单状态不能为空。|
       |为有效状态值|E9903|请输入有效的订单状态值。|

   - 1.2 状态流转校验：
     - pending → confirmed/shipped/cancelled
     - confirmed → shipped/cancelled
     - shipped → completed
     - completed/cancelled → 不可变更
     - 无效状态流转时返回 `E9907` 当前订单状态不允许变更为目标状态。

   - 1.3 乐观LOCK检查。（同订单更新API）

2. 业务处理
   - 更新 `orders` 表：
     - 更新项目：`status`, `updated_at`, `updated_by`
     - 条件：`id = :orderId` AND `updated_at = :updatedAt`

3. 响应的生成
   - 处理成功时为正常系，返回更新后的订单状态。
   - 处理失败时为异常系，返回错误消息。

## 6. 订单删除API

订单删除API是逻辑删除指定订单的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
DELETE /api/v1/orders/{orderId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|订单ID|orderId|TRUE|String|-||路径参数|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|信息|message|String||处理结果消息|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 订单ID：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|订单ID不能为空。|

   - 1.2 乐观LOCK检查。（同订单更新API）

2. 业务处理
   - 逻辑删除 `orders` 表：`is_deleted = true`, `updated_at`, `updated_by`。
   - 逻辑删除关联的 `order_items`：`is_deleted = true`。
   - 使用事务确保数据一致性。

3. 响应的生成
   - 处理成功时为正常系，返回删除成功消息。
   - 处理失败时为异常系，返回错误消息，事务回滚。
