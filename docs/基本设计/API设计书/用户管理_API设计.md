# 用户管理API

## API 功能列表

|功能名|CRUD|概要|方法|URI|
|:---|:---|:---|:---|:---|
|用户登录|C|验证用户凭证并生成登录令牌|POST|/api/v1/auth/login|
|用户列表|R|分页查询用户信息|GET|/api/v1/users|
|用户详情|R|获取单个用户详细信息|GET|/api/v1/users/{id}|
|用户新建|C|创建新用户账号并初始化用户信息|POST|/api/v1/users|
|用户更新|U|更新已有用户信息并保证数据一致性|PUT|/api/v1/users/{id}|
|用户状态更新|U|更新用户状态（启用/停用）|PUT|/api/v1/users/{id}/status|
|用户删除|D|逻辑删除用户账号|DELETE|/api/v1/users/{id}|

## 1. 用户登录API

用户登录API用于接收用户名/密码凭证，验证身份，并返回访问令牌与会话信息。

### 请求

#### 请求URL

```text
POST /api/v1/auth/login
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户账号|username|TRUE|String|-|||
|登录密码|password|TRUE|String|-|||
|记住我|rememberMe|FALSE|Boolean|true/false|false|省略时默认false|
|多因素验证码|otp|FALSE|String|-||MFA启用时必填|
|客户端类型|clientType|FALSE|String|web/mobile|web|可选|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|访问令牌|accessToken|String|JWT|登录成功时返回的访问令牌|
|-|令牌类型|tokenType|String|-|`Bearer`|
|-|有效期限|expiresIn|Integer|seconds|访问令牌有效期|
|-|刷新令牌|refreshToken|String|-|可选：刷新令牌|
|-|用户ID|userId|String|-|登录用户标识|
|-|用户名|userName|String|-|登录用户显示名|
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 请求参数的验证
   - 用户账号：
     - 必填检查：E9101 用户名不能为空。
     - 字符数检查：E9102 用户名长度应为1到100个字符。
     - 禁止字符检查：E9103 用户名不能包含禁止字符或控制字符。
   - 登录密码：
     - 必填检查：E9104 密码不能为空。
     - 字符数检查：E9105 密码长度应为8到128个字符。
     - 格式检查：E9106 密码必须为半角字符。

2. 用户认证
   - 目标表：`users`
   - 获取条件：`username = :username`
   - 用户不存在时认证失败：E9108 用户名或密码不正确。
   - `status` 不为 `active` 时：E9109 账户不可用。
   - 账户锁定检查：E9110 账户已被锁定，请稍后重试。
   - 密码校验：输入密码哈希后与 `password_hash` 比对。
   - 失败时 `failed_login_count` 递增，达到阈值后锁定账户。

3. 会话和令牌生成
   - 生成 JWT 访问令牌，负载包含 `sub`(userId)、`username`、`iat`、`exp`、`aud`。
   - `rememberMe = false` 时 expiresIn = 1800秒，`true` 时 = 86400秒。
   - 登录成功时将 `failed_login_count` 重置为0，清除 `lock_expires_at`。

4. 响应的生成
   - 成功：返回 `accessToken`、`tokenType`、`expiresIn`、`refreshToken`、`userId`、`userName`。
   - 失败：返回错误消息。

## 2. 用户列表API

用户列表API用于分页查询用户信息，支持多条件筛选。

### 请求

#### 请求URL

```text
GET /api/v1/users
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|页码|page|FALSE|Integer|-|0||
|每页条数|size|FALSE|Integer|-|20||
|用户账号|username|FALSE|String|-||模糊查询|
|用户名称|displayName|FALSE|String|-||模糊查询|
|邮箱地址|email|FALSE|String|-||模糊查询|
|用户状态|status|FALSE|String|-||active/inactive|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|内容|content|Array|-|用户列表|
|内容|用户ID|id|String|-||
|内容|用户账号|username|String|-||
|内容|用户名称|displayName|String|-||
|内容|邮箱地址|email|String|-||
|内容|电话号码|phone|String|-||
|内容|用户状态|status|String|-||
|内容|创建时间|createdAt|DateTime|yyyy/MM/dd HH:mm:ss||
|内容|更新时间|updatedAt|DateTime|yyyy/MM/dd HH:mm:ss||
|-|总记录数|total|Long|-||
|-|当前页|page|Integer|-||
|-|每页条数|size|Integer|-||
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 请求参数的验证
   - 页码：默认为0，不能为负数。
   - 每页条数：默认为20，最大不超过100。

