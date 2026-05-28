# 地址管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|地址列表|R|从地址表中查询当前用户地址信息|GET|/api/v1/addresses|
|地址详情|R|获取指定地址的详细信息|GET|/api/v1/addresses/{addressId}|
|地址创建|C|创建新地址|POST|/api/v1/addresses|
|地址更新|U|更新指定地址信息|PUT|/api/v1/addresses/{addressId}|
|地址删除|D|逻辑删除指定地址|DELETE|/api/v1/addresses/{addressId}|
|设置默认地址|U|将指定地址设为当前用户的默认地址|PATCH|/api/v1/addresses/{addressId}/default|

## 1. 地址列表API

地址列表API是从地址表中获取当前登录用户符合查询条件的地址信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/addresses
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户ID|userId|FALSE|String|-||默认当前用户|
|地址类型|addressType|FALSE|String|-||完全匹配|
|收件人姓名|recipientName|FALSE|String|-||LIKE|
|页码|page|FALSE|Integer|-|1||
|每页条数|size|FALSE|Integer|-|20||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|地址列表|addresses|list|||
|地址列表|地址ID|id|String|||
|地址列表|所属用户ID|userId|String|||
|地址列表|地址类型|addressType|String||shipping/billing|
|地址列表|收件人姓名|recipientName|String|||
|地址列表|收件人电话|recipientPhone|String|||
|地址列表|完整地址|fullAddress|String||省市区+详细地址拼接|
|地址列表|是否默认|isDefault|Boolean|||
|地址列表|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|地址列表|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|页码|page|Integer|||
|-|每页条数|size|Integer|||
|-|总条数|total|Integer|||
|-|总页数|totalPages|Integer|||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 地址类型：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为有效状态值|E9903|请输入有效的地址类型值。|

     - 收件人姓名：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |为允许的字符|E9916|请输入不包含禁止字符的收件人姓名。|

2. 从数据库获取数据
   - 目标表：`addresses`
   - 获取项目：
     - `id`, `user_id`, `address_type`, `recipient_name`, `recipient_phone`, `country`, `province`, `city`, `district`, `street`, `is_default`, `created_at`, `updated_at`
   - 获取条件：
     - 以下条件使用AND连接进行查询
     - `is_deleted = false`
     - 当参数中包含用户ID且输入了值时
       - `user_id = :userId`
     - 未指定userId时默认使用当前登录用户ID
     - 当参数中包含地址类型且输入了值时
       - `address_type = :addressType`
     - 当参数中包含收件人姓名且输入了值时
       - `recipient_name LIKE %:recipientName%`
   - 排序条件：
     - `is_default` 降序（默认地址优先）
     - `created_at` 降序
   - 分页处理：
     - 根据 page 和 size 参数进行分页

3. 响应的生成
   - 处理成功时为正常系，返回分页后的地址列表及分页信息。
   - 获取件数为0件时为正常系，返回空列表。
   - 处理失败时为异常系，返回错误消息。

## 2. 地址详情API

