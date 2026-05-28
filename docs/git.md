# Git 常用命令速查

## 仓库初始化与克隆

```bash
git init                              # 在当前目录初始化 Git 仓库
git clone <url>                       # 克隆远程仓库到本地
```

## 配置

```bash
git config user.name "name"           # 设置当前仓库用户名
git config user.email "email"         # 设置当前仓库邮箱
git config --global user.name "name"  # 设置全局用户名
git config --list                     # 查看所有配置
git remote add origin <url>           # 添加远程仓库
git remote set-url origin <url>       # 修改远程仓库地址
git remote -v                         # 查看远程仓库地址
```

## 基本操作

```bash
git status                            # 查看工作区状态
git add <file>                        # 暂存文件
git add .                             # 暂存所有变更（新建/修改/删除）
git commit -m "message"               # 提交暂存区
git commit -am "message"              # 暂存所有已跟踪文件 + 提交（不含新建文件）
git log --oneline                     # 查看简洁提交历史
```

## 分支

```bash
git branch                            # 查看本地分支列表（* 表示当前分支）
git branch <name>                     # 创建分支
git checkout <name>                   # 切换分支
git checkout -b <name>                # 创建并切换分支
git merge <name>                      # 合并指定分支到当前分支
git branch -d <name>                  # 删除本地分支
```

## 远程同步

```bash
git push origin <branch>              # 推送到远程
git push -u origin <branch>           # 推送并建立上游跟踪（首次推送用）
git pull origin <branch>              # 拉取远程并合并
git fetch origin                      # 拉取远程但不合并
```

## 撤销与回退

```bash
git restore <file>                    # 撤销工作区修改（未暂存）
git restore --staged <file>           # 取消暂存（保留修改）
git reset --soft HEAD~1               # 撤销上一次提交，保留修改在工作区
git reset --hard HEAD~1               # 撤销上一次提交，丢弃修改（慎用）
git reset --hard <commit-hash>        # 回退到指定提交（慎用）
```

## 查看差异

```bash
git diff                              # 工作区 vs 暂存区
git diff --staged                     # 暂存区 vs 上一次提交
git diff <commit1> <commit2>          # 两个提交之间的差异
```

## 暂存工作区

```bash
git stash                             # 暂存当前修改（工作区恢复干净）
git stash pop                         # 恢复最近一次暂存
git stash list                        # 查看暂存列表
```

## 标签

```bash
git tag                               # 查看标签列表
git tag <tagname>                     # 创建标签
git push origin <tagname>             # 推送标签到远程
```

## .gitignore 语法

```
# 注释
*.log                 # 忽略所有 .log 文件
temp_*                # 忽略以 temp_ 开头的文件/目录
build/                # 忽略 build 目录
!important.log        # 不忽略重要日志（排除规则）
```

## 推送流程示例（当前仓库）

```bash
# 远程地址：https://github.com/liuujn/ai-design.git
git add .
git commit -m "feat: 添加新功能"
git push origin main
```

## 拉取远程更新示例

```bash
git pull origin main    # 拉取远程 main 并合并到当前分支
```
