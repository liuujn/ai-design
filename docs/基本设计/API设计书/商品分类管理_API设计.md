# 商品分类管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|分类列表|R|查询分类列表|GET|/api/v1/categories|
|分类详情|R|获取指定分类的详细信息|GET|/api/v1/categories/{id}|
|分类创建|C|创建新分类|POST|/api/v1/categories|
|分类更新|U|更新指定分类信息|PUT|/api/v1/categories/{id}|
|分类删除|D|逻辑删除指定分类|DELETE|/api/v1/categories/{id}|

## 1. 分类列表API

分类列表API是从分类表中获取符合查询条件的分类信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/categories
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|关键词|keyword|FALSE|String|-||LIKE（名称+描述）|
|分类状态|status|FALSE|String|-||完全匹配|
|页码|page|FALSE|Integer|-|1||
|每页条数|size|FALSE|Integer|-|20||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|分类列表|content|list|||
|分类列表|分类ID|id|String|||
|分类列表|分类名称|name|String|||
|分类列表|分类描述|description|String|||
|分类列表|排序序号|sortOrder|Integer|||
|分类列表|分类状态|status|String||active/inactive|
|分类列表|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|分类列表|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|页码|page|Integer|||
|-|每页条数|size|Integer|||
|-|总条数|total|Integer|||
|-|总页数|totalPages|Integer|||

### 处理内容

1. 从数据库获取数据
   - 目标表：`categories`
   - 获取条件：
     - `is_deleted = 0`
     - 当参数中包含关键词时：`(name LIKE %:keyword% OR description LIKE %:keyword%)`
     - 当参数中包含状态时：`status = :status`
   - 排序条件：`sort_order ASC, created_at DESC`

2. 响应的生成
   - 成功时返回分页后的分类列表。
   - 获取件数为0时返回空列表。

## 2. 分类详情API

分类详情API是获取指定分类的详细信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/categories/{id}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|分类ID|id|TRUE|String|-||路径参数|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|分类ID|id|String|||
|-|分类名称|name|String|||
|-|分类描述|description|String|||
|-|排序序号|sortOrder|Integer|||
|-|分类状态|status|String||active/inactive|
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 从数据库获取数据
   - 目标表：`categories`
   - 获取条件：`id = :id` AND `is_deleted = 0`

2. 响应的生成
   - 分类不存在时返回错误消息 E9404。
   - 成功时返回分类详细信息。

## 3. 分类创建API

分类创建API是创建新分类的端点。

### 请求

#### 请求URL

```text
POST /api/v1/categories
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|分类名称|name|TRUE|String|-|||
|分类描述|description|FALSE|String|-|||
|排序序号|sortOrder|FALSE|Integer|-|0||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|分类ID|id|String|||
|-|分类名称|name|String|||
|-|分类描述|description|String|||
|-|排序序号|sortOrder|Integer|||
|-|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 分类名称：必填检查，字符数检查（1~100字符）。
   - 排序序号：默认0。

2. 业务处理
   - 生成分类ID（UUID）。
   - 向 `categories` 表插入数据。

3. 响应的生成
   - 成功时返回创建的分类信息。

## 4. 分类更新API

分类更新API是更新指定分类信息的端点。使用乐观LOCK机制。

### 请求

#### 请求URL

```text
PUT /api/v1/categories/{id}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|分类名称|name|TRUE|String|-|||
|分类描述|description|FALSE|String|-|||
|排序序号|sortOrder|FALSE|Integer|-|0||
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|分类ID|id|String|||
|-|分类名称|name|String|||
|-|更新后状态|status|String|||
|-|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

### 处理内容

1. 请求参数的验证
   - 分类名称：必填检查，字符数检查。
   - 乐观LOCK检查：对比 `updated_at`。

2. 业务处理
   - 更新 `categories` 表。
   - 条件：`id = :id` AND `updated_at = :updatedAt` AND `is_deleted = 0`

3. 响应的生成
   - 成功时返回更新后的分类信息。

## 5. 分类删除API

分类删除API是逻辑删除指定分类的端点。使用乐观LOCK机制。

### 请求

#### 请求URL

```text
DELETE /api/v1/categories/{id}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|更新时间|updatedAt|TRUE|Timestamp|yyyy/MM/dd HH:mm:ss||乐观LOCK用|

### 处理内容

1. 乐观LOCK检查。
2. 业务处理：逻辑删除 `categories` 表。
3. 响应的生成：返回204 No Content。
