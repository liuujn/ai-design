# Prompt 定义：/generate-backend

## 名称
generate_backend

## 描述
根据后端API设计书生成后端Java代码（Spring Boot + Oracle + MyBatis）

## 触发命令
/generate-backend

## 参照内容
- 后端API设计书：/docs/基本设计/API设计书/*
- 表定义：/docs/基本设计/表定义/*
- 已生成的代码示例：/docs/基本设计/后端代码/*

## 代码生成
用户提供需要生成的API名称（如"订单"），执行以下步骤：

### 第一步：读取设计文档
1. 读取对应的后端API设计书（`{API名}_API设计.md`），获取：
   - API功能列表（所有端点及CRUD类型）
   - 每个API的请求URL、方法、请求参数、响应体结构
   - 校验规则和错误消息
   - 业务处理逻辑
   - 数据库表定义和字段映射

2. 读取对应的表定义文件，确认各字段类型、长度、约束

### 第二步：生成后端代码
根据API设计书生成完整的后端Java代码，**严格遵循以下技术栈和规范**：

#### 技术栈
- Spring Boot 3.x
- Oracle 19c+
- MyBatis（XML配置）
- Maven

#### 项目包结构
```
com/example/{module}/
├── controller/
│   └── {Entity}Controller.java       # REST控制器
├── service/
│   ├── {Entity}Service.java          # 业务接口
│   └── impl/
│       └── {Entity}ServiceImpl.java  # 业务实现
├── mapper/
│   └── {Entity}Mapper.java           # MyBatis Mapper接口
├── model/
│   ├── entity/
│   │   ├── {Entity}.java             # 主表实体
│   │   └── {Entity}Item.java         # 明细表实体（如有）
│   ├── dto/
│   │   ├── request/
│   │   │   ├── {Entity}ListQuery.java      # 列表查询参数
│   │   │   ├── {Entity}CreateRequest.java  # 创建请求
│   │   │   ├── {Entity}UpdateRequest.java  # 更新请求
│   │   │   ├── {Entity}StatusRequest.java  # 状态变更请求
│   │   │   └── {Entity}DeleteRequest.java  # 删除请求
│   │   └── response/
│   │       ├── {Entity}ListVO.java         # 列表响应
│   │       ├── {Entity}DetailVO.java       # 详情响应
│   │       ├── {Entity}CreateVO.java       # 创建响应
│   │       ├── {Entity}UpdateVO.java       # 更新响应
│   │       └── PageResult.java             # 分页结果
│   └── enums/
│       └── {Entity}Status.java             # 状态枚举
├── config/
│   ├── WebMvcConfig.java           # WebMVC配置
│   ├── MyBatisConfig.java          # MyBatis配置
│   └── GlobalExceptionHandler.java # 全局异常处理
└── resources/
    └── mapper/
        └── {Entity}Mapper.xml      # MyBatis XML映射
```

#### 代码生成规范

##### 1. Entity 实体类
```java
@Data
@TableName("orders")  // Oracle表名
public class Order {
    private String id;
    private String orderNo;
    private String customerName;
    private BigDecimal totalAmount;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Boolean isDeleted;
}
```

- 使用 `@Data`（Lombok）
- 字段使用 **驼峰命名**，与表字段 **蛇形命名** 映射通过 MyBatis 配置完成
- 日期类型：`LocalDateTime`
- 金额类型：`BigDecimal`
- **校验注解选择（重要）**：
  - `String` 类型 → `@NotBlank`
  - `BigDecimal`/`Integer`/`Long`/`Boolean` 等数值类型 → `@NotNull`（不可用 `@NotBlank`/`@NotEmpty`)
  - `BigDecimal` 范围校验 → `@DecimalMin` / `@DecimalMax`
  - `Integer` 范围校验 → `@Min` / `@Max`
- Oracle字段类型映射：
  - `VARCHAR2(36)` → `String`
  - `NUMBER(12,2)` → `BigDecimal`
  - `NUMBER` → `Integer`/`Long`
  - `TIMESTAMP` → `LocalDateTime`
  - `CHAR(1)`/`NUMBER(1)` → `Boolean`

##### 2. Mapper 接口
```java
@Mapper
public interface OrderMapper {
    // 分页查询
    List<Order> selectPage(OrderListQuery query);
    Long count(OrderListQuery query);
    
    // 单笔查询
    Order selectById(@Param("id") String id);
    List<OrderItem> selectItemsByOrderId(@Param("orderId") String orderId);
    
    // 插入
    int insert(Order order);
    int insertItems(List<OrderItem> items);
    
    // 更新（乐观锁）
    int updateByIdAndUpdatedAt(Order order);  // WHERE id = #{id} AND updated_at = #{updatedAt}
    
    // 逻辑删除
    int logicDeleteByIdAndUpdatedAt(@Param("id") String id, @Param("updatedAt") LocalDateTime updatedAt);
    int logicDeleteItemsByOrderId(@Param("orderId") String orderId);
    
    // 状态更新
    int updateStatusByIdAndUpdatedAt(Order order);
    
    // 获取更新时间
    LocalDateTime selectUpdatedAtById(@Param("id") String id);
}
```

##### 3. Mapper XML（Oracle语法）
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.order.mapper.OrderMapper">
    <!-- 字段映射 -->
    <resultMap id="BaseResultMap" type="Order">
        <id column="id" property="id"/>
        <result column="order_no" property="orderNo"/>
        ...
    </resultMap>
    
    <!-- Oracle分页查询 -->
    <select id="selectPage" resultMap="BaseResultMap">
        SELECT * FROM (
            SELECT T.*, ROWNUM RN FROM (
                SELECT id, order_no, customer_name, ...
                FROM orders
                <where>
                    is_deleted = 0
                    <if test="keyword != null and keyword != ''">
                        AND (order_no LIKE #{keyword} OR customer_name LIKE #{keyword})
                    </if>
                    ...
                </where>
                ORDER BY created_at DESC
            ) T WHERE ROWNUM &lt;= #{offset} + #{size}
        ) WHERE RN > #{offset}
    </select>
    
    <!-- 乐观锁更新 -->
    <update id="updateByIdAndUpdatedAt">
        UPDATE orders
        SET customer_name = #{customerName},
            total_amount = #{totalAmount},
            updated_at = SYSTIMESTAMP,
            updated_by = #{updatedBy}
        WHERE id = #{id} AND updated_at = #{updatedAt}
    </update>
</mapper>
```

- 使用 **Oracle 12c+ 分页** 语法或 ROWNUM 方式
- LIKE 查询使用 Oracle 的 `||` 拼接或 `CONCAT`
- 时间戳函数使用 `SYSTIMESTAMP` 或 `CURRENT_TIMESTAMP`
- 布尔字段在 Oracle 中用 `0/1` 或 `'Y'/'N'`
- 乐观锁更新条件包含 `AND updated_at = #{updatedAt}`
- 批量插入明细（如 order_items）使用 `foreach` + `UNION ALL`，不可在 foreach 外部写单独的 SELECT：
  ```xml
  <insert id="insertItems">
      INSERT INTO order_items (...)
      <foreach collection="list" item="item" separator="UNION ALL">
          SELECT #{item.id}, #{item.orderId}, ... FROM DUAL
      </foreach>
  </insert>
  ```
  （`collection` 属性值取决于 Mapper 方法参数名，无 `@Param` 注解时用 `list`）
- 序列生成（如需）：使用 `SELECT {sequence}.NEXTVAL FROM DUAL`

##### 4. Service 接口及实现
```java
public interface OrderService {
    PageResult<OrderListVO> list(OrderListQuery query);
    OrderDetailVO detail(String id);
    OrderCreateVO create(OrderCreateRequest request, String operatorId);
    OrderUpdateVO update(String id, OrderUpdateRequest request, String operatorId);
    void delete(String id, OrderDeleteRequest request, String operatorId);
    OrderStatusVO updateStatus(String id, OrderStatusRequest request, String operatorId);
}
```

实现类规范：
- 使用 `@Service` + `@Transactional(rollbackFor = Exception.class)`
- 校验逻辑按照API设计书分步实现
- 乐观锁：先查 `updated_at`，比较，再执行带条件更新
- 业务编号生成：使用 Oracle `SELECT TO_CHAR(SYSDATE, 'YYYYMMDD') FROM DUAL` 拼接流水号
- 异常：抛自定义 `BusinessException`（如 `new BusinessException("E011", "数据已被其他用户修改")`）
- 方法内步骤顺序严格对照API设计书中的"处理内容"章节：
  1. 请求参数校验
  2. 数据获取
  3. 乐观锁检查
  4. 业务逻辑处理
  5. 响应生成

##### 5. Controller REST控制器
```java
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    
    @GetMapping
    public ResponseEntity<PageResult<OrderListVO>> list(OrderListQuery query) {
        return ResponseEntity.ok(orderService.list(query));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailVO> detail(@PathVariable String id) {
        return ResponseEntity.ok(orderService.detail(id));
    }
    
    @PostMapping
    public ResponseEntity<OrderCreateVO> create(@Valid @RequestBody OrderCreateRequest request) {
        String operatorId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(orderService.create(request, operatorId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrderUpdateVO> update(@PathVariable String id, @Valid @RequestBody OrderUpdateRequest request) {
        String operatorId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(orderService.update(id, request, operatorId));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, @Valid @RequestBody OrderDeleteRequest request) {
        String operatorId = SecurityUtils.getCurrentUserId();
        orderService.delete(id, request, operatorId);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderStatusVO> updateStatus(@PathVariable String id, @Valid @RequestBody OrderStatusRequest request) {
        String operatorId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(orderService.updateStatus(id, request, operatorId));
    }
}
```

- 统一使用 `@Valid` 或 `@Validated` 做参数校验
- 当前用户从 SecurityUtils / Token 中获取
- 响应统一使用 `ResponseEntity`
- 路径变量必须与API设计书的URI模板一致

##### 6. 枚举类
```java
public enum OrderStatus {
    PENDING("pending", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    SHIPPED("shipped", "已发货"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");
    
    private final String code;
    private final String label;
    
    // 状态流转校验
    public static void validateTransition(String currentStatus, String targetStatus) {
        // pending → confirmed/cancelled
        // confirmed → shipped/cancelled
        // shipped → completed
        // completed/cancelled → 不可变更
    }
}
```

- 状态值使用 `String code`，与数据库存储值一致
- 必须包含状态流转校验方法

##### 7. 全局异常处理
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse error = new ErrorResponse(e.getCode(), e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        // 提取字段校验错误信息
    }
}
```

##### 8. 配置类
- `WebMvcConfig.java`：CORS配置，允许跨域请求
- `MyBatisConfig.java`：MyBatis驼峰命名映射配置
- 使用Oracle JDBC驱动：`oracle.jdbc.OracleDriver`

##### 9. 分页结果封装
```java
@Data
public class PageResult<T> {
    private List<T> content;
    private int page;
    private int size;
    private long total;
    private int totalPages;
}
```

#### Oracle 注意事项
- 使用 `SELECT SEQ_ORDER.NEXTVAL FROM DUAL` 获取序列值
- 分页使用 Oracle ROWNUM 或 OFFSET FETCH 语法
- 布尔类型用 `NUMBER(1)`，Java映射为 `Boolean`，XML中 `is_deleted = 0`
- 日期用 `SYSTIMESTAMP` 或 `CURRENT_TIMESTAMP`
- 模糊查询用 `LIKE '%' || #{keyword} || '%'`
- MyBatis `&lt;` 转义小于号
- 自增主键用序列+触发器或程序UUID

### 第三步：输出文件（双路径生成）
生成的代码需要 **同时写入两个位置**：

#### 路径A：文档目录（用于 MkDocs 展示）
`/docs/基本设计/后端代码/{module}/`

#### 路径B：实际运行项目
`/backend/src/main/java/com/example/app/{module}/`（Java源码）
`/backend/src/main/resources/mapper/`（Mapper XML）

#### 文件对应关系
| 文件 | 路径A (文档) | 路径B (运行项目) |
|------|-------------|-----------------|
| Controller | `docs/基本设计/后端代码/{module}/controller/{Entity}Controller.java` | `backend/src/main/java/com/example/app/{module}/controller/{Entity}Controller.java` |
| Service 接口 | `docs/基本设计/后端代码/{module}/service/{Entity}Service.java` | `backend/src/main/java/com/example/app/{module}/service/{Entity}Service.java` |
| Service 实现 | `docs/基本设计/后端代码/{module}/service/impl/{Entity}ServiceImpl.java` | `backend/src/main/java/com/example/app/{module}/service/impl/{Entity}ServiceImpl.java` |
| Mapper 接口 | `docs/基本设计/后端代码/{module}/mapper/{Entity}Mapper.java` | `backend/src/main/java/com/example/app/{module}/mapper/{Entity}Mapper.java` |
| Entity | `docs/基本设计/后端代码/{module}/model/entity/{Entity}.java` | `backend/src/main/java/com/example/app/{module}/model/entity/{Entity}.java` |
| DTO 请求 | `docs/基本设计/后端代码/{module}/model/dto/request/*.java` | `backend/src/main/java/com/example/app/{module}/model/dto/request/*.java` |
| DTO 响应 | `docs/基本设计/后端代码/{module}/model/dto/response/*.java` | `backend/src/main/java/com/example/app/{module}/model/dto/response/*.java` |
| 枚举 | `docs/基本设计/后端代码/{module}/model/enums/{Entity}Status.java` | `backend/src/main/java/com/example/app/{module}/model/enums/{Entity}Status.java` |
| Mapper XML | `docs/基本设计/后端代码/{module}/resources/mapper/{Entity}Mapper.xml` | `backend/src/main/resources/mapper/{Entity}Mapper.xml` |

#### 共享文件（仅当不存在时生成）
以下文件在 `backend/src/main/java/com/example/app/common/` 中已存在，**不要覆盖**：
- `config/GlobalExceptionHandler.java`
- `config/MyBatisConfig.java`
- `config/WebMvcConfig.java`
- `exception/BusinessException.java`
- `model/dto/response/PageResult.java`

这些文件首次生成时写入路径A（文档），路径B用已有版本。后续模块生成时路径A也**不要覆盖**，直接复用。

#### 文件编码
- 全部使用 UTF-8

目录结构示例（路径A）：
```
docs/基本设计/后端代码/order/
├── controller/OrderController.java
├── service/OrderService.java
├── service/impl/OrderServiceImpl.java
├── mapper/OrderMapper.java
├── model/entity/Order.java
├── model/entity/OrderItem.java
├── model/dto/request/OrderListQuery.java
├── model/dto/request/OrderCreateRequest.java
├── model/dto/request/OrderUpdateRequest.java
├── model/dto/request/OrderStatusRequest.java
├── model/dto/request/OrderDeleteRequest.java
├── model/dto/response/OrderListVO.java
├── model/dto/response/OrderDetailVO.java
├── model/dto/response/OrderCreateVO.java
├── model/dto/response/OrderUpdateVO.java
├── model/dto/response/OrderStatusVO.java
├── model/dto/response/PageResult.java
├── model/enums/OrderStatus.java
├── config/WebMvcConfig.java
├── config/MyBatisConfig.java
├── config/GlobalExceptionHandler.java
└── resources/mapper/OrderMapper.xml
```

目录结构示例（路径B）：
```
backend/src/main/java/com/example/app/order/
├── controller/OrderController.java
├── service/OrderService.java
├── service/impl/OrderServiceImpl.java
├── mapper/OrderMapper.java
├── model/entity/Order.java
├── model/entity/OrderItem.java
├── model/dto/request/OrderListQuery.java
├── model/dto/request/OrderCreateRequest.java
├── model/dto/request/OrderUpdateRequest.java
├── model/dto/request/OrderStatusRequest.java
├── model/dto/request/OrderDeleteRequest.java
├── model/dto/response/OrderListVO.java
├── model/dto/response/OrderDetailVO.java
├── model/dto/response/OrderCreateVO.java
├── model/dto/response/OrderUpdateVO.java
├── model/dto/response/OrderStatusVO.java
├── model/dto/response/PageResult.java
├── model/enums/OrderStatus.java
backend/src/main/resources/mapper/OrderMapper.xml
```

### 第四步：更新 mkdocs.yml 导航（确保所有入口可访问）

根据该模块已创建的设计文档，在 `mkdocs.yml` 的 `基本设计` 下按以下规则逐分区补充导航入口。

> 检查原则：如果模块对应的设计文档已存在，则必须添加导航入口；如果不存在则跳过。

#### 4.1 表定义
```yaml
- 表定义:
  - {模块}表: 基本设计/表定义/{模块}.md
```
检查 `docs/基本设计/表定义/{模块}.md` 是否存在，存在则追加。

#### 4.2 API设计
```yaml
- API设计:
  - {API名}: 基本设计/API设计书/{API名}_API设计.md
```
检查 `docs/基本设计/API设计书/{API名}_API设计.md` 是否存在，存在则追加。
可能有多个API（如 `用户登录`、`用户新建`），逐一添加。

#### 4.3 画面设计
```yaml
- 画面设计:
  - {画面名}: 基本设计/画面设计书/{画面名}_画面设计.md
```
检查 `docs/基本设计/画面设计书/{画面名}_画面设计.md` 是否存在，存在则追加。

#### 4.4 后端代码
```yaml
- 后端代码:
  - {模块名}: 基本设计/后端代码/{module}/index.md
```
先确认 `docs/基本设计/后端代码/{module}/index.md` 已创建（上一步已生成），然后追加。

#### 4.5 画面(HTML)
```yaml
- "画面(HTML)":
  - {画面名}: 基本设计/画面/{画面名}.html
```
检查 `docs/基本设计/画面/{画面名}.html` 是否存在，存在则追加。

> 排序规则：所有子项均按字母/拼音顺序或按已有顺序追加在末尾，不破坏现有顺序。

### 第五步：更新主页侧边栏

1. **更新 `docs/基本设计/画面/主页.html`**
   - 在 JavaScript 的 `menuItems` 数组中添加新的菜单项
   - 放在 `{ divider: true, label: '业务管理' }` 分隔线之后，其他菜单项之前或之后
   - 如果该模块有对应的前端页面文件（`docs/基本设计/画面/{Entity}.html`），设置 `pageSrc: '{Entity}.html'`
   - 如果没有前端页面，则不设置 `pageSrc`（只作为导航菜单展示入口）
   - 菜单项格式（示例）：
     ```javascript
     { label: '订单管理', icon: '<svg>...</svg>', pageTitle: '订单管理', pageSrc: '订单.html' }
     ```
   - 图标使用 SVG 的 `outline` 风格（stroke），大小 `w-5 h-5`，与现有图标风格一致
   - 插入位置：在 `menuItems` 数组末尾（`业务管理` 分隔线之后）

2. **如果还没有前端页面**，同时运行 `/generate-page` 生成对应的画面HTML

### 第五步补充：侧边栏索引同步

插入新菜单项后，必须同步更新所有引用 `switchMenu` 的快捷入口索引：

1. **检查 `docs/基本设计/画面/主页.html` 中的快捷入口（首页 dashboard）**
   - 找到 `快捷入口` 区域的 `switchMenu(N)` 调用
   - 确保每个快捷按钮的 `N` 值与 `menuItems` 数组中的索引一致
   - 示例：如果 `menuItems` 中的顺序是 `[首页(0), 分隔线, 用户管理(2), 地址管理(3), 商品管理(4), 订单管理(5)]`，则快捷按钮应为 `switchMenu(2)`→用户, `switchMenu(3)`→地址, `switchMenu(4)`→商品, `switchMenu(5)`→订单
   - 新增模块后，其之后的所有菜单索引 +1，对应的快捷按钮也必须更新

2. **新增快捷入口卡片（可选）**
   - 如果首页 dashboard 的快捷入口区域未满 4 个卡片，添加新模块的快捷卡片
   - 格式与其他卡片一致（图标 + 标签 + 描述）
   - 插入位置：按 `menuItems` 的业务管理顺序排列

### 第六步：验证确认

生成并部署后，验证以下内容是否正常工作：

1. **Maven 编译验证**
   ```bash
   cd backend && mvn compile
   ```
   - 确认编译成功，无错误

2. **主页侧边栏验证**
   - 打开 `docs/基本设计/画面/主页.html`
   - 确认新模块的菜单项已出现在侧边栏「业务管理」分组中
   - 点击菜单项，确认 iframe 正确加载对应的 HTML 页面（如 `商品.html`）
   - 如果模块没有前端页面，确认菜单项仍可显示（仅作为导航入口）

3. **API 端点验证**（后端启动后）
   - 列表查询：`GET /api/v1/{模块名}` → 返回 200 + 空列表（无数据时）
   - 新建：`POST /api/v1/{模块名}` → 返回 200 + 创建的数据
   - 详情：`GET /api/v1/{模块名}/{id}` → 返回 200 + 数据
   - 更新：`PUT /api/v1/{模块名}/{id}` → 返回 200 + 更新后数据
   - 删除：`DELETE /api/v1/{模块名}/{id}` → 返回 204
   - 状态变更（如有）：`PATCH /api/v1/{模块名}/{id}/status` → 返回 200

4. **前端 CRUD 验证**
   - 打开 `docs/基本设计/画面/{模块}.html`
   - 新建一条数据 → 确认列表刷新显示新数据
   - 编辑数据 → 确认保存后字段值更新
   - 查看详情 → 确认数据完整显示
   - 删除数据 → 确认列表刷新，数据消失
   - 状态变更 → 确认标签切换正确

5. **乐观锁验证**
   - 更新操作：请求不带 `updatedAt` → 返回 400
   - 更新操作：请求带错误的 `updatedAt` → 返回 409 (E9409)
   - 删除/状态变更同理

## 代码检查
生成后检查以下内容：
- **API一致性检查**：所有端点URL、方法、参数、响应是否与API设计书完全一致
- **字段映射检查**：实体字段与表定义是否一致（类型、长度、约束）
- **校验规则检查**：请求参数校验是否覆盖API设计书中所有规则
- **乐观锁检查**：更新/删除/状态变更是否包含updatedAt乐观锁
- **状态流转检查**：状态变更API是否正确实现了流转规则
- **Oracle兼容检查**：SQL语法是否符合Oracle规范（分页、序列、日期函数等）
- **事务检查**：涉及多表操作的方法是否正确使用`@Transactional`
