---
name: new-module-workflow
description: >
  AI Design 项目中添加新模块（CRUD 功能）的完整工作流程。
  从表定义 → API 设计书 → 画面设计书 → 后端代码 → Oracle DDL → 关联模块更新 → 画面 → mkdocs.yml 依次执行。
  当用户要求"新增模块"、"创建新的 CRUD"、"添加{模块名}功能"时使用此技能。
---

# New Module Workflow — 新模块添加工作流程

> AI Design 项目中，从表定义到画面完整添加一个新 CRUD 模块的 10 步工作流程。

## 前提条件

- 项目根目录: `D:\ai-design`
- 后端: Spring Boot 3.2 + Oracle 19c + MyBatis（`D:\ai-design\backend`）
- 前端: Vue 3 + Tailwind CSS（CDN，无需构建工具）
- 文档: MkDocs（`mkdocs serve` on port 8000）
- 后端启动: `cd D:\ai-design\backend ; mvn spring-boot:run` (port 9090)
- Oracle 连接: `sqlplus rak/rak@"(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.11.24)(PORT=1521))(CONNECT_DATA=(SID=DS6CM)))"`
- 远程仓库: `https://github.com/liuujn/ai-design.git` (origin)
- 字符集: JA16SJIS 不能存储简体中文 → 使用日文汉字或英文
- 所有 API 需要 `Authorization: Bearer <token>` 头（401 → 清除 token → 跳转登录页）

## 工作流程概览（共 10 步）

```
Step 1  ─ 表定义: {Module}.md（表规格文档）
Step 2  ─ API 设计书: {Module}_API设计.md（接口定义）
Step 3  ─ 画面设计书: {Module}_画面设计.md（UI 规格）
Step 4  ─ 后端代码（Java）: Entity/Mapper/Service/Controller/DTOs + Mapper XML
Step 5  ─ Oracle DDL: 创建表、修改现有表
Step 6  ─ 关联模块更新（如添加分类时更新商品关联）
Step 7  ─ 画面(HTML): CRUD 画面（Vue 3）
Step 8  ─ 关联画面更新（如添加分类时给商品画面加下拉框）
Step 9  ─ mkdocs.yml 更新 + 导航更新
Step 10 ─ 编译验证 + API 测试
```

---

## Step 1: 表定义

**文件**: `docs/基本设计/表定义/{Module}.md`

### 创建规则

1. 参考现有表定义（如 `docs/基本设计/表定义/商品分类.md`）
2. 必填字段（模板）:
   - `id` VARCHAR2(36) PK — UUID
   - `status` VARCHAR2(20) — active/inactive
   - `created_at` TIMESTAMP
   - `created_by` VARCHAR2(36)
   - `updated_at` TIMESTAMP
   - `updated_by` VARCHAR2(36)
   - `is_deleted` NUMBER(1) — 逻辑删除标记
3. 使用中文描述
4. 表名使用英文复数形式（categories, products, orders…）

### 输出

- `docs/基本设计/表定义/{Module}.md`
- mkdocs.yml 添加在 Step 9 中处理

---

## Step 2: API 设计书

**文件**: `docs/基本设计/API设计书/{Module}_API设计.md`

### 包含内容

1. **模块概述**（功能说明）
2. **所有端点一览表**
   - `GET /api/v1/{modules}` — 分页列表
   - `GET /api/v1/{modules}/all-active` — 获取所有激活记录（下拉框用，仅分类需要）
   - `GET /api/v1/{modules}/{id}` — 详情
   - `POST /api/v1/{modules}` — 创建
   - `PUT /api/v1/{modules}/{id}` — 更新（乐观锁）
   - `DELETE /api/v1/{modules}/{id}` — 删除（乐观锁）
3. **每个端点的详细信息**
   - 请求 URL / 方法
   - 请求参数（body/query/path）
   - 响应结构
   - 校验规则
   - 错误消息
4. **错误消息一览**（E9xxx 格式，同时追加到 `Messages.md`）

### 注意事项

- 新错误消息必须追加到 `docs/基本设计/Messages.md`
- 乐观锁 `updatedAt` 在所有更新/删除/状态变更 API 中必填

---

## Step 3: 画面设计书

**文件**: `docs/基本设计/画面设计书/{Module}_画面设计.md`

### 包含内容

