# LServer 项目知识点整理

## 技术栈总览

| 层级 | 技术 | 用途 |
|------|------|------|
| 后端框架 | Jersey (JAX-RS) | REST API 路由（`@Path`、`@POST`、`@Produces`） |
| ORM | Seasar DOMA 2 | 数据库访问：`@Dao` + SQL 文件 + `@Entity` 映射 |
| JSON | Jackson (`JacksonJsonProvider`) | Java 对象 ↔ JSON 序列化 |
| Web 容器 | Tomcat 9 / Servlet 3.0 | HTTP 容器 |
| 前端框架 | Backbone.js | 前端 MVC（Model/View/Collection） |
| 模板引擎 | Handlebars | JavaScript 模版渲染（预编译模式） |
| CSS 预处理 | Less | 样式文件编写（`.less` 文件） |
| 构建 | Grunt.js | 前端构建编译（Handlebars 预编译、Less 编译等） |
| 样式 | Bootstrap | UI 基础样式 |
| 依赖 | jQuery | DOM 操作、Ajax 请求 |

---

## 一、后端核心知识点

### 1. Jersey JAX-RS — REST API

**作用：** 处理所有 HTTP 请求，路由到对应的 Resource 类。

**web.xml 配置：**
```xml
<filter>
    <filter-name>Jersey</filter-name>
    <filter-class>com.sun.jersey.spi.container.servlet.ServletContainer</filter-class>
    <init-param>
        <param-name>javax.ws.rs.Application</param-name>
        <param-value>jp.co.isid.rialips.common.RestApp</param-value>
    </init-param>
</filter>
<filter-mapping>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

所有 URL 经过 Jersey Filter，由 `RestApp` 自动扫描所有 `*Resource.class` 注册。

**RestApp 自动发现资源：** `RestApp.java:25`
```java
classSet.addAll(ClassUtils.getAllClassName("(\\S)*Resource"));
```
扫描 `jp.co.isid.rialips` 包下所有类名以 `Resource` 结尾的类。

**典型 Resource 类模式：**
```java
@Path("/md/fwakedaicho/FwakeDaichoKoma")
public class GetFwakeDaichoResource extends MDLogger {
    @Context private HttpServletRequest request;
    @FormParam("hinban") private String hinban;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFwakeDaichoKomas() {
        // 1. 获取 Session 用户
        UserDfltInfo userDfltInfo = SessionUtils.getUserInfo(request);
        // 2. 调用 Service
        FwakeDaichoService fwakeService = new FwakeDaichoServiceImpl();
        appDatas = fwakeService.getFwakeDaichoDatas(hinban, ...);
        // 3. 包装为 JsonResult 返回
        result.setData(appDatas);
        return Response.ok(result).build();
    }
}
```

**常用注解：**
- `@Path` — 定义 URL 路径
- `@POST` / `@GET` — HTTP 方法
- `@Produces(MediaType.APPLICATION_JSON)` — 返回 JSON
- `@FormParam` — 接收表单参数
- `@Context` — 注入 HttpServletRequest 等

### 2. DOMA 框架 — 数据库 ORM

**官方名称为 Seasar DOMA 2**，是日本 Seasar 基金会开发的 ORM 框架。

#### 2.1 Config — 数据库配置

`AppConfig.java` 实现 `org.seasar.doma.jdbc.Config` 接口：

```java
@Dao(config = AppConfig.class)  // DAO 通过此注解引用配置
public interface TbdFwakeDaichoDao { ... }
```

AppConfig 核心功能：
- `getDataSource()` → 返回 `LocalTransactionDataSource`（基于 `SimpleDataSource`）
- `getDialect()` → 返回 `OracleDialect`
- `getSqlFileRepository()` → `GreedyCacheSqlFileRepository`（缓存 SQL 文件）

数据源从 `appConfig.properties` 读取：
```properties
db_driver = oracle.jdbc.driver.OracleDriver
db_url = jdbc:oracle:thin:@172.17.11.24:1521:DS6CM
db_username = rak
db_password = rak
```

#### 2.2 DAO — 数据访问接口

```java
@Dao(config = AppConfig.class)
public interface TbdFwakeDaichoDao {

    @Select
    List<FwakeDaichoEntity> getAllFwakeDaichoKomas(
        String tantoSyuCd, String hinban, String brandCd, ...);

