# SmartLips — RakFW 架构与技术分析


---



---

## 1. 页面生成流程与技术分析

### 1.1 XPD / XWD / XDD 生成页面完整流程

```
浏览器 HTTP 请求
     │
     ▼
Lips{Module}PtnServlet (v30 PtnServlet)
     │ 继承关系：v30 PtnServlet → 自动加载 XML Engine
     │
     ▼
XML Engine 启动
     ├── 读取 XPD（Program Definition）
     │    ├── 确定 <program id>、数据源 (<datasource ref>)
     │    ├── 关联窗口 (<window ref>)
     │    └── 定义按钮事件、页面迁移规则
     │
     ├── 读取 XWD（Window Definition）
     │    ├── 解析 <window> 下的 <item>（输入框、下拉框等）
     │    ├── 解析 <table>（明细列表）
     │    └── 应用校验规则（必须、长度、类型）
     │
     ├── 读取 XDD（Data Dictionary）— 可选
     │    └── 全局数据项目定义、类型、默认值
     │
     ▼
     实例化 Plugin（PtnPlugin 子类）
     │  根据 XPD 中配置的 <plugin class="..."> 反射加载
     │  Plugin 覆盖基础行为：
     │  ├── setSql() → 构建动态 SQL
     │  ├── getValue() → 设定默认值
     │  ├── checkInsert/Update/Delete() → 业务校验
     │  └── getObj() → 画面定制
     │
     ▼
     执行 CRUD → DB（通过 LTableBase → PmsTableBase → JDBC）
     │
     ▼
     JSP 自动生成（v21 Engine）
     │  根据 XWD 定义 + Plugin 返回值渲染 HTML
     │  g_pp.g_nextPage 控制跳转目标：
     │  10=入力, 15=确认, 16=结果, 20=照会条件, 21=照会一覧, 22=照会详细
     │
     ▼
     响应返回浏览器
```

### 1.2 与 Spring MVC 的核心区别

Spring MVC 是代码定义路由（`@RequestMapping`），而 RakFW 是 **XML 定义即画面**。一个画面 = 1 个 XPD + 1 个 XWD + 0~1 个 Plugin Java 类，无需写 Controller。

### 1.3 g\_ssp 的作用与优缺点

`g_ssp` = `SeiServletParam`（v21），是整个系统的"上帝对象"。

```java
g_ssp.g_gp       // SeiGetParam — HTTP 请求参数
g_ssp.g_con      // Connection — DB 连接
g_ssp.g_session  // HttpSession
g_ssp.g_req      // HttpServletRequest
g_ssp.g_res      // HttpServletResponse
g_ssp.g_url3     // 上下文路径（context path）
g_ssp.g_class2   // 类名（日志用）
g_ssp.g_user     // 登录用户
g_ssp.getMessage() // 消息/错误信息
g_ssp.commit()
g_ssp.rollback()
```

**优点：**
- 任何地方都能拿到请求上下文 —— Plugin、Table、Util 等无需参数传递
- 降低了方法签名复杂度（不需要层层传 HttpServletRequest）
- 与 XML Engine 深度集成，框架内部自动填充

**缺点：**
- **强耦合** —— 所有业务代码直接依赖 SeiServletParam，脱离 Web 容器无法测试
- **全局可变状态** —— 多线程环境下如果误用可能造成数据错乱
- **无单元测试可行性** —— 需要 mock 整个 g_ssp 才能测试一个 Plugin
- **DI 不可用** —— 没有依赖注入，无法替换实现

### 1.4 画面迁移控制机制

与传统 MVC（Spring MVC 的 `return "redirect:/xxx"` 或 forward）不同，RakFW 的迁移是在 **同一个 Servlet 内** 通过 `g_pp.g_nextPage` 控制：

```java
public int getPP(PtnParam x_pp) {
    g_ssp = x_pp.g_ssp;
    g_pp = x_pp;

    if (保存ボタン押下) {
        g_pp.g_nextPage = 15; // → 确认画面
    } else if (确认后) {
        g_pp.g_nextPage = 16; // → 结果画面
    }
}
```

| 值 | 模式 | 说明 |
|----|------|------|
| 10 | 入力 | 新增/编辑输入画面 |
| 15 | 确认 | 输入确认画面 |
| 16 | 结果 | 处理结果画面 |
| 20 | 照会条件 | 查询条件输入 |
| 21 | 照会一覧 | 查询结果列表 |
| 22 | 照会详细 | 单条记录详情 |

**关键点：** 插件方法不需要 `return`，框架在 `getPP()` 执行完毕后自动读取 `g_pp.g_nextPage` 跳转。这意味着整个画面流都在一个方法内完成。

### 1.5 v21 → v30 迁移的兼容性问题与废弃 API

v30 只提取了骨架层，大量 v21 API 在 v30 中 **不存在**：