1. **画面概述**（功能、用户角色）
2. **画面构成** — 布局说明（搜索区域 / 列表表格 / 编辑对话框）
3. **功能一览**（CRUD 操作列表）
4. **画面イメージ**（ASCII 艺术简化线框图）
5. **列表表格项目一览**（列名、说明、显示格式）
6. **表单项目一览**（字段名、类型、必填、校验规则）

---

## Step 4: 后端代码生成

**文件**: `docs/基本设计/后端代码/{module}/`（文档 Java 源文件）+ `backend/src/main/java/com/example/app/{module}/`（运行代码）

### 生成规则

1. 按 `.github/prompts/generate-backend.prompt.md` 的规范生成代码
2. 同时输出到两个路径:
   - 路径 A（文档）: `docs/基本设计/后端代码/{module}/`（.java + .md 都生成）
   - 路径 B（运行）: `backend/src/main/java/com/example/app/{module}/`（仅 .java）
3. 每个模块的文件结构:
   ```
   {module}/
   ├── controller/{Entity}Controller.java
   ├── service/{Entity}Service.java
   ├── service/impl/{Entity}ServiceImpl.java
   ├── mapper/{Entity}Mapper.java
   ├── mapper/resources/mapper/{Entity}Mapper.xml   ← 路径 B 在 backend/src/main/resources/mapper/
   ├── model/entity/{Entity}.java
   ├── model/enums/{Entity}Status.java（如需要）
   ├── model/dto/request/{Entity}CreateRequest.java
   ├── model/dto/request/{Entity}UpdateRequest.java
   ├── model/dto/request/{Entity}DeleteRequest.java
   ├── model/dto/request/{Entity}ListQuery.java
   ├── model/dto/response/{Entity}CreateVO.java
   ├── model/dto/response/{Entity}UpdateVO.java
   ├── model/dto/response/{Entity}ListVO.java
   ├── model/dto/response/{Entity}DetailVO.java
   └── model/dto/response/{Entity}SimpleVO.java（all-active 用，如需要）
   ```
4. 路径 A 的 **`.md` 文档文件** 每个 Java 文件都要生成:
   - 同目录同名 `.md`（如 `CategoryController.java` → `CategoryController.md`）
   - 内容为 Java 源码用代码块包裹
   - XML → `{Entity}MapperXML.md`
5. 创建 `index.md`:
   - 包结构树（ASCII）
   - 所有文件的 Markdown 可点击链接
   - 格式参考 `docs/基本设计/后端代码/product/index.md`

### 路径 B 注意事项

- XML 放在 `backend/src/main/resources/mapper/` 下（与文档的 `mapper/resources/mapper/` 路径不同）
- `PageResult.java`, `BusinessException.java`, `GlobalExceptionHandler.java` 等为共享类（不要重新生成）

---

## Step 5: Oracle DDL

### 创建表

```sql
CREATE TABLE {table_name} (
    id VARCHAR2(36) PRIMARY KEY,
    ...
    status VARCHAR2(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    created_by VARCHAR2(36),
    updated_at TIMESTAMP DEFAULT SYSTIMESTAMP,
    updated_by VARCHAR2(36),
    is_deleted NUMBER(1) DEFAULT 0
);

CREATE INDEX idx_{table}_status ON {table_name}(status);
CREATE INDEX idx_{table}_deleted ON {table_name}(is_deleted);
```

### 修改现有表（如需要）

```sql
ALTER TABLE existing_table ADD (new_column VARCHAR2(36));
COMMENT ON COLUMN existing_table.new_column IS '说明';
```

### sqlplus 连接

```powershell
sqlplus rak/rak@"(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=172.17.11.24)(PORT=1521))(CONNECT_DATA=(SID=DS6CM)))"
```

---

## Step 6: 关联模块更新

当新模块与现有模块有关联时，需要更新现有模块。

示例：**添加商品分类（category）时更新商品模块**:
1. `products` 表添加 `category_id VARCHAR2(36)` 列（DDL）
2. `Product.java` Entity 添加 `categoryId` 字段
3. `ProductMapper.xml` 的 SELECT/INSERT/UPDATE 添加 `category_id`
4. `ProductCreateRequest` / `ProductUpdateRequest` 添加 `categoryId`
5. `ProductServiceImpl` 的 create/update 中从 `categories` 表自动获取 `name` 填充 `category` 字段
6. `ProductListVO` / `ProductDetailVO` / `ProductCreateVO` 添加 `categoryId`
7. 表定义 `docs/基本设计/表定义/商品.md` 追加 `category_id` 字段