    @BatchUpdate(sqlFile = true)
    int[] update(List<TbdFwakeDaichoUpdateEntity> entitys);
}
```

**关键规则：**
- DAO 必须是 `interface`，DOMA 在编译期通过 Annotation Processor 生成 `Impl` 类
- 方法名对应 SQL 文件名（见下文）
- `@Select` 默认 `sqlFile = true`，从外部 SQL 文件读取 SQL

#### 2.3 SQL 文件查找规则（核心知识点）

路径规则：
```
src/META-INF/{DAO接口全限定名}/{方法名}.sql
```

示例：
```
DAO: jp.co.isid.rialips.md.fwakedaicho.dao.TbdFwakeDaichoDao
方法: getAllFwakeDaichoKomas()
路径: src/META-INF/jp/co/isid/rialips/md/fwakedaicho/dao/TbdFwakeDaichoDao/getAllFwakeDaichoKomas.sql
```

**DOMA 特有 SQL 语法：**
- `/* 参数名 */'默认值'` — 绑定变量（`'默认值'` 仅供 IDE 语法校验，运行时被替换）
- `/* 参数名 */` — 简单值替换
- `/* 参数名 */'默认值'` + `IN` — 自动展开 List 参数

```sql
WHERE hinban = /* hinban */'0304477'
  AND conv_size_cd IN /* convSizeCds */('11','13')
```

#### 2.4 Entity — 实体映射

```java
@Entity(listener = FwakeDaichoListener.class)
public class FwakeDaichoEntity {
    @Column(name = "FWAKE_DAICHO_SEQ_NO")
    private String fwakeDaichoSeqNo;

    @Column(name = "HINBAN")
    private String hinban;
    // getter/setter ...
}
```

**映射规则：**
- `@Column(name = "...")` 中的 name 对应 SQL 结果集列名（大小写不敏感）
- DOMA 自动调用 setter 注入值
- 所有字段使用 `String` 类型（非 int/date），避免 Oracle 数字精度问题
- `@Entity(listener = ...)` 可指定生命周期监听器

#### 2.5 事务管理

继承体系：
```
LipsTransactionService (abstract)
  └── LipsMDTransaction (abstract)
        └── FwakeDaichoServiceImpl
```

```java
public class FwakeDaichoServiceImpl extends LipsMDTransaction {
    public Map<String, Object> getFwakeDaichoDatas(...) {
        begin();          // 开启 DOMA LocalTransaction
        try {
            // ... DAO 调用 ...
            commit();
        } catch(Exception e) {
            rollback();
        }
    }
}
```

事务来源于 `AppConfig.getLocalTransaction()` → `LocalTransactionDataSource`。

### 3. 后端分层架构

```
Resource (JAX-RS, 接收HTTP请求)
    ↓
Service (业务逻辑 + 事务管理)
    ↓
DAO (DOMA接口, 数据库访问)
    ↓
SQL 文件 (DOMA管理) → Oracle DB
    ↓
Entity (DOMA映射) → VO (手动转换, 返回给前端)
```

**Entity vs VO 分离：**
- Entity — 与数据库表字段严格对应，不暴露给前端
- VO — 按前端需求裁剪字段（`FwakeRecordRowVO`、`FwakeDaichoHinbanInfoVO`）
- Service 中手动将 Entity 转换为 VO

### 4. JSON 响应格式

统一的 `JsonResult` 包装类：
```java
public class JsonResult {
    private String status;  // "ok" | "error" | "info"
    private String message; // 错误消息
    private Object data;    // 实际数据（Map<String, Object>）
}
```

前端根据 `status` 判断请求是否成功。

---

## 二、前端核心知识点

### 1. Backbone.js — 前端 MVC

**核心组件：**
- `Backbone.Model` — 数据模型（`App.Models.Koma`、`App.Models.FwakeRow`）
- `Backbone.Collection` — 模型集合（`App.Collections.KomaList`、`App.Collections.FwakeRowList`）
- `Backbone.View` — 视图（`App.Views.KomaView`、`App.Views.FwakeRowView`）

**典型 View 模式：**
```javascript
App.Views.KomaView = Backbone.View.extend({
    tagName: 'div',
    className: 'koma-view',
    template: App.Templates.komahinbanview,  // 预编译的 Handlebars 模板
    render: function() {
        this.$el.html(this.template(this.model.toJSON()));
        return this;
    }
});
```

**事件驱动通信：** 各 View 之间通过 `Backbone.Events` 的 `publish/subscribe` 模式通信。

### 2. Handlebars — 前端模板

**模板预编译：** `.handlebars` 文件通过 Grunt 编译为 JavaScript 函数，嵌入到主 JS 文件中。

```javascript
// 编译后直接用
this.$el.html(App.Templates.komahinbanview(this.model.toJSON()));
```

**典型模板结构（`.handlebars` 文件）：**
```html
<div class="koma-item">
    <span class="size-mark">{{sizeMark}}</span>
    <span class="maisu">{{maisu}}</span>
</div>
```

### 3. 前端数据流

```
index.html (页面骨架)
    ↓ 加载
js/fwakedaicho.js (所有 JS + 预编译模板)
    ↓
App.Utils.DataSource ($.ajax 调用 REST API)
    ↓ 返回 JSON
全局变量缓存 (TBD_FWAKE_DAICHO_DATA, FWAKE_SIZE_MAISU_MAP 等)
    ↓