| 废弃/缺失的 v21 API | v30 替代 | 实际影响 |
|---------------------|----------|----------|
| `PtnPlugin` | 无 | ⚠️ 427 个 Plugin 类无法编译 |
| `PmsTableBase` | 无 | ⚠️ 4056 个 Table 类无法编译 |
| `SeiServletParam` | 无 | ⚠️ 13123 处引用报错 |
| `SeiUtil.isEmpty2()` | 无 | ⚠️ 必须自行实现 |
| `SeiCalc.add/sub/compare` | 无 | ⚠️ 数值计算需重写 |
| `SeiType.editComma/removeComma` | 无 | ⚠️ 金额格式化需重写 |
| `PmsSql / SeiSql` | 无 | ⚠️ SQL 构建需重写 |
| `SeiHtmlWriter` | 无 | ⚠️ HTML 渲染需重写 |
| `PmsDDPlugin` | 无 | ⚠️ DD 插件无法工作 |

**实际迁移策略：** 仅替换了 Servlet 层（v30 的 `PtnServlet`、`SeiServlet`），业务层全部保留 v21。注释 `/* RAKFW3 VERSION UP 2015/01/15 ISID tanaka */` 验证了这一点。

### 1.6 为什么 v30 只保留 Servlet/Project 基底？

这不是纯粹的"设计决策"，而是 **渐进式迁移的现实选择**。

| 因素 | 说明 |
|------|------|
| **风险控制** | 替换 4000+ 个 Table 类和 427 个 Plugin 类风险极大，先替换入口层（Servlet）验证兼容性 |
| **v21 的 License 问题** | SeiLicense21.xml 的存在说明 v21 需要独立许可证，v30 可能开放了新许可 |
| **Servlet API 升级** | 从 javax.servlet → Jakarta Servlet 4.0，Servlet 层必须升级 |
| **保持业务不变** | v21 的业务逻辑成熟稳定，重写代价高且收益低 |

**影响：** 新开发者需要同时掌握两套 API —— 继承用 v30，业务逻辑用 v21。

### 1.7 9 个数据源的数据一致性

从 `SeiDataSource.xml` 分析，9 个数据源实际上只有 3 个独立 Schema：

| Schema | 数据源 | 特性 |
|--------|--------|------|
| `pslips` | SmartLips, LipsPsDataSource | 主应用数据 |
| `rak` | LipsRakDataSource, LipsCrakDataSource, LipsAsrakDataSource | 框架数据 |
| `mdlips` | LipsMdDataSource | 商品数据 |
| `stlips` | LipsStDataSource | 在库数据 |
| `db_adm` | LipsAdmDataSource | 管理数据 |
| `jcrak` | LipsJcrakDataSource | 追加数据 |

**一致性保证方式：**

- **XA 分布式事务未使用** —— 无 JTA 配置，实际依赖业务补偿
- **应用层协调** —— 通常一个画面只操作一个数据源（如商品画面只用 LipsMdDataSource）
- **跨数据源操作** —— 通过 Plugin 中的手动 commit/rollback，如先更新 rak 再更新 pslips
- **RakFW 的机制** —— g_ssp.g_con 只对应一个数据源，切换数据源需额外获取 Connection

**潜在风险：** 跨数据源操作没有两阶段提交，宕机时可能出现数据不一致。

### 1.8 JA16SJIS vs UTF-8 编码转换处理

**配置定义：**

```xml
<CodeSet>MS932</CodeSet>                          <!-- 数据库连接参数 -->
<Param Name="Log.Encoding">UTF-8</Param>           <!-- 日志输出 -->
<Param Name="PMS.FileEncoding">UTF-8</Param>       <!-- 文件编码 -->
```

**实际编码流转：**

```
Java 源码 (UTF-8)
    │
    ▼ 编译为 class (UTF-8)
    │
    ▼ RakFW 内部 → SeiEncodingConv 自动转换
    │
    ├── SQL 发送到 DB → MS932 (Shift-JIS)  ← 数据库字符集
    │
    ├── 画面HTML 输出 → MS932 ← 浏览器渲染
    │    <meta charset="Shift_JIS">
    │
    └── 日志输出 → UTF-8 ← Log.Encoding
```

**常见问题：**
- Java 字符串在内存中是 UTF-16，写入 DB 时自动转 MS932
- 如果直接拼接 SQL（非绑定变量），日文汉字在两端编码不一致时会出现 **文字化け**（乱码）
- v21 的 `SeiEncodingConv` 类承担了大部分转换工作

### 1.9 定位慢 SQL 的方法

**配置中已开启的跟踪：**

```xml
<Param Name="Trace.SQL">true</Param>  <!-- 慢 SQL 跟踪 -->
<Param Name="Trace">true</Param>      <!-- 全局跟踪 -->
```

**实际排错方法：**

| 方法 | 说明 |
|------|------|
| **查看日志** | Trace.SQL 会输出所有 SQL 执行时间和 SQL 文本到 WEB-INF/RakAppl/log/ |
| **g_ssp.g_class2** | 日志中会记录是哪个类执行的 SQL，方便定位 |
| **RakFW 框架自身** | 如果 SQL 执行超过阈值，日志中会标记 [SLOW SQL] |
| **数据库端** | Oracle 的 AWR / ASH 报告，或者 v$sql 视图监控 |
| **XPD 中 SQL 定义** | 查看 XML 中的 Select/From/Where 确认索引使用情况 |

### 1.10 无构建工具如何保证可重复性

