# Agent 注意事项

## 后端代码文档路径

文档目录 `docs/基本设计/后端代码/` 中 `后` 是简化字（`后` 字 U+540E），不是旧字体（`後` U+5F8C）。

## 后端代码文档结构

每个模块在 `docs/基本设计/后端代码/{module}/` 下需要：

1. **Java 源文件** — 直接从 `backend/` 复制
2. **`.md` 文档文件** — 每个 Java 文件 → 同目录同名 `.md` 文件，内容为源码嵌入
3. **Mapper XML** — 放在 `mapper/resources/mapper/` 下，对应 `.md` 为 `{Entity}MapperXML.md`
4. **`index.md`** — 包含包结构树 + 所有文件的 Markdown 可点击链接

## 生成后端代码时要同时创建的文档

参照 `docs/基本设计/后端代码/user/` 或 `docs/基本设计/后端代码/product/` 的目录结构：
- 每个 `.java` 文件 → 同路劲的 `.md` 文件
- `index.md` → 包结构 + 链接索引
- XML 路径：`{module}/mapper/resources/mapper/` 而非 `{module}/resources/mapper/`

## 每次修改后必须重新验证全流程

- Java 代码修改后 → `mvn compile -q` → 停旧进程 → `mvn spring-boot:run`（必须重启，不会热加载）
- 前端 HTML 修改后 → 直接刷新浏览器即可
- **ORA-17004 陷阱**: Oracle 的 JDBC 驱动无法推断 null 的类型，必须设置 `jdbc-type-for-null: null` 或在 mapper XML 中显式指定 `jdbcType=VARCHAR`
- 前端 `api.get('/carts/items/{id}')` 不存在 → 前端应该使用 `item.updatedAt` 或 `item.createdAt` 直接从已加载的购物车数据中获取
- CartItemVO 需要返回 `updatedAt` 字段，给前端数量修改操作提供乐观锁参数