2. 从数据库获取数据
   - 目标表：`users`
   - 获取条件：
     - `is_deleted = false`
     - `username LIKE %参数%`（当参数不为空时）
     - `display_name LIKE %参数%`（当参数不为空时）
     - `email LIKE %参数%`（当参数不为空时）
     - `status = 参数`（当参数不为空时）
   - 排序条件：`created_at DESC`

3. 响应的生成
   - 成功：返回分页后的用户列表。
   - 失败：返回错误消息。

## 3. 用户详情API

用户详情API用于获取单个用户的完整信息。

### 请求

#### 请求URL

```text
GET /api/v1/users/{id}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户ID|id|TRUE|String|-||URL路径参数|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|用户ID|id|String|-||
|-|用户账号|username|String|-||
|-|用户名称|displayName|String|-||
|-|邮箱地址|email|String|-||
|-|电话号码|phone|String|-||
|-|用户状态|status|String|-||
|-|创建时间|createdAt|DateTime|yyyy/MM/dd HH:mm:ss||
|-|更新时间|updatedAt|DateTime|yyyy/MM/dd HH:mm:ss||
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 从数据库获取数据
   - 目标表：`users`
   - 获取条件：`id = :id AND is_deleted = false`
   - 用户不存在时：E2012 用户不存在。

2. 响应的生成
   - 成功：返回用户详细信息。
   - 失败：返回错误消息。

## 4. 用户新建API

用户新建API用于创建新的用户账号，包括基本信息和认证方式。

### 请求

#### 请求URL

```text
POST /api/v1/users
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户账号|username|TRUE|String|-|||
|用户名称|displayName|TRUE|String|-|||
|邮箱地址|email|TRUE|String|email|-|必须唯一|
|电话号码|phone|FALSE|String|phone|-||
|登录密码|password|TRUE|String|-||至少8字符，含大小写和数字|
|确认密码|confirmPassword|TRUE|String|-||与password一致|
|用户状态|status|FALSE|String|active/inactive|active||

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|用户ID|id|String|-|新建用户的唯一标识|
|-|用户账号|username|String|-||
|-|用户名称|displayName|String|-||
|-|邮箱地址|email|String|email||
|-|用户状态|status|String|-||
|-|创建时间|createdAt|DateTime|yyyy/MM/dd HH:mm:ss||
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 请求参数的验证
   - 用户账号：必填检查 E1001、字符数检查 E1002（3~50字符）、格式检查 E1003（字母数字下划线）。
   - 用户名称：必填检查 E1005、字符数检查 E1006（1~100字符）。
   - 邮箱地址：必填检查 E1008、格式检查 E1009、唯一性检查 E1010。
   - 密码：必填检查 E1012、字符数检查 E1013（8~128字符）、强度检查 E1014（含大小写和数字）。
   - 确认密码：必填检查 E1015、匹配检查 E1016。

2. 唯一性检查
   - `username` 唯一性：`SELECT id FROM users WHERE username = :username`
   - `email` 唯一性：`SELECT id FROM users WHERE email = :email`

3. 创建新用户
   - 使用 bcrypt 对密码加密生成 `password_hash`。
   - 生成 UUID 作为用户ID。
   - 插入 `users` 表：`id`、`username`、`display_name`、`email`、`phone`、`password_hash`、`status`、`created_at`、`created_by`、`updated_at`、`updated_by`、`is_deleted`。

4. 响应的生成
   - 成功：返回新建用户的完整信息。
   - 失败：返回错误消息。

## 5. 用户更新API

用户更新API用于更新已有用户的信息，使用乐观锁确保并发场景下数据一致性。

### 请求

#### 请求URL