| 方式 | 说明 |
|------|------|
| .classpath + .project | Eclipse 项目文件版本管理，确保 IDE 配置一致 |
| .tomcatplugin | Sysdeo Tomcat Plugin 配置（TOMCAT_HOME=E:/RakRakTest/tomcat/tomcat9.0.48） |
| lib/ 目录下所有 JAR 在 SVN 中 | 不依赖 Maven 仓库，JAR 版本锁定 |
| SVN 版本管理 | 整个项目目录（含编译输出？）受版本控制 |
| 手工流程 | Eclipse → 右键 → Run on Server |

**风险：** 依赖开发者本地 Eclipse 配置，无 CI/CD，新人搭建环境困难。没有依赖传递管理（例如 JDBC 驱动版本升级需要手动替换 JAR）。

### 1.11 setSql() 动态 SQL 与 SQL 注入防护

```java
public int setSql(PtnParam x_pp) {
    g_ssp = x_pp.g_ssp;
    g_pp = x_pp;

    PmsSql x_sql = new PmsSql();
    x_sql.setSel("SELECT t.* FROM tbm_syhn t");
    x_sql.setWhere("t.syhn_cd = ?");                  // ← 绑定变量 ?
    x_sql.addWhereValue(g_ssp.g_gp.get("syhn_cd"));   // ← 值单独传入

    // ❌ 错误做法：字符串拼接
    // x_sql.setWhere("t.syhn_cd = '" + g_ssp.g_gp.get("syhn_cd") + "'");

    return 0;
}
```

**防护原理：**
- RakFW 的 PmsSql/PmsTable 内部使用 PreparedStatement
- ? 占位符 + addWhereValue() 确保参数被正确转义
- **禁止字符串拼接 SQL** —— 这是框架层面的约束

### 1.12 开发一个新画面的步骤顺序

| 步骤 | 产物 | 说明 |
|------|------|------|
| 1 | **XDD**（数据字典） | 定义数据项目和类型（如有新字段） |
| 2 | **XWD**（画面定义） | 定义窗口布局、item、table（画面外观） |
| 3 | **XPD**（程序定义） | 定义程序、数据源、窗口关联（程序入口） |
| 4 | **Plugin Java** | 继承 PtnPlugin，覆盖方法（业务逻辑） |
| 5 | **LTableBase 子类** | ORM 表映射（如需新表操作） |
| 6 | **JavaScript** | script/{module}/ 下（客户端逻辑） |
| 7 | **JSP** | app/{module}/ 下（自定义视图，较少用） |

> **XML 配置比 Java 代码更重要** —— 没有 XPD/XWD，画面根本无法启动。

### 1.13 查询结果为空如何排查

```java
1. 确认 g_nextPage 的值
   g_pp.g_nextPage = 20 → 照会条件画面（SQL 可能没执行）
   g_pp.g_nextPage = 21 → 照会一覧（SQL 执行了但结果为空）

2. 检查 SQL 构造（setSql 方法）
   - 确认绑定变量是否正确传递
   - 检查 WHERE 条件是否过严
   - 查看 Trace.SQL 日志输出的实际 SQL

3. 检查数据源连接
   - 确认 XPD 中的 <datasource ref> 指向正确的数据源
   - 确认表属于该 schema

4. 检查权限
   - v$sql 验证 SQL 是否真的执行了
   - ALL_TABLES 确认表存在且有 SELECT 权限
```

### 1.14 LTableBase 自动字段问题

```java
mk_date   // 作成日（INSERT 时）
mk_term   // 作成端末（INSERT 时）
mk_user   // 作成ユーザ（INSERT 时）
upd_date  // 更新日（UPDATE 时）
upd_term  // 更新端末（UPDATE 时）
upd_user  // 更新ユーザ（UPDATE 时）
```

**批量更新时的典型问题：**

| 问题 | 说明 |
|------|------|
| **mk_date 被覆写** | 批量 UPDATE 时如果误用 PmsTable.update()，会把全量记录的 mk_date 改成当前时间 |
| **upd_user 不准确** | バッチ处理时 g_ssp.g_user 可能是系统用户（SYSTEM/BATCH），丢失了实际操作人 |
| **性能开销** | 大量 INSERT/UPDATE 每次都额外设置 6 个字段，对批量导入场景有影响 |
| **时区问题** | SYSDATE 取的是 DB 服务器时间，与应用服务器时间可能不一致 |
| **排他冲突** | 不同端末同时更新同一条记录时，upd_term 会被最后提交者覆盖 |

### 1.15 结论：仅使用 RakFW v30 能否运行？

**绝对不行。**

| 指标 | RakFW v21 | RakFW v30 | 结论 |
|------|-----------|-----------|------|
| Java 引用数 | 14,004 | 66 | v30 只有 v21 的 0.5% |
| 类数量（JAR内） | 3,723 | 341 | v30 只有 v21 的 9% |
| Plugin 基类 | PtnPlugin（427子类） | ❌ 无 | 所有 Plugin 无法编译 |
| ORM 基类 | PmsTableBase → LTableBase（4056子类） | ❌ 无 | 所有 Table 类无法编译 |
| 全局上下文 | SeiServletParam（g_ssp，13123引用） | ❌ 无 | 13123处编译错误 |
| 工具类 | SeiUtil, SeiCalc, SeiType, SeiHtmlWriter... | ❌ 无 | 整个 common 层报错 |
| SQL 构建 | PmsSql, SeiSql | ❌ 无 | 无法执行数据库查询 |
| 画面渲染 | XML Engine + JSP Generator | ❌ 无 | 无法生成页面 |

