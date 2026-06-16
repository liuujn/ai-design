# 菜单管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|侧边栏菜单|R|获取所有激活的菜单(排序后)|GET|/api/v1/menus/sidebar|
|菜单列表|R|查询菜单列表|GET|/api/v1/menus|
|菜单详情|R|获取指定菜单的详细信息|GET|/api/v1/menus/{id}|
|菜单创建|C|创建新菜单|POST|/api/v1/menus|
|菜单更新|U|更新指定菜单信息|PUT|/api/v1/menus/{id}|
|菜单删除|D|逻辑删除指定菜单|DELETE|/api/v1/menus/{id}|
|菜单状态变更|U|启用/停用菜单|PATCH|/api/v1/menus/{id}/status|

## 1. 侧边栏菜单API

侧边栏菜单API是获取所有激活菜单（按排序号排列）的端点，供主页侧边栏动态加载。

### 请求

#### 请求URL

```text
GET /api/v1/menus/sidebar
```

### 请求参数

无

### 响应体

|項目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|
|菜单ID|id|String|||
|父菜单ID|parentId|String|||
|菜单名称|label|String|||
|图标|icon|String||SVG代码|
|页面标题|pageTitle|String|||
|页面路径|pageSrc|String|||
|排序序号|sortOrder|Integer|||
|分隔线标识|isDivider|Boolean||true=分隔线|
|状态|status|String||active/inactive|

### 处理内容

1. 从数据库获取数据
   - 目标表：`sys_menus`
   - 获取条件：
     - `is_deleted = 0`
     - `status = 'active'`
   - 排序条件：`sort_order ASC, created_at ASC`

2. 响应的生成
   - 成功时返回菜单数组。
   - 无菜单时返回空数组。

## 2. 菜单列表API

菜单列表API是从菜单表中获取符合查询条件的菜单信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/menus
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|初期値|条件|
|:---|:---|:---|---|:---|:---|:---|
|关键词|keyword|FALSE|String|-||LIKE（名称）|
|状态|status|FALSE|String|-||完全匹配|
|页码|page|FALSE|Integer|-|1||
|每页条数|size|FALSE|Integer|-|20||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|菜单列表|content|list|||
|菜单列表|菜单ID|id|String|||
|菜单列表|菜单名称|label|String|||
|菜单列表|图标|icon|String||SVG代码|
|菜单列表|页面标题|pageTitle|String|||
|菜单列表|页面路径|pageSrc|String|||
|菜单列表|排序序号|sortOrder|Integer|||
|菜单列表|分隔线标识|isDivider|Boolean|||
|菜单列表|状态|status|String||active/inactive|
|菜单列表|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|菜单列表|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|-|页码|page|Integer|||
|-|每页条数|size|Integer|||
|-|总条数|total|Integer|||
|-|总页数|totalPages|Integer|||

### 处理内容

1. 从数据库获取数据
   - 目标表：`sys_menus`
   - 获取条件：
     - `is_deleted = 0`
     - 当参数中包含关键词时：`label LIKE %:keyword%`
     - 当参数中包含状态时：`status = :status`
   - 排序条件：`sort_order ASC, created_at ASC`

2. 响应的生成
   - 成功时返回分页后的菜单列表。
   - 获取件数为0时返回空列表。

## 3. 菜单详情API

菜单详情API是获取指定菜单的详细信息的端点。

### 请求

#### 请求URL

```text
GET /api/v1/menus/{id}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|
|:---|:---|:---|---|:---|
|菜单ID|id|TRUE|String|PATH|

### 响应体

|項目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|
|菜单ID|id|String|||
|父菜单ID|parentId|String|||
|菜单名称|label|String|||
|图标|icon|String|||
|页面标题|pageTitle|String|||
|页面路径|pageSrc|String|||
|排序序号|sortOrder|Integer|||
|分隔线标识|isDivider|Boolean|||
|状态|status|String|||
|创建时间|createdAt|Timestamp|yyyy/MM/dd HH:mm:ss||
|更新时间|updatedAt|Timestamp|yyyy/MM/dd HH:mm:ss||

## 4. 菜单创建API

### 请求

#### 请求URL

```text
POST /api/v1/menus
```

### 请求体

|項目名|键|必須|タイプ|フォーマット|说明|
|:---|:---|:---|---|:---|:---|
|父菜单ID|parentId|FALSE|String|||
|菜单名称|label|TRUE|String|1~100字符||
|图标|icon|FALSE|String||SVG代码|
|页面标题|pageTitle|FALSE|String|||
|页面路径|pageSrc|FALSE|String|||
|排序序号|sortOrder|FALSE|Integer||默认0|
|分隔线标识|isDivider|FALSE|Boolean||默认false|
|状态|status|FALSE|String||默认active|

## 5. 菜单更新API

### 请求

#### 请求URL

```text
PUT /api/v1/menus/{id}
```

### 请求体

|項目名|键|必須|タイプ|フォーマット|说明|
|:---|:---|:---|---|:---|:---|
|父菜单ID|parentId|FALSE|String|||
|菜单名称|label|TRUE|String|||
|图标|icon|FALSE|String|||
|页面标题|pageTitle|FALSE|String|||
|页面路径|pageSrc|FALSE|String|||
|排序序号|sortOrder|FALSE|Integer|||
|分隔线标识|isDivider|FALSE|Boolean|||
|状态|status|FALSE|String|||
|更新时间|updatedAt|TRUE|String|yyyy/MM/dd HH:mm:ss|乐观锁|

## 6. 菜单删除API

### 请求

#### 请求URL

```text
DELETE /api/v1/menus/{id}?updatedAt={updatedAt}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|
|:---|:---|:---|---|:---|
|菜单ID|id|TRUE|String|PATH|
|更新时间|updatedAt|TRUE|String|QUERY|

## 7. 菜单状态变更API

### 请求

#### 请求URL

```text
PATCH /api/v1/menus/{id}/status?status={status}&updatedAt={updatedAt}
```

### 请求参数

|項目名|パラメータ名|必須|タイプ|フォーマット|
|:---|:---|:---|---|:---|
|菜单ID|id|TRUE|String|PATH|
|目标状态|status|TRUE|String|QUERY|
|更新时间|updatedAt|TRUE|String|QUERY|

## 错误消息一览

|错误码|消息|说明|
|:---|:---|:---|
|E9101|菜单ID不能为空。|详情API id为空|
|E9404|菜单不存在。|指定ID的记录不存在|
|E9409|数据已被其他用户修改，请刷新后重试。|乐观锁冲突|
