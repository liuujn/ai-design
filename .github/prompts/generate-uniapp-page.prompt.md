# Prompt 定义：/generate-uniapp-page

## 名称
generate_uniapp_page

## 描述
根据画面设计书和API设计书生成 UniApp 前端页面代码（跨平台移动端）

## 触发命令
/generate-uniapp-page

## 参照内容
- 画面设计书目录：/docs/基本设计/画面设计书/*
- API设计书目录：/docs/基本设计/API设计书/*
- 表定义：/docs/基本设计/表定义/*
- 已生成的画面示例：/docs/基本设计/画面/*.html（参考业务逻辑）

## 页面生成
用户提供需要生成的画面名称（如"商品列表"），执行以下步骤：

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

### 第二步：生成 UniApp 页面

生成一个完整的 UniApp 单页 `.vue` 文件。**外观必须精致、现代、符合移动端设计规范（iOS/ Material Design 风格）**。视觉质量是第一优先级。

#### 技术栈
- **框架**: UniApp（Vue 3 Composition API + setup 语法糖）
- **语言**: `<template>` + `<script setup>` + `<style scoped>`
- **网络请求**: `uni.request()` 封装，统一拦截器
- **状态管理**: Vue 3 `ref()` / `reactive()` / `computed()`
- **路由跳转**: `uni.navigateTo()` / `uni.switchTab()` / `uni.redirectTo()`
- **本地存储**: `uni.getStorageSync()` / `uni.setStorageSync()`
- **弹窗提示**: `uni.showToast()` / `uni.showModal()` / `uni.showLoading()` / `uni.hideLoading()`

####  UniApp 组件映射（HTML → UniApp）

| Web 标签 | UniApp 组件 | 说明 |
|----------|-------------|------|
| `<div>` | `<view>` | 块级容器 |
| `<span>` | `<text>` | 行内文本 |
| `<input>` | `<input>` | 输入框 |
| `<textarea>` | `<textarea>` | 多行输入 |
| `<button>` | `<button>` | 按钮（type=primary/default/warn） |
| `<select>` + `<option>` | `<picker>` | 选择器（mode=selector/multiSelector/date） |
| `<table>` + `<tr>` + `<td>` | 手写 `<view>` 布局 | 列表用 `<scroll-view>` + flex 布局 |
| `<img>` | `<image>` | 图片（mode=aspectFill/widthFix…） |
| `<a>` | `<navigator>` | 页面跳转 |
| `<form>` | `<form>` | 表单容器 |
| `<iframe>` | 不支持 | UniApp 无 iframe |
| `<label>` | `<label>` | 标签 |
| `<ul>` + `<li>` | `<view>` + flex | 列表 |
| `<i>` / `<svg>` | `<text>` 或 iconfont | 图标 |

#### 页面结构

每个 `.vue` 文件包含三部分:

```vue
<template>
  <view class="page">
    <!-- 页面内容 -->
  </view>
</template>

<script setup>
import { ref, reactive, computed, onLoad, onShow, onReady } from 'vue'

// 页面逻辑
</script>

<style scoped>
/* 页面样式 */
</style>
```

#### 视觉风格要求

生成页面必须精美、专业、符合移动端设计规范：

1. **整体布局**
   - 页面背景色: `#f5f6fa`，安全区域: `padding: 20rpx 30rpx`
   - 内容卡片: 白色背景 + 圆角 `border-radius: 16rpx` + 盒阴影 `box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.06)`
   - 页面标题: `font-size: 36rpx; font-weight: 600; color: #1a1a2e`
   - **所有可交互元素添加过渡动画**: `transition: all 0.3s ease`

2. **单位规范**
   - 使用 `rpx` 作为尺寸单位（UniApp 标准响应式单位）
   - 字号: 标题 32-36rpx / 正文 28rpx / 辅助 24rpx / 小字 20rpx
   - 间距基准: 20rpx / 30rpx / 40rpx
   - 圆角: 小 8rpx / 中 12rpx / 大 16rpx / 超大 24rpx