**改造代价估算：** 重写 427 个 Plugin 类、4056 个 Table 类、实现 SeiServletParam 替代品（13123个引用点）、实现 SQL Builder、重新实现 JSP 自动生成引擎、重新实现数值计算/格式化工具。

**最终结论：** 在当前架构下，**RakFW v30 必须和 v21 共存，缺一不可**。v30 只承担了 66 个引用点的 Servlet 骨架角色，而 v21 承载了 14,004 个引用点的完整业务逻辑。移除 v21 ≈ 系统重写。

---

## 2. v30 基层 Servlet 及作用

v30 仅提供了 4 种核心 Servlet 基类，共 66 处引用（vs v21 的 14,004 处）。

### 2.1 4 大基层 Servlet

| v30 基类 | 被继承的模块 Servlet | URL Pattern |
|----------|---------------------|-------------|
| `SeiServlet` | Lips{Module}Servlet（12个模块） | /Lips{Module}Servlet/* |
| `PtnServlet` | Lips{Module}PtnServlet（12个模块） | /Lips{Module}PtnServlet/* |
| `PtnWinServlet` | Lips{Module}WinServlet（12个模块） | /Lips{Module}WinServlet/* |
| `PtnMenuServlet` | Lips{Module}MenuServlet（12个模块） | /Lips{Module}MenuServlet/* |

**例外：** pc（生产管理）模块是唯一一个全部 4 个 Servlet 仍使用 v21 的模块，未迁移到 v30。

### 2.2 各 Servlet 的职责详解

**SeiServlet** — 通用请求入口
- URL: /LipsStServlet/*
- 用途：非画面模式的请求处理 — 直接 HTTP 请求（非 XPD 驱动的请求）、文件下载、简单响应
- 使用频率：极少，大部分请求走 PtnServlet

```java
public class LipsStServlet extends SeiServlet {
    // 由 v30 框架处理，几乎不写额外代码
}
```

**PtnServlet** — 最核心的 Servlet（画面模式引擎）
- URL: /LipsStPtnServlet/*
- 用途：90% 的业务画面由此处理 — 接受 HTTP → 加载 XPD → 调用 Plugin → 执行 SQL → 渲染 JSP

```java
public class LipsStPtnServlet extends PtnServlet {
    // v30 的 PtnServlet 会自动：
    // 1. 解析 URL 确定 XPD 文件
    // 2. 加载 XML Engine (v21)
    // 3. 实例化对应的 Plugin（PtnPlugin 子类，v21）
    // 4. 执行 Plugin.getPP() → setSql() → DB 查询
    // 5. 根据 g_pp.g_nextPage 渲染 JSP
    g_proj = new LipsStProject();
}
```

控制流：
```
/PtnServlet/xxx
    ↓
v30 PtnServlet 接收请求
    ↓
委托给 v21 XML Engine（XPD 解析）
    ↓
Plugin (v21 PtnPlugin) 执行业务逻辑
    ↓
返回 HTML（v21 JSP 自动生成引擎）
```

**PtnWinServlet** — 弹出子窗口
- URL: /LipsStWinServlet/*
- 用途：弹窗式的子画面 — 商品选择弹窗、日期选择器、辅助输入画面
- 功能与 PtnServlet 相同，但独立 URL，可在弹窗中打开

**PtnMenuServlet** — 模块菜单
- URL: /LipsStMenuServlet/*
- 用途：每个模块的菜单入口 — 读取 XMD（Menu Definition）、显示功能列表树、点击菜单项跳转到对应 PtnServlet/xxx

### 2.3 NOT v30 的部分（仍用 v21）

| Servlet 类型 | 使用的基类 | 说明 |
|-------------|-----------|------|
| FileDnDServlet（13个模块全量） | PmsFileDndServlet (v21) | 文件拖拽上传 — v30 未提供替代品 |

### 2.4 层次关系图

```
Tomcat
  │
  ├── web.xml 定义 70+ Servlet 映射
  │
  ├── v30 层（骨架，66 引用）
  │     ├── SeiServlet          → 通用入口
  │     ├── PtnServlet          → 核心画面引擎（最关键）
  │     ├── PtnWinServlet       → 弹窗
  │     ├── PtnMenuServlet      → 菜单
  │     ├── PtnComponent        → 组件注入（少量 Plugin 使用）
  │     ├── PmsActionDownload   → 下载动作
  │     ├── PmsActionWbExecute  → Web 执行
  │     ├── PmsActionFile       → 文件操作
  │     └── SeiProject          → LProject 基类
  │
  └── v21 层（业务引擎，14,004 引用）
        ├── PtnPlugin           → 427 个 Plugin
        ├── PmsTableBase        → 4056 个 Table 类
        ├── SeiServletParam     → g_ssp（13123 引用）
        ├── PmsFormValue        → g_pp.g_fv
        ├── PmsSql              → SQL 构建
        ├── SeiUtil / SeiCalc   → 工具类
        ├── XML Engine          → XPD/XWD/XDD 解析
        ├── JSP Generator       → 自动渲染
        └── SeiEncodingConv     → MS932 编码
```

---

## 3. v21 vs v30 详细对比

> 基于 SmartLips 项目实际使用的 RakFW21_14.jar / RakFW30_14.jar 字节码反编译分析

### 3.1 继承关系

```
v30 PtnServlet → extends → v21 PtnServlet
v30 SeiServlet → extends → v21 SeiServlet
```

v30 **不是重写**，而是在 v21 上面叠加功能。

### 3.2 类职责对比

**Servlet 层：**

| 类 | v21 | v30 |
|----|-----|-----|
| SeiServlet | 基础 Servlet。SeiMain() 处理请求路由 | 继承 v21。增加 RakStudio 帧布局。当 g_class2 == "rakstudio" 时生成三栏框架页面（导航 + 主区 + 指令栏），否则调 super.SeiMain() |
| PtnServlet | 完整 PtnServlet。getPattern() / printPmsWeb() / printManager() / printDevelMenu() / initAppl2() / needCheckAuthority() 等全部实现在这里 | 继承 v21。在所有关键方法上叠加：RakStudio 菜单注入 + 权限增强 + 新的 XPR 解析器优先 |

**XML 解析层：**

| 组件 | v21 | v30 |
|------|-----|-----|
| XPD 解析器 | PtnXPD（自包含 SAX 解析器） | PtnXPR（高层入口）+ PtnXPRParser（继承 v21 的 PtnXPDParser）|
| XPD 根元素 | `<XPD>` | `<SeiProgramDefinition>`（此外 XPRParser 也兼容 `<XPD>`）|
| 数据模型 | PtnParamSet → PtnParam（扁平） | PtnProgDef → PtnPageCtrl → PtnPage（层级）|
| 程序缓存 | g_pps: Hashtable<String, PtnParamSet> | g_pg: Hashtable<String, PtnProgDef> |
| 兼容机制 | 无 | PtnProgDef.g_legacyPPS 存储 v21 的 PtnParamSet |

**ORM 层：**

| 组件 | v21 | v30 |
|------|-----|-----|
| ORM 基类 | PmsTableBase | **无**（项目通过 LTableBase extends PmsTableBase 继续使用 v21）|
| SQL 执行 | PmsSql / SeiSql | **无**（继续使用 v21）|
| 表单值 | PmsFormValue / PmsAryValue | **无**（继续使用 v21）|

### 3.3 每个重写方法的详细差异

**initAppl2(SeiServletParam)** — 启动初始化

```java
// v21: 基础初始化
protected void initAppl2(SeiServletParam x_ssp) {
    // 加载模块配置、初始化数据源、注册程序等
}

// v30: 先调 v21，再注册 RakStudio
protected void initAppl2(SeiServletParam x_ssp) {
    super.initAppl2(x_ssp);  // ← v21 的初始化先执行
    // v30 额外：反射加载 RakStudio 项目
    try {
        g_rakStudioProject = Class.forName("jp.co.sei.RakStudio.rktc.rktcProject").newInstance();
        PtnBridgeUtil.addDefaultProject(x_ssp, this, rktaProject, "jp.co.sei.RakStudio");
        PtnBridgeUtil.addDefaultProject(x_ssp, this, rktbProject, "jp.co.sei.RakStudio");
        PtnBridgeUtil.addDefaultProject(x_ssp, this, rktcProject, "jp.co.sei.RakStudio");
    } catch (Exception e) { /* 忽略 - 没装RakStudio不影响 */ }
}
```

**getPattern(SeiServletParam, PtnParam)** — 核心：解析 XPD 获取程序定义

这是 **v21 和 v30 最核心的功能差异点**。

```java
// v21: 直接读 PtnXPD
protected PtnBase getPattern(SeiServletParam x_ssp, PtnParam x_pp) {
    String progName = x_ssp.g_class2;
    PtnParamSet pps = g_xpd.getParamSet(progName);
    if (pps == null) return null;
    return new PtnBase(x_pp).addPattern(pps.createPattern(x_pp));
}

