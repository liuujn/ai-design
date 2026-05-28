# Prompt 定义：/generate-page

## 名称
generate_page

## 描述
根据画面设计书和API设计书生成前端页面代码

## 触发命令
/generate-page

## 参照内容
- 画面设计书目录：/docs/基本设计/画面设计书/*
- API设计书目录：/docs/基本设计/API设计书/*
- 表定义：/docs/基本设计/表定义/*
- 已生成的画面示例：/docs/基本设计/画面/订单.html

## 页面生成
用户提供需要生成的画面名称（如"订单"），执行以下步骤：

### 第一步：读取设计文档
1. 读取对应的画面设计书（`{画面名}_画面设计.md`），获取：
   - 页面概要（功能描述）
   - 页面布局（区域划分）
   - 页面项目信息（组件类型、校验规则、长度、事件绑定）
   - 处理功能详细（所有事件的处理逻辑）
   - API接口规范与页面项映射（字段对应关系）

2. 读取对应的API设计书（`{画面名}_API设计.md`），获取：
   - API端点（URL、方法、请求参数、响应结构）
   - 校验规则和错误消息

3. 读取对应的表定义，确认字段类型、长度、必填性

### 第二步：生成前端页面
生成一个完整的单页HTML文件，外观风格必须与主流后台管理系统一致（如 Ant Design Pro、Element Plus Admin 等），确保专业、美观、易用。**页面的视觉质量是第一优先级，必须精致、现代、有质感。**

#### 技术栈
- Vue 3 (CDN: `https://cdnjs.cloudflare.com/ajax/libs/vue/3.5.13/vue.global.prod.js`)
- Tailwind CSS (CDN: `https://cdn.tailwindcss.com`)
- axios (CDN: `https://cdnjs.cloudflare.com/ajax/libs/axios/1.7.9/axios.min.js`)
- 全部使用CDN引入，无需构建工具

备选CDN：
- Vue: `https://cdn.bootcdn.net/ajax/libs/vue/3.5.13/vue.global.prod.js`
- axios: `https://cdn.bootcdn.net/ajax/libs/axios/1.7.9/axios.min.js`

#### 视觉风格要求
生成的页面必须精美、现代，与一线互联网公司后台系统看齐。**严格按照以下规范实现：**

1. **整体布局**
   - 页面背景：`bg-gray-50`，内容卡片：白色圆角 + 细腻阴影（`shadow-sm`）+ 1px 边框（`border border-gray-100`）
   - 内容区 padding: 20-24px，卡片间间距 16-20px
   - 页面标题：`text-xl font-bold text-gray-800 tracking-tight`
   - **必须添加平滑过渡动画**：`transition-all duration-200` 到所有可交互元素

2. **搜索区域**
   - 使用 Tailwind 手写折叠面板，不要依赖任何第三方组件库
   - 折叠头：`bg-gray-50` hover 变色，右侧箭头旋转动画
   - 搜索项使用网格布局 `grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3`
   - 所有输入框：圆角 `rounded-lg`，focus 时 `ring-2 ring-blue-500` + 边框变蓝
   - 搜索按钮蓝色 `bg-blue-600 hover:bg-blue-700`，清除按钮灰色边框

3. **表格区域**
   - 原生 `<table>` 实现，不用第三方表格组件
   - 表头：`bg-gray-50` + `text-xs uppercase tracking-wider text-gray-500`
   - 表格行：`hover:bg-blue-50` + `cursor-pointer` + `transition-colors`
   - 斑马纹不需要，用 `divide-y divide-gray-100` 分隔线
   - 状态列：`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium`，**每个状态有独立的颜色组合**
   - 数字列：`tabular-nums` 等宽数字字体
   - 操作按钮：文字按钮 `text-blue-600 hover:text-blue-800`（查看）/ `text-red-500 hover:text-red-700`（删除）
   - **表格上方必须有 loading 状态**：旋转动画 spinner

4. **分页区域**
   - 手写分页，不使用第三方组件
   - 左右结构：左侧"共 N 条"，右侧页码控制
   - 页码按钮：`px-3 py-1.5 border border-gray-300 rounded-lg` hover 变色
   - 禁用状态：`opacity-40 cursor-not-allowed`

5. **详情/编辑表单区域**
   - 信息展示区域使用 `grid grid-cols-2 gap-4 p-4 bg-gray-50 rounded-lg`，标签灰色小字，值加粗
   - 编辑区域：输入框风格与搜索区域一致
   - 必填字段标记：在标签文本后添加红色 `*`（用 CSS `::after` 或单独 `<span>`）
   - **密码字段必须有显示/隐藏切换按钮**（眼睛图标或文字按钮）

6. **明细表格（订单类业务）**
   - 数量编辑：**手写 - / + 按钮组**（`inline-flex items-center border border-gray-300 rounded-lg overflow-hidden`），中间数字可编辑
   - 添加明细按钮：蓝色文字 + 浅蓝背景 `text-blue-600 bg-blue-50 rounded-lg hover:bg-blue-100`

7. **操作按钮区域**
   - 所有按钮统一风格：`px-4 py-2 text-sm rounded-lg transition-colors`
   - 主操作（保存/新建）：`bg-blue-600 text-white hover:bg-blue-700 shadow-sm`
   - 次要操作（取消/返回）：`border border-gray-300 text-gray-600 hover:bg-gray-50`
   - 编辑按钮：`border border-blue-300 text-blue-600 hover:bg-blue-50`
   - 状态操作按钮使用不同颜色（emerald-500 确认、blue-500 发货、red-500 取消）

8. **交互体验**
   - 操作提示：**右下角滑入式通知**（`fixed bottom-4 right-4`），不要居中
   - 删除确认弹窗：居中模态，背景遮罩 `bg-black/40`，点击遮罩可关闭
   - 空数据：表格区域居中显示 `text-gray-400 text-sm py-12`
   - 搜索 loading：表格上方居中旋转动画
   - **所有 hover 和 focus 状态必须有 transition 动画**（`transition-colors duration-200`）
   - 必填字段在提交时逐字段校验，给出具体的错误提示

9. **颜色规范**
   - 主色蓝：`#3b82f6`（blue-500）/ hover: `#2563eb`（blue-600）
   - 成功绿：`#10b981`（emerald-500）
   - 警告橙：`#f59e0b`（amber-500）
   - 危险红：`#ef4444`（red-500）
   - 信息灰：`#6b7280`（gray-500）
   - 背景灰：`#f9fafb`（gray-50）
   - 卡片白：`#ffffff`
   - 文字主：`#1f2937`（gray-800）/ 次要：`#4b5563`（gray-600）/ 提示：`#9ca3af`（gray-400）

10. **点缀细节（让页面更高级）**
    - 卡片顶部可以加一条细的蓝色顶部边框（`border-t-2 border-t-blue-500`）
    - 新建按钮包含 SVG 加号图标
    - 折叠面板箭头使用 SVG 图标取代纯文字
    - 数值列使用等宽字体（`tabular-nums` 或 `font-mono`）
    - 分页器页码使用 `font-medium`
    - 弹窗使用 `shadow-xl` + `rounded-xl` 更高级阴影
    - 搜索区域折叠后保留已填写的值（`v-show` 而非 `v-if`）

#### 页面结构
严格对照画面设计书生成，包括：
1. **列表模式**（对应画面设计书的搜索+列表区域）
   - 搜索条件面板（折叠式，带箭头旋转动画）
   - 操作按钮区域（新建等）
   - 数据表格（原生 table，hover 高亮，loading 状态）
   - 分页器（手写）

2. **详情/编辑模式**（对应画面设计书的表单+明细区域）
   - 页面标题与操作按钮（保存、取消、返回）
   - 信息展示/编辑表单（网格布局）
   - 状态操作按钮区域（根据当前状态动态显示）
   - 明细表格（手写 -/+ 按钮组，行内编辑）
   - 编辑/查看模式切换

#### 业务逻辑
严格对照画面设计书第5章"处理功能详细"实现：
- 每个 handleXxx 方法按照处理功能详细描述实现
- API调用使用 `axios` 实现，注释标注实际API地址
- 校验规则按照API设计书的校验逻辑实现
- 乐观LOCK机制：更新/删除/状态变更请求携带 updatedAt
- 状态流转控制：按照设计书中的状态流转规则控制按钮显示

#### 权限认证（Token认证）
- 所有API请求必须携带认证Token
- 创建 axios 实例时设置基础配置：
  ```javascript
  const api = axios.create({
    baseURL: '/api/v1',
    headers: { 'Authorization': `Bearer ${localStorage.getItem('auth_token') || ''}` }
  })
  ```
- 使用 axios 请求拦截器自动注入 Token：
  ```javascript
  api.interceptors.request.use(config => {
    const token = localStorage.getItem('auth_token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
  })
  ```
- 使用 axios 响应拦截器统一处理 401 错误：
  ```javascript
  api.interceptors.response.use(
    res => res,
    error => {
      if (error.response?.status === 401) {
        localStorage.removeItem('auth_token')
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )
  ```
- 页面初始化时检查 Token 是否存在，不存在则跳转到登录页：
  ```javascript
  if (!localStorage.getItem('auth_token')) { window.location.href = '/login' }
  ```
- 所有API函数使用 `api` 实例调用（`api.get` / `api.post` / `api.put` / `api.patch` / `api.delete`），注释标注实际API地址
- 模拟数据字段与API设计书的响应体完全一致
- axios 请求示例：
  ```javascript
  // 实际API: GET /api/v1/orders
  // const res = await api.get('/orders', { params })
  // 模拟数据：
  const res = { data: { orders: [...], total: 100 } }
  ```

### 第三步：输出文件
- 生成的文件存放在 `/docs/基本设计/画面/` 目录下
- 文件名：`{画面名}.html`
- 文件编码：UTF-8
- 页面标题为画面设计书中的画面名

### 第四步：更新导航
- 将生成的HTML页面添加到 `mkdocs.yml` 导航中
- 确保通过 MkDocs 站点可以访问该页面

## 页面检查
生成后检查以下内容：
- **视觉检查**：页面是否精美、现代，与主流后台系统看齐？颜色、间距、字体、动画是否到位？
- **功能检查**：所有API端点是否与API设计书一致（URL、方法、参数）
- **字段检查**：页面字段是否与画面设计书中的页面项目信息一致（组件类型、校验规则、长度限制）
- **逻辑检查**：所有处理功能是否都已实现，状态流转是否正确
- **映射检查**：字段映射是否正确（页面项目 ↔ API请求/响应）
- **体验检查**：操作提示、确认弹窗、空状态、加载状态、过渡动画是否完善
- **代码检查**：HTML结构是否简洁，CSS类名是否规范，JS逻辑是否清晰