```text
PUT /api/v1/users/{id}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户名称|displayName|FALSE|String|-|||
|邮箱地址|email|FALSE|String|email|-|必须唯一|
|电话号码|phone|FALSE|String|phone|-||
|登录密码|password|FALSE|String|-||更新密码时必填|
|确认密码|confirmPassword|FALSE|String|-||与password一致|
|用户状态|status|FALSE|String|active/inactive|||
|更新时间|updatedAt|TRUE|String|yyyy/MM/dd HH:mm:ss||乐观锁字段|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|用户ID|id|String|-||
|-|用户账号|username|String|-||
|-|用户名称|displayName|String|-||
|-|邮箱地址|email|String|email||
|-|电话号码|phone|String|phone||
|-|用户状态|status|String|-||
|-|更新时间|updatedAt|DateTime|yyyy/MM/dd HH:mm:ss|更新后的时间戳|
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 请求参数的验证
   - 邮箱地址：格式检查 E2004、唯一性检查 E2005。
   - 密码：字符数检查 E2007（8~128字符）、强度检查 E2008。
   - 确认密码：匹配检查 E2009。
   - 更新时间：必填检查（乐观锁不能为空）。

2. 从数据库获取数据
   - 目标表：`users`
   - 获取条件：`id = :id AND is_deleted = false`
   - 用户不存在时：E2012 用户不存在。

3. 乐观锁校验
   - 对比请求中的 `updatedAt` 与数据库中的 `updated_at`。
   - 不匹配时：E2013 数据已被其他用户修改，请刷新后重试。

4. 更新用户信息
   - 更新字段：`display_name`、`email`、`phone`、`password_hash`、`status`、`updated_at`、`updated_by`。
   - 更新时间戳更新为当前时间。

5. 响应的生成
   - 成功：返回更新后的用户信息。
   - 失败：返回错误消息。

## 6. 用户状态更新API

用户状态更新API用于启用或停用用户账号。

### 请求

#### 请求URL

```text
PUT /api/v1/users/{id}/status
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|用户状态|status|TRUE|String|active/inactive|||
|更新时间|updatedAt|TRUE|String|yyyy/MM/dd HH:mm:ss||乐观锁字段|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|用户ID|id|String|-||
|-|用户状态|status|String|-|更新后的状态|
|-|更新时间|updatedAt|DateTime|yyyy/MM/dd HH:mm:ss||
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 请求参数的验证
   - 用户状态：必须为 `active` 或 `inactive`。

2. 乐观锁校验
   - 对比请求中的 `updatedAt` 与数据库中的 `updated_at`。
   - 不匹配时返回乐观锁冲突错误。

3. 更新用户状态
   - 目标表：`users`
   - 更新字段：`status`、`updated_at`、`updated_by`。

4. 响应的生成
   - 成功：返回更新后的用户状态。
   - 失败：返回错误消息。

## 7. 用户删除API

用户删除API用于逻辑删除用户账号。

### 请求

#### 请求URL

```text
DELETE /api/v1/users/{id}
```

### 请求参数

|项目名|参数名|必填|类型|格式|初始值|条件|
|:---|:---|:---|---|:---|:---|:---|
|更新时间|updatedAt|TRUE|String|yyyy/MM/dd HH:mm:ss||乐观锁字段|

### 响应体

|父项目|项目名|键|类型|格式|说明|
|:---|:---|:---|:---|:---|:---|
|-|消息|message|String|-|仅在出错时返回|

### 处理内容

1. 乐观锁校验
   - 对比请求中的 `updatedAt` 与数据库中的 `updated_at`。
   - 不匹配时返回乐观锁冲突错误。

2. 逻辑删除
   - 目标表：`users`
   - 更新字段：`is_deleted = true`、`updated_at`、`updated_by`。

3. 响应的生成
   - 成功：返回204 No Content。
   - 失败：返回错误消息。

## 共通项目

### 目标表

用户主数据 `users`

|字段名|类型|说明|
|:---|:---|---|
|id|VARCHAR|用户唯一标识（UUID）|
|username|VARCHAR|用户账号（唯一）|
|display_name|VARCHAR|用户显示名|
|email|VARCHAR|邮箱地址（唯一）|
|phone|VARCHAR|电话号码|
|password_hash|VARCHAR|密码哈希值|
|status|VARCHAR|账户状态（active/inactive）|
|failed_login_count|INTEGER|失败登录次数|
|lock_expires_at|TIMESTAMP|账户锁定过期时间|
|last_login_at|TIMESTAMP|最后登录时间|
|mfa_enabled|BOOLEAN|是否启用多因素认证|
|mfa_secret|VARCHAR|MFA密钥|
|created_at|TIMESTAMP|创建时间|
|created_by|VARCHAR|创建者ID|
|updated_at|TIMESTAMP|更新时间（乐观锁字段）|
|updated_by|VARCHAR|更新者ID|
|is_deleted|BOOLEAN|逻辑删除标志|

### 错误处理

- 使用统一的错误响应格式。
- 乐观锁冲突：`E2013` 数据已被其他用户修改，请刷新后重试。
- 资源不存在：`E2012` 用户不存在。

### 安全要求

- 密码必须采用 bcrypt 加密存储。
- 敏感信息（如密码）不应在响应中返回。
- 操作者身份必须经过 JWT 认证。
- 删除操作采用软删除（逻辑删除），保留审计跟踪。