// v30: 优先用 v30 的 XPR，找不到再 fallback 到 v21
protected PtnBase getPattern(SeiServletParam x_ssp, PtnParam x_pp) {
    // 步骤 1: 先用 v30 的 XPR 解析器
    PtnXPR xpr = getXPR(x_ssp);
    PtnBase result = xpr.getProgram(x_ssp, x_pp, x_ssp.g_class2);
    if (result != null) return result;  // v30 找到 → 直接返回

    // 步骤 2: 如果是 RakDesigner 开发模式，追加 RakStudio 菜单
    if ("rakdesigner_making".equals(x_ssp.g_class2)
        && "rakstudio".equals(x_ssp.g_gp.getParam("rktd_application"))) {
        // 反射调用 getFilteredSSP、addRakStudioMenu()
    }

    // 步骤 3: fallback → 调 v21 的 getPattern
    return super.getPattern(x_ssp, x_pp);
}
```

流程对比：
```
v21: 请求 → PtnXPD 读 XPD → PtnParamSet → PtnBase → 执行
v30: 请求 → PtnXPR.getProgram() → 查 PtnProgDef 缓存
                                    ├─ 有 → 从 PtnProgDef 构建 PtnBase
                                    ├─ 没有 → PtnXPRParser 解析 XPD
                                    │           ├─ <SeiProgramDefinition> → v30 格式
                                    │           └─ <XPD> → super.parse() → v21 的 PtnXPDParser
                                    └─ 找不到 → super.getPattern() → v21 的 PtnXPD