Backbone.Collection.reset(data)
    ↓ 触发
Backbone.View.render()
    ↓
Handlebars 模板 → HTML → DOM 渲染
```

### 4. 前端项目结构

```
WebContent/{module}/
├── index.html                     # 入口页面
├── design/
│   └── config.js                  # 全局配置 URL、消息等
├── css/                           # Less 样式文件
├── img/                           # 图片
├── js/
│   ├── models.js                  # Backbone Model/Collection
│   ├── views.js                   # Backbone View
│   ├── managers.js                # 业务逻辑（拖拽、布局等）
│   ├── datas.js                   # 数据访问（Ajax REST 调用 + 缓存）
│   └── setup.js                   # 启动入口
└── templates/                     # Handlebars 模板源文件
    └── *.handlebars
```

实际项目中有时合并为单个 `.js` 文件（如 `fwakedaicho.js` 含模板、Model、View、Manager）。

---

## 三、完整数据流示例

用户输入品番 → 页面渲染：

```
1. 用户输入品番 "12345"，按回车
2. SelectView 捕获事件 → dataSource.searchFwakeDaichoDatas("12345")
3. $.ajax GET /LServer/md/fwakedaicho/FwakeHinbTonyuMaisu/12345
                                        ↓
   GetFwakeHinbTonyuMaisuResource (Jersey @Path)
     → FwakeDaichoServiceImpl.getFwakeHinbTonyuMaisu()
       → begin() 开启 DOMA 事务
       → VFwakeHinbanMaiSDao.getMaisu() → SQL 查询
       → VCommSyhninfoDao.getCommSyhninfo() → SQL 查询
       → commit()
       → 返回 Map<String, Object>
     → Jackson 序列化为 JSON
     → HTTP Response

4. $.ajax POST /LServer/md/fwakedaicho/FwakeDaichoKoma
   参数: hinban=12345&convSizeCds=07,09,11,13
                                        ↓
   GetFwakeDaichoResource
     → FwakeDaichoServiceImpl.getFwakeDaichoDatas()
       → begin()
       → TbdFwakeDaichoDao.getAllFwakeDaichoKomas(...)
         → DOMA 查找: META-INF/.../TbdFwakeDaichoDao/getAllFwakeDaichoKomas.sql
         → 执行 SQL, ResultSet → @Column 映射 → List<FwakeDaichoEntity>
       → 手动转换 Entity → List<FwakeRecordRowVO>
       → getKanrenFwakes(), getHsokInfo(), getSelectInfo() ...
       → commit()
       → 返回 Map<String, Object>

5. 前端收到 JSON → _setupFwakeDatas(data)
   → 存入全局变量 TBD_FWAKE_DAICHO_DATA
   → 发布 'productsearch' 事件

6. MiFwakeListView / KomaListView 监听事件
   → collection.reset(data)
   → 遍历 collection, 每个 model 创建 KomaView
   → KomaView.render() → Handlebars 模板 → HTML
   → DOM 渲染
```

---

## 四、开发注意事项

### 1. DOMA 编译期生成代码
- DAO 接口必须在编译期由 `doma-processor` 生成 `*Impl.class`
- Eclipse 需要配置 Annotation Processing（`.factorypath` 中包含 `doma-processor.jar`）
- 编译后可看到 `TbdFwakeDaichoDaoImpl` 等自动生成的类

### 2. Servlet 3.0 + 无 web.xml 路由
- 路由完全由 Jersey 注解驱动，`@Path` 定义 URL
- web.xml 只配置 Jersey Filter 和应用级参数

### 3. 前端单文件模式
- 所有 Backbone 代码、Handlebar 模板编译后合并到单个 `模块名.js`
- Grunt 任务：编译 Less → 预编译 Handlebars → 合并 JS

### 4. 全局变量数据管理
- 前端数据不存放在 Backbone Model 中，而是放在全局变量
- `datas.js` 中的 `App.Utils.DataSource` 既有 REST 调用、又有数据缓存
- 这是一种简化写法，适合快速开发但不易维护

### 5. 跨域支持
```java
return Response.ok(result)
    .header("Access-Control-Allow-Origin", "*")
    .header("Access-Control-Allow-Methods", "POST, GET")
    .header("Access-Control-Allow-Headers", "*")
    .build();
```

### 6. 编码
- Java 源文件：UTF-8
- 数据库连接：MS932 (Shift-JIS)
- 画面显示：MS932
- 日志：UTF-8

---

## 五、相关学习资源

| 技术 | 参考 |
|------|------|
| Jersey 2.x | https://eclipse-ee4j.github.io/jersey/ |
| Seasar DOMA | https://doma.seasar.org/ |
| Backbone.js | https://backbonejs.org/ |
| Handlebars | https://handlebarsjs.com/ |
| Grunt.js | https://gruntjs.com/ |