---

## Step 7: 画面(HTML)

**文件**: `docs/基本设计/画面/{Module}.html`

### 技术栈

- CDN: Vue 3 + Tailwind CSS + axios + FontAwesome（lucide）
- 所有 API 携带 `Authorization: Bearer ${localStorage.getItem('auth_token')}` 头
- 401 → `localStorage.removeItem('auth_token'); window.parent.postMessage('logout', '*')`

### 画面模板

以 `docs/基本设计/画面/商品分类.html` 为基础，替换以下内容:
1. 标题、API 端点（`/api/v1/{modules}`）
2. 列表表格的列
3. 搜索表单的字段
4. 创建/编辑对话框的字段
5. 表单校验
6. 界面文案

---

## Step 8: 关联画面更新

当新模块影响现有画面时，更新现有 HTML。

示例：**添加分类时更新商品画面**:
1. `docs/基本设计/画面/商品.html` 的搜索表单添加分类下拉框
2. 创建/编辑对话框添加分类下拉框
3. 下拉框数据从 `GET /api/v1/categories/all-active` 获取
4. 在 `created` 生命周期中异步 fetch

---

## Step 9: mkdocs.yml + 导航更新

### 在 mkdocs.yml nav 中添加

`mkdocs.yml` 的 `nav` → `基本设计` 下按分区添加:
1. 表定义: `- {Module}: 基本设计/表定义/{Module}.md`
2. API 设计: `- {Module}: 基本设计/API设计书/{Module}_API设计.md`
3. 画面设计: `- {Module}: 基本设计/画面设计书/{Module}_画面设计.md`
4. 后端代码: `- {Module}: 基本设计/后端代码/{module}/index.md`
5. 画面(HTML): `- {Module}: 基本设计/画面/{Module}.html`

### 更新主页（主页.html）

1. `docs/基本设计/画面/主页.html` 的 `menuItems` 数组添加新菜单项
2. 可选：在仪表盘添加快捷入口卡片
3. 调整所有 `switchMenu(N)` 的索引值

---

## Step 10: 编译验证 + API 测试

### 1. Maven 编译

```powershell
cd D:\ai-design\backend; mvn compile -q
```

如有错误，修复后重新编译。

### 2. 重启后端

每次修改 Java 代码后，必须停止旧进程并重启（仅 `mvn compile` 不会自动热加载新 Controller 映射）:

```powershell
# 查找并杀掉旧进程
$pid = (netstat -ano | Select-String LISTENING | Select-String ":9090" | ForEach-Object { ($_ -split '\s+')[-1] } | Select-Object -First 1)
if ($pid) { Stop-Process -Id $pid -Force; Start-Sleep -Seconds 3 }
# 重启
cd D:\ai-design\backend; Start-Process -NoNewWindow -FilePath "mvn" -ArgumentList "spring-boot:run"
Start-Sleep -Seconds 15
```

> **重要**: 新建模块添加 Controller 后、或修改任何 Java 源文件后，必须重启后端进程。PID 查找方式: `netstat -ano | Select-String ":9090"`

### 3. API 测试

登录获取 token，然后执行所有 CRUD 测试:

```powershell
# 登录
$r = curl.exe -s -X POST "http://localhost:9090/api/v1/auth/login" -H "Content-Type: application/json" -d '{"username":"admin","password":"admin123"}'
$token = if ($r -match '"accessToken":"([^"]+)"') { $Matches[1] } else { "none" }
```

执行 CRUD 测试:
- `POST /api/v1/{modules}` → 201 + 创建数据
- `GET /api/v1/{modules}` → 200 + 分页结果
- `GET /api/v1/{modules}/{id}` → 200 + 详情
- `PUT /api/v1/{modules}/{id}` → 200 + 更新数据
- `DELETE /api/v1/{modules}/{id}` → 204
- 乐观锁验证: 错误的 `updatedAt` → 409
- 关联 API 验证（如 all-active）

### 4. 全流程端到端验证 ⭐

每次修改或添加代码后，**必须从头到尾跑一遍完整流程**，不能只测试改动部分。例如购物车模块验证:

```powershell
# 1. 商品列表
$prods = curl.exe -s -H "Authorization: Bearer $token" "http://localhost:9090/api/v1/products?status=active&size=1"
# 提取第一个商品ID

# 2. 加入购物车
curl.exe -s -X POST "http://localhost:9090/api/v1/carts/items" -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{\"productId\":\"...\",\"quantity\":2}'
# 验证返回 itemId

# 3. 查看购物车
$cart = curl.exe -s -H "Authorization: Bearer $token" "http://localhost:9090/api/v1/carts/my"
# 验证 items 不为空，totalAmount 正确

# 4. 修改数量（验证 ± 按钮）
$itemUpdatedAt = (从 cart 解析)
curl.exe -s -X PUT "http://localhost:9090/api/v1/carts/items/{itemId}" -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d "{\"quantity\":4,\"updatedAt\":\"$itemUpdatedAt\"}"
# 验证 quantity 已更新

# 5. 选择地址
curl.exe -s -X PUT "http://localhost:9090/api/v1/carts/address" -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{\"addressId\":\"...\",\"updatedAt\":\"...\"}'
# 验证 cart.addressId 已设置

# 6. 结算下单
curl.exe -s -X POST "http://localhost:9090/api/v1/carts/checkout" -H "Authorization: Bearer $token" -H "Content-Type: application/json" -d '{\"updatedAt\":\"...\"}'
# 验证返回 orderNo

# 7. 结算后购物车为空
$cart2 = curl.exe -s -H "Authorization: Bearer $token" "http://localhost:9090/api/v1/carts/my"
# 验证 items 为空数组

# 8. 订单出现
$orders = curl.exe -s -H "Authorization: Bearer $token" "http://localhost:9090/api/v1/orders?size=5"
# 验证新订单在列表中
```

### 5. 前端验证

```powershell
cd D:\ai-design; mkdocs serve
```

- 打开 `http://localhost:8000` 确认画面正常显示
- 执行操作: 登录 → 导航到页面 → CRUD 操作
- 检查浏览器控制台是否有 JS 错误

### 6. MkDocs 构建验证

```powershell
cd D:\ai-design; mkdocs build --clean
```

确认没有 ERROR，WARNING 只接受预存问题（order 模块缺少 .md、图片缺失）。

---

## 修改现有代码时的规则

每次对现有代码进行修改后，必须重新执行完整验证流程：

### 后端修改后
1. 重新编译: `mvn compile -q`
2. 重启后端进程（必须 Stop-Process + 重新 `mvn spring-boot:run`）
3. 登录验证
4. 执行被修改模块的完整 CRUD 测试
5. **执行所有关联模块的端到端流程**

### 前端修改后
1. 刷新浏览器页面（无需重启服务）
2. 执行完整的操作流程验证
3. 检查所有关联页面的联动是否正常

### 修改后端代码常见陷阱

| 症状 | 原因 | 解决 |
|------|------|------|
| 404 (新 Controller) | 未重启后端 | `Stop-Process` + `mvn spring-boot:run` |
| ORA-17004 (Oracle 列类型无效) | 向 Oracle 插入 null 时未指定 JDBC 类型 | 设置 `jdbc-type-for-null: null` 或在 mapper 中加 `jdbcType=VARCHAR` |
| 前端 401 跳转 | token 过期、未登录 | `localStorage.getItem('auth_token')` 检查 |
| 前端调用的 API 不存在的 404 | 前端代码与后端接口不匹配 | 先检查 `CartController.java` 有哪些端点 |

---

## 完成检查清单

- [ ] 表定义文件创建
- [ ] API 设计书创建，Messages.md 更新
- [ ] 画面设计书创建
- [ ] 后端代码双路径生成（.java + .md + index.md）
- [ ] Oracle DDL 执行，表创建完成
- [ ] 关联模块更新（Entity/XML/Service）
- [ ] 画面 HTML 创建 + 关联画面更新
- [ ] mkdocs.yml nav 更新 + 主页.html menuItems 更新
- [ ] Maven compile 成功
- [ ] 重启后端进程
- [ ] 所有 API 端点 CRUD 正常动作确认
- [ ] 全流程端到端验证通过（商品→购物车→地址→订单）
- [ ] 前端页面操作正常
- [ ] MkDocs build 无 ERROR