```

**getXPR(SeiServletParam)** — v30 新增方法

```java
protected PtnXPR getXPR(SeiServletParam x_ssp) {
    return PtnXPR.getInstance(x_ssp);
}
```

PtnXPR 是一个带缓存和热加载的 XPD 读取器：
- 内部持有 g_xpd: PtnXPD（v21 的 fallback 解析器）
- 缓存 g_pg: Hashtable<String, PtnProgDef>
- 10 秒间隔检测文件修改时间
- 支持 File 和 URL 两种来源

**printPmsWeb(SeiServletParam)** — PMS 页面输出

```java
// v21: 直接渲染
protected void printPmsWeb(SeiServletParam x_ssp) {
    // 渲染 PMS 管理页面
}

// v30: 先检查权限和注入菜单，再调 v21
protected void printPmsWeb(SeiServletParam x_ssp) {
    if (!checkAuthManager(x_ssp)) {
        SeiSecurity.log(x_ssp, "ACCESS", "illegal pms access");
        x_ssp.g_proj.errorProc(x_ssp, 5);
        return;
    }
    addRakStudioMenu(x_ssp);
    super.printPmsWeb(x_ssp);  // 实际渲染在 v21
}
```

**addRakStudioMenu(SeiServletParam)** — v30 新增

```java
private void addRakStudioMenu(SeiServletParam x_ssp) {
    if (g_rakStudioProject == null) return;
    // 1. 反射加载 RakStudio 菜单画面类
    Class cls = Class.forName("jp.co.sei.RakStudio.rkta.ptn.rktaPtn11010.rktaScr1101011");
    // 2. 克隆 SSP 并修改 g_class2
    SeiServletParam filteredSSP = x_ssp.getClone();
    filteredSSP.g_class2 = "rktc" + x_ssp.g_class2;
    // 3. 反射调用 getFilteredSSP
    Method m = g_rakStudioProject.getClass().getMethod("getFilteredSSP", SeiServletParam.class);
    filteredSSP = m.invoke(g_rakStudioProject, filteredSSP);
    // 4. 创建 PtnParam → 调用 getMenu() → 注入到页面
    Constructor c = cls.getConstructor(PtnParam.class);
    Object menuScreen = c.newInstance(new PtnParam(filteredSSP));
    menuScreen.getClass().getMethod("getMenu").invoke(menuScreen);
    // 5. 传递 titleHeader
    x_ssp.setAttribute("jp.co.sei.is.lib21.page.titleHeader",
        filteredSSP.getAttribute("jp.co.sei.is.lib21.page.titleHeader"));
}
```

**printDevelMenu(SeiServletParam, SeiHtmlWriter, String)** — 调试工具栏

v30 完全重写了这个方法的 HTML 生成逻辑，绘制一个调试工具栏：

```
┌──────────────────────────────────────────────────────────────┐
│  Debug ON │ Debug OFF │ Dev ON │ Dev OFF │ [JP] │ [EN] │ ... │
└──────────────────────────────────────────────────────────────┘
```

- Debug ON/OFF: 切换 g_ss.g_debug 的 bit 0（显示 SQL、参数等调试信息）
- Dev ON/OFF: 切换 g_ss.g_debug 的 bit 17（开发者模式）
- 语言切换: 显示所有已定义语言的切换链接
- 样式：黄色背景选中态、灰色背景未选中态

**needCheckAuthority(SeiServletParam, String)** — 权限检查

```java
// v21: 基础检查
protected boolean needCheckAuthority(SeiServletParam x_ssp, String x_class2) {
    // 按默认规则检查权限
}