3. **搜索区域**
   - 搜索框: `<input>` + 搜索图标，`border-radius: 40rpx` 胶囊风格
   - 筛选条件：用 `<picker>` 实现下拉选择 + 手写标签式筛选条
   - 搜索按钮：`uni-app` 的 `button` 组件，`type='primary'`，圆角

4. **列表区域**
   - 使用 `<scroll-view>` 包裹列表，支持下拉刷新 `@refresherrefresh` + 触底加载更多 `@scrolltolower`
   - 列表项卡片风格：白色背景 + 圆角 + 底部分隔线
   - 每个列表项包含：标题（加粗）、描述（灰色小字）、状态标签（彩色圆角badge）、右侧箭头或操作按钮
   - 列表项点击事件：`@tap` 跳转到详情
   - 空数据状态：居中图标 + 文字提示 + 可能的重试按钮
   - Loading 状态：`uni.showLoading()` 或自定义骨架屏

5. **表单区域**
   - 表单项：左标签右内容布局（`flex`），底部边框分隔
   - 输入框：`<input>` 组件，`border-bottom: 2rpx solid #eee`，focus 时变主色
   - picker 选择器：右侧显示当前选中值 + 箭头图标
   - 必填项：标签前红色 `*` 标记
   - 表单校验：实时校验 + 提交时逐字段校验，使用 `uni.showToast({ icon: 'none' })` 显示错误

6. **操作按钮**
   - 主操作（保存/提交/登录）：全宽按钮 `width: 100%`，`background: linear-gradient(135deg, #667eea, #764ba2)`
   - 次要操作（取消/返回）：白底灰边框
   - 危险操作（删除）：红色文字按钮
   - 按钮高度: 88rpx，圆角: 44rpx

7. **弹窗与提示**
   - Toast: `uni.showToast({ icon: 'none' })` 显示文字提示
   - 确认弹窗: `uni.showModal({ title: '提示', content: '确认执行操作？' })`
   - Loading: `uni.showLoading({ title: '加载中...' })` + `uni.hideLoading()`
   - 底部弹出式选择器（ActionSheet）: `uni.showActionSheet()`

8. **颜色规范**
   - 主色渐变: `linear-gradient(135deg, #667eea, #764ba2)`（紫蓝色渐变）
   - 主色纯色: `#667eea`
   - 成功绿: `#10b981` (emerald)
   - 警告橙: `#f59e0b` (amber)
   - 危险红: `#ef4444` (red)
   - 背景灰: `#f5f6fa`
   - 卡片白: `#ffffff`
   - 文字主: `#1a1a2e` / 次要: `#6b7280` / 提示: `#9ca3af` / 禁用: `#d1d5db`

9. **状态标签颜色**
   - active/上架/已完成: 绿底绿字 `background: #d1fae5; color: #065f46`
   - inactive/下架/已取消: 灰底灰字 `background: #f3f4f6; color: #6b7280`
   - pending/待处理: 橙底橙字 `background: #fef3c7; color: #92400e`
   - 其他状态根据业务语义选择合适的颜色组合

#### 业务逻辑实现

严格对照画面设计书第5章"处理功能详细"实现：

1. **API 请求封装**
   ```javascript
   // api/request.js - 统一请求封装
   const BASE_URL = 'http://localhost:9090/api/v1'

   function request(config) {
     return new Promise((resolve, reject) => {
       const token = uni.getStorageSync('auth_token')
       uni.request({
         url: BASE_URL + config.url,
         method: config.method || 'GET',
         data: config.data,
         header: {
           'Content-Type': 'application/json',
           ...(token ? { Authorization: 'Bearer ' + token } : {})
         },
         success: (res) => {
           if (res.statusCode === 401) {
             uni.removeStorageSync('auth_token')
             uni.redirectTo({ url: '/pages/login/login' })
             return
           }
           resolve(res)
         },
         fail: (err) => {
           uni.showToast({ title: '网络异常', icon: 'none' })
           reject(err)
         }
       })
     })
   }
   ```