地址详情API是获取指定地址的详细信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/addresses/{addressId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|地址ID|addressId|TRUE|String|-||路径参数|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|地址ID|id|String|||
|-|所属用户ID|userId|String|||
|-|地址类型|addressType|String||shipping/billing|
|-|收件人姓名|recipientName|String|||
|-|收件人电话|recipientPhone|String|||
|-|国家|country|String|||
|-|省/州|province|String|||
|-|城市|city|String|||
|-|区/县|district|String|||
|-|详细地址|street|String|||
|-|邮政编码|postalCode|String|||
|-|是否默认|isDefault|Boolean|||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 地址ID：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|地址ID不能为空。|

2. 从数据库获取数据
   - 目标表：`addresses`
   - 获取条件：
     - `id = :addressId`
     - `is_deleted = false`

3. 响应的生成
   - 地址不存在时返回错误消息 `E9404` 地址不存在。
   - 处理成功时为正常系，返回地址详细信息。
   - 处理失败时为异常系，返回错误消息。

## 3. 地址创建API

地址创建API是创建新地址的端点。

### 请求

#### 请求URL

```text
POST /api/v1/addresses
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|地址类型|addressType|TRUE|String|-|shipping||
|收件人姓名|recipientName|TRUE|String|-|||
|收件人电话|recipientPhone|TRUE|String|-||电话格式校验|
|国家|country|TRUE|String|-|中国||
|省/州|province|TRUE|String|-|||
|城市|city|TRUE|String|-|||
|区/县|district|FALSE|String|-|||
|详细地址|street|TRUE|String|-|||
|邮政编码|postalCode|FALSE|String|-||邮政编码格式|
|是否默认|isDefault|FALSE|Boolean|true/false|false||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|地址ID|id|String|||
|-|地址类型|addressType|String|||
|-|收件人姓名|recipientName|String|||
|-|收件人电话|recipientPhone|String|||
|-|是否默认|isDefault|Boolean|||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 收件人姓名：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|收件人姓名不能为空。|
       |字符数检查|E9102|收件人姓名长度应为1到100个字符。|

     - 收件人电话：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|收件人电话不能为空。|
       |格式检查|E9203|收件人电话格式不正确。|

     - 省/州：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|省/州不能为空。|

     - 城市：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|城市不能为空。|

     - 详细地址：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|详细地址不能为空。|
       |字符数检查|E9102|详细地址长度应为1到200个字符。|

     - 邮政编码（当输入时）：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |格式检查|E9204|邮政编码格式不正确。|

2. 业务处理
   - 生成地址ID（UUID）。
   - 获取当前登录用户ID，设置 `user_id`。
   - 若 `isDefault = true`，将该用户其他地址的 `is_default` 设为 false。
   - 校验该用户地址数量不超过20条。
   - 向 `addresses` 表插入地址数据。

3. 响应的生成
   - 处理成功时为正常系，返回创建的地址信息。
   - 处理失败时为异常系，返回错误消息，事务回滚。

## 4. 地址更新API

地址更新API是更新指定地址信息的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
PUT /api/v1/addresses/{addressId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|地址ID|addressId|TRUE|String|-||路径参数|
|地址类型|addressType|TRUE|String|-|||
|收件人姓名|recipientName|TRUE|String|-|||
|收件人电话|recipientPhone|TRUE|String|-||电话格式校验|
|国家|country|TRUE|String|-|||
|省/州|province|TRUE|String|-|||
|城市|city|TRUE|String|-|||
|区/县|district|FALSE|String|-|||
|详细地址|street|TRUE|String|-|||
|邮政编码|postalCode|FALSE|String|-|||
|是否默认|isDefault|FALSE|Boolean|true/false|||
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|地址ID|id|String|||
|-|地址类型|addressType|String|||
|-|收件人姓名|recipientName|String|||
|-|收件人电话|recipientPhone|String|||
|-|是否默认|isDefault|Boolean|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。（同地址创建API）
   - 1.2 乐观LOCK检查。
     - 从数据库获取当前 `updated_at`。
     - 与请求参数中的 `updatedAt` 比较。
     - 不一致时返回 `E9409` 数据已被其他用户修改，请刷新后重试。

2. 业务处理
   - 更新 `addresses` 表：
     - 更新项目：`address_type`, `recipient_name`, `recipient_phone`, `country`, `province`, `city`, `district`, `street`, `postal_code`, `is_default`, `updated_at`, `updated_by`
     - 条件：`id = :addressId` AND `updated_at = :updatedAt` AND `is_deleted = false`
   - 若 `isDefault = true`，将该用户其他地址的 `is_default` 设为 false。

3. 响应的生成
   - 处理成功时为正常系，返回更新后的地址信息。
   - 处理失败时为异常系，返回错误消息，事务回滚。

## 5. 地址删除API

地址删除API是逻辑删除指定地址的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
DELETE /api/v1/addresses/{addressId}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|地址ID|addressId|TRUE|String|-||路径参数|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|信息|message|String||处理结果消息|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 地址ID：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|地址ID不能为空。|

   - 1.2 乐观LOCK检查。（同地址更新API）

2. 业务处理
   - 逻辑删除 `addresses` 表：`is_deleted = true`, `updated_at`, `updated_by`。
   - 若删除的是默认地址，无需自动设置新的默认地址。

3. 响应的生成
   - 处理成功时为正常系，返回删除成功消息。
   - 处理失败时为异常系，返回错误消息，事务回滚。

## 6. 设置默认地址API

设置默认地址API是将指定地址设为当前用户默认地址的端点。
使用乐观LOCK机制，根据更新时间戳进行冲突检测。

### 请求

#### 请求URL

```text
PATCH /api/v1/addresses/{addressId}/default
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|地址ID|addressId|TRUE|String|-||路径参数|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|地址ID|id|String|||
|-|是否默认|isDefault|Boolean|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|信息|message|String||仅在出错时返回|

- 共通项目按共通设置处理，此处省略

### 处理内容

1. 请求参数的验证
   - 1.1 执行请求参数的验证。
     - 地址ID：

       |检查内容|消息ID|消息|
       |:---|:---|:---|
       |必填检查|E9101|地址ID不能为空。|

   - 1.2 乐观LOCK检查。（同地址更新API）

2. 业务处理
   - 将该用户所有地址的 `is_default` 设为 false。
   - 将目标地址的 `is_default` 设为 true。
   - 更新 `updated_at`, `updated_by`。
   - 条件：`id = :addressId` AND `updated_at = :updatedAt` AND `is_deleted = false`
   - 使用事务确保数据一致性。

3. 响应的生成
   - 处理成功时为正常系，返回更新后的默认地址信息。
   - 处理失败时为异常系，返回错误消息，事务回滚。