// v30: RakStudio 程序免检查
protected boolean needCheckAuthority(SeiServletParam x_ssp, String x_class2) {
    if (g_rakStudioProject != null && x_class2.startsWith("rktc"))
        return false;
    return super.needCheckAuthority(x_ssp, x_class2);
}
```

### 3.4 v30 SeiServlet 的额外功能

**SeiMain — RakStudio 帧布局**

当请求的 g_class2 == "rakstudio" 时，生成一个三栏框架页面：

```
┌──────────────────┬──────────────────────────────────┐
│  导航栏 (navi)    │                                  │
│  (300px)          │  主画面 (main)                    │
│                   │                                  │
│                   ├──────────────────────────────────┤
│                   │  指令栏 (mount / instruction)     │
│                   │  (150px)                         │
├──────────────────┴──────────────────────────────────┤
│  虚拟帧 (dummy, 0~40px, 用于 iframe hack)             │
└─────────────────────────────────────────────────────┘
```

支持三种浏览器的不同帧渲染：IE（frameborder=1, framespacing=2）、其他（border=1）、调试模式。

**URL 生成：**

```java
getMainURL()   → /Lips{Module}PtnServlet/rktcMain?ss=xxx
getNaviURL()   → /Lips{Module}PtnServlet/rktc11010?np=100&ss=xxx
getMountURL()  → /Lips{Module}PtnServlet/rktc23010?np=100&ss=xxx
```

### 3.5 v30 PtnXPR 详细解析

**架构：**

```
PtnXPR (extends SeiObject)
├── 构造函数(application, package, sub)
│   ├── 创建 g_xpd = new PtnXPD(application, package, sub)  ← v21 解析器
│   ├── 创建 g_chkint = new SeiInterval(10000ms)             ← 10秒检测
│   └── 创建 g_pg = new Hashtable()                          ← 程序缓存
│
├── getInstance(ssp) → 单例工厂
│   └── 以 "application#package#sub" 为 key 缓存实例
│
├── getProgram(ssp, pp, name) → PtnBase
│   ├── check(ssp) → 检测文件变更，必要时重新解析
│   ├── 从 g_pg 查 PtnProgDef
│   └── PtnProgDef.create(pp) → PtnBase
│
├── getParam(ssp, name, np) → PtnParam
│   ├── 从 g_pg 查 PtnProgDef
│   ├── 如果有 g_legacyPPS → PtnParamSet.getPP(ssp, np)  ← v21 fallback
│   ├── 如果有 PtnPageCtrl → getPageCtrl.getPage(np)      ← v30 新格式
│   └── 标题/默认页面/插件名等设置
│
└── check(ssp) → boolean
    ├── g_chkint.checkInterval() 跳过检测则直接返回
    ├── PmsUtil.getXMLFiles() 获取所有 XPD 文件
    ├── PtnXPRParser.parse(file) 解析变更的文件
    └── 更新 g_lastmod 和 g_lastmod_file
```

**PtnXPRParser 继承链：**

```
PtnXPDParser (v21, 包私有?)
  └── PtnXPRParser (v30)
```

PtnXPRParser 继承 v21 的 PtnXPDParser，新增的解析能力：

| v30 解析器字段 | 对应 XML 元素 | 说明 |
|----------------|---------------|------|
| g_pageCtrl: PtnPageCtrl | `<PageCtrl>` | 页面控制定义（页面跳转、权限）|
| g_page: PtnPage | `<Page>` | 单个页面定义（字段、布局）|
| g_updrule: PtnUpdateRule | `<UpdateRule>` | 更新规则定义 |
| g_rule: PtnUpdateRule.Rule | `<Rule>` | 单条规则 |
| g_updtbl: PtnUpdateRule.UpdateTable | `<UpdateTable>` | 更新表定义 |
| g_part: PtnHtmlPart | `<HtmlPart>` | HTML 部件（header/footer）|
| g_logic: PmsLogicObject | `<Logic>` | 业务逻辑块 |
| g_menuLogic: PtnLogicList | `<MenuLogic>` | 菜单逻辑 |
| g_ddoption: Hashtable | `<DDOption>` | 数据字典选项 |
| g_ddLogicList: ArrayList | `<DDLogic>` | 数据字典逻辑列表 |
| g_common: Hashtable | `<Common>` | 公共定义 |
| g_commonMenu: Vector | `<CommonMenu>` | 公共菜单 |
| g_commonSubMenu: Vector | `<CommonSubMenu>` | 公共子菜单 |

**PtnProgDef 数据模型：**

```java
PtnProgDef (extends SeiObject)
├── g_name: String                    // 程序名
├── g_title: String[]                 // 标题（多语言）
├── g_list: boolean                   // 是否列表模式
├── g_funcGroup: String              // 功能组
├── g_desc: Vector                   // 描述
├── g_crud: Vector                   // CRUD 权限
├── g_defaultPage: int               // 默认页面 (-1 = 未设置)
├── g_defaultWinPage: int            // 默认窗口页面 (-2)
├── g_defaultBatPage: int            // 默认批处理页面 (-3)
├── g_commit: boolean                // 是否自动提交
├── g_pagectrl: List<PtnPageCtrl>    // 页面控制列表 (v30新)
├── g_commonPage: Map                // 公共页面 (v30新)
├── g_commonMenu: List               // 公共菜单 (v30新)
├── g_commonSubMenu: List            // 公共子菜单 (v30新)
├── g_sql: Hashtable                 // SQL 定义
├── g_updrule: Hashtable             // 更新规则 (v30新)
├── g_ddoption: Hashtable            // DD 选项 (v30新)
├── g_legacyPPS: PtnParamSet         // ← v21 兼容: 旧格式的 ParamSet
├── g_pluginnms: ArrayList           // Plugin 名称列表
├── g_default: PtnDefaultXPR         // 默认配置
└── g_file: File                     // XPD 源文件
```

**PtnPageCtrl / PtnPage：**

```java
PtnPageCtrl (extends SeiObject, implements Cloneable)
├── g_name: String                   // 名称
├── g_stdPtn: String                 // 标准 Pattern (如 PtnUpdRAC)
├── g_valid: boolean                 // 是否有效
├── g_progDef: PtnProgDef           // 所属程序定义
├── g_pagetran: List<PtnPage>       // 页面列表
├── g_menu: List                    // 菜单列表
├── g_submenu: List                 // 子菜单列表
├── g_loopcnt: int                  // 循环计数
└── g_loopPage: int[]               // 循环页面