2. **认证与登录检查**
   ```javascript
   onLoad(() => {
     const token = uni.getStorageSync('auth_token')
     if (!token) {
       uni.redirectTo({ url: '/pages/login/login' })
       return
     }
     // 初始化数据
   })
   ```

3. **乐观锁机制**
   - 所有更新/删除/状态变更请求必须携带 `updatedAt` 参数
   - 从列表项中获取 `updatedAt` 值
   - 处理 `E9409` 错误码提示"数据已被其他用户修改"

4. **分页实现**
   - 使用 `page` 和 `size` 参数
   - 触底加载更多（`@scrolltolower`）：`page++` 后追加到列表
   - 下拉刷新（`@refresherrefresh`）：重置 `page=1` 重新加载
   - 第一页加载显示 loading，后续页面追加显示"加载中..."

5. **状态流转控制**
   - 按照设计书中的状态流转规则控制按钮显示
   - 不同状态显示不同的操作按钮组合

#### UniApp 特有注意事项

1. **不支持 HTML 标签**：全部使用 UniApp 内置组件（`<view>` / `<text>` / `<input>` / `<image>` / `<scroll-view>` / `<swiper>` / `<picker>` 等）
2. **不支持 `window` / `document` / `localStorage`**：使用 `uni.getStorageSync` / `uni.setStorageSync`
3. **不支持 `axios`**：使用 `uni.request()` 封装
4. **不支持 `window.location.href`**：使用 `uni.navigateTo()` / `uni.redirectTo()` / `uni.switchTab()`
5. **不支持 DOM 操作**：不要使用 `document.querySelector` 等
6. **CSS 使用标准 CSS**（不是 Tailwind），使用 `rpx` 单位
7. **页面生命周期**：`onLoad` / `onShow` / `onReady` 代替 Vue Web 的 `onMounted`
8. **导航栏**：可以使用 `uni.setNavigationBarTitle()` 设置标题
9. **下拉刷新**：`<scroll-view refresher-enabled @refresherrefresh="onRefresh">`
10. **触底加载**：`<scroll-view @scrolltolower="onLoadMore">`
11. **图标**：可使用 iconfont（`<text class="iconfont icon-xxx">`）或 emoji
12. **不支持 `<style scoped>` 的深度选择器 `>>>`**：使用 `/deep/` 或 `::v-deep`

### 第三步：输出文件
- 生成的文件存放在项目的 `pages/{模块名}/` 目录下
- 文件名：`{画面名}.vue`
- 文件编码：UTF-8

### 第四步：更新路由
- 在 `pages.json` 中添加页面路由配置：
  ```json
  {
    "path": "pages/{模块名}/{画面名}",
    "style": {
      "navigationBarTitleText": "页面标题"
    }
  }
  ```

## 页面检查
生成后检查以下内容：
- **视觉检查**：页面是否精美、符合移动端设计规范？颜色、间距、字体、圆角是否到位？
- **组件检查**：是否全部使用 UniApp 内置组件（无 HTML 标签）？
- **API 检查**：所有 API 端点是否与 API 设计书一致（URL、方法、参数）
- **字段检查**：页面字段是否与画面设计书一致（组件类型、校验规则、长度限制）
- **逻辑检查**：所有处理功能是否都已实现，状态流转是否正确
- **映射检查**：字段映射是否正确（页面项目 ↔ API请求/响应）
- **体验检查**：操作提示、确认弹窗、空状态、加载状态、下拉刷新、触底加载是否完善
- **存储检查**：Token 存取使用 `uni.getStorageSync/setStorageSync`，非 `localStorage`
- **路由检查**：页面跳转使用 `uni.navigateTo` 而非 `window.location`