PtnPage
├── 页面编号
├── 执行选项 (Option)
├── 插件名列表
└── 页面特定配置
```

### 3.6 实际运行时的流程对比

**v21 处理流程：**

```
HTTP GET /LipsMdPtnServlet/EntryCal?np=10

1. LipsMdPtnServlet (v30, 继承 v21)
2.   → SeiMain() → 路由到 PtnServlet 处理
3.     → getPP(ssp, "EntryCal", 10) → 调用 Plugin.getPP()
4.       → getPattern(ssp, pp)
5.         → PtnXPD.getParamSet("EntryCal") → PtnParamSet
6.           → PtnParamSet.getPP(ssp, 10) → PtnParam
7.             → PtnBase.exec() → 执行 SQL → 渲染 HTML
```

**v30 处理流程：**

```
HTTP GET /LipsMdPtnServlet/EntryCal?np=10

1. LipsMdPtnServlet (v30)
2.   → SeiMain() → 路由到 PtnServlet 处理
3.     → getPP(ssp, "EntryCal", 10) → 调用 Plugin.getPP()
4.       → getPattern(ssp, pp)        ← v30 覆写
5.         → getXPR(ssp) → PtnXPR.getInstance(ssp)
6.           → PtnXPR.getProgram(ssp, pp, "EntryCal")
7.             → check(ssp) → 检测文件变更
8.             → 从 g_pg 查 PtnProgDef
9.               → g_legacyPPS.getPP(ssp, 10) → PtnParam  ← v21 fallback
10.                → PtnBase.exec() → 执行 SQL → 渲染 HTML
11.         → addRakStudioMenu(ssp)    ← v30 新增
12.         → super.getPattern(ssp, pp) ← 如果 v30 没找到
```

**关键观察：步骤 9 中 g_legacyPPS.getPP() 执行的是 v21 的 PtnParamSet.getPP()，即底层 XML Engine 和 ORM 仍然是 v21。**

### 3.7 项目中实际使用的 API 分布

| API 类别 | 包 | 引用数 | 说明 |
|----------|-----|--------|------|
| Servlet 基类 | jp.co.sei.is.lib30.pms.ptn.PtnServlet | 12/13 模块 | 仅 pc 模块未迁移到 v30 |
| 项目基类 | jp.co.sei.is.lib30.SeiProject | 1 (LProject) | 仅 LProject 引用 |
| **所有业务 API** | jp.co.sei.is.lib21.* | **14000+** | Plugin/ORM/SQL/工具类全部是 v21 |

---

## 总结

| 层面 | 实际执行者 |
|------|-----------|
| Servlet 入口 | **v30**（继承 v21，叠加 RakStudio + 权限检查）|
| XPD 解析 | **v30 先尝试，v21 兜底**（项目所有 XPD 文件都是 v21 格式 → 全部走 v21 fallback）|
| XML Engine | **v21**（PtnBase.exec() / PtnParam.setSql() 等）|
| ORM | **v21**（PmsTableBase / LTableBase）|
| Plugin 执行 | **v21**（PtnPlugin / PtnParam / PmsFormValue）|
| SQL 执行 | **v21**（PmsSql / SeiSql）|
| HTML 生成 | **v21**（SeiHtmlWriter / SeiTag）|
| 工具类 | **v21**（SeiUtil / SeiType / SeiCalc / SeiConMgr）|
| 开发工具集成 | **v30**（RakStudio 菜单 / 调试工具栏 / 帧布局）|

RakFW v30 并不是对 v21 的重写，而是在 v21 之上叠加的一个薄骨架层。v30 提供了 4 个 Servlet 基类（SeiServlet、PtnServlet、PtnWinServlet、PtnMenuServlet）作为 HTTP 请求的接收入口，并额外集成了 RakStudio 开发工具（三栏帧布局、调试工具栏、菜单注入）和权限增强。但所有核心业务引擎——包括 XML Engine（XPD/XWD/XDD 解析）、427 个 Plugin 类、4056 个 Table ORM 类、SQL 构建（PmsSql/SeiSql）、HTML 渲染、数值计算/格式化工具（SeiUtil/SeiCalc/SeiType）以及全局上下文 SeiServletParam——全部是 v21。v30 在实际项目中仅占 66 处引用（0.5%），而 v21 占 14,004 处（99.5%）。v30 的 PtnServlet 继承 v21 的 PtnServlet，采用"v30 优先解析（PtnXPR），找不到则 fallback 到 v21"的策略，但由于项目所有 XPD 文件都是 v21 格式，实际全部走 v21 fallback 路径。**结论：v30 必须与 v21 共存，移除 v21 等价于系统重写。**
