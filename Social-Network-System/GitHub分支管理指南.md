# GitHub分支管理完整指南

## 📋 分支管理策略

### 项目分支结构
```
main (主分支，保护分支)
├── data-structures (数据结构组分支)
├── algorithms (算法组分支)
└── gui-development (GUI组分支)
```

## 🚀 创建分支的三种方法

### 方法一：使用Git命令行（推荐）

#### 1. 克隆仓库到本地
```bash
# 如果是首次操作，先克隆仓库
git clone https://github.com/你的用户名/Social-Network-System.git
cd Social-Network-System
```

#### 2. 创建并切换到新分支
```bash
# 数据结构组创建分支
git checkout -b data-structures

# 算法组创建分支  
git checkout -b algorithms

# GUI组创建分支
git checkout -b gui-development
```

#### 3. 推送分支到GitHub
```bash
# 推送分支到远程仓库
git push -u origin data-structures
git push -u origin algorithms
git push -u origin gui-development
```

### 方法二：使用GitHub网页界面

#### 1. 在GitHub仓库页面创建分支
1. 打开仓库主页：`https://github.com/你的用户名/Social-Network-System`
2. 点击分支下拉框
3. 输入新分支名称：`data-structures`
4. 点击"Create branch"

#### 2. 克隆到本地
```bash
git clone https://github.com/你的用户名/Social-Network-System.git
cd Social-Network-System
git checkout data-structures
```

### 方法三：使用GitHub Desktop

#### 1. 创建分支
1. 打开GitHub Desktop
2. 选择仓库
3. 点击"Current branch"
4. 点击"New branch"
5. 输入分支名称
6. 点击"Create branch"

## 🔄 日常开发工作流程

### 每日开始工作
```bash
# 1. 切换到自己的分支
git checkout data-structures

# 2. 拉取最新代码（如果有更新）
git pull origin data-structures

# 3. 开始开发工作
```

### 开发完成后提交代码
```bash
# 1. 查看修改状态
git status

# 2. 添加修改文件
git add .

# 3. 提交代码（使用规范提交信息）
git commit -m "feat: 实现邻接表节点添加功能"

# 4. 推送到远程分支
git push origin data-structures
```

### 提交Pull Request（代码合并）

#### 1. 在GitHub上创建Pull Request
1. 打开仓库页面
2. 点击"Pull requests"标签
3. 点击"New pull request"
4. 选择分支：
   - base: `main` 
   - compare: `data-structures`
5. 填写PR标题和描述
6. 点击"Create pull request"

#### 2. PR描述模板
```markdown
## 功能描述
实现邻接表数据结构的基本功能

## 修改内容
- 添加addUser方法
- 添加addFriendship方法  
- 添加getFriends方法
- 编写单元测试

## 测试结果
- 所有单元测试通过
- 代码覆盖率90%

## 相关Issue
Close #1
```

## 📝 提交信息规范

### 提交类型
```
feat: 新功能
fix: 修复bug
docs: 文档更新
test: 测试相关
refactor: 代码重构
style: 代码格式调整
chore: 构建过程或辅助工具变动
```

### 提交示例
```bash
# 好的提交信息
git commit -m "feat: 实现哈希表put和get方法"
git commit -m "fix: 修复BFS算法重复访问问题"
git commit -m "docs: 更新数据结构组开发文档"

# 不好的提交信息
git commit -m "更新代码"  # 太模糊
git commit -m "修复bug"   # 不具体
```

## 🔧 分支管理命令大全

### 查看分支
```bash
# 查看本地分支
git branch

# 查看所有分支（包括远程）
git branch -a

# 查看分支最后提交
git branch -v
```

### 切换分支
```bash
# 切换到已有分支
git checkout data-structures

# 切换到上一个分支
git checkout -
```

### 合并分支
```bash
# 合并其他分支到当前分支
git merge algorithms

# 合并时创建合并提交
git merge --no-ff data-structures
```

### 删除分支
```bash
# 删除本地分支
git branch -d data-structures

# 强制删除分支
git branch -D data-structures

# 删除远程分支
git push origin --delete data-structures
```

## 🎯 三小组具体操作指南

### 数据结构组操作流程

#### 第1天开始
```bash
# 克隆仓库（如果还没有）
git clone https://github.com/你的用户名/Social-Network-System.git
cd Social-Network-System

# 创建并切换到数据结构分支
git checkout -b data-structures

# 开始开发AdjacencyList.java等文件
```

#### 每日工作
```bash
# 早上：拉取最新代码
git pull origin data-structures

# 开发...

# 晚上：提交代码
git add src/com/socialnetwork/core/AdjacencyList.java
git commit -m "feat: 实现邻接表基础功能"
git push origin data-structures
```

### 算法组操作流程

#### 第1天开始
```bash
git clone https://github.com/你的用户名/Social-Network-System.git
cd Social-Network-System
git checkout -b algorithms

# 开始开发GraphAlgorithm.java等文件
```

### GUI组操作流程

#### 第1天开始
```bash
git clone https://github.com/你的用户名/Social-Network-System.git
cd Social-Network-System
git checkout -b gui-development

# 开始开发SocialNetworkApp.java等文件
```

## ⚠️ 常见问题解决

### 问题1: 分支冲突
**场景**: 多人修改了同一个文件
**解决**:
```bash
# 拉取最新代码
git pull origin data-structures

# 解决冲突后
git add .
git commit -m "fix: 解决合并冲突"
git push origin data-structures
```

### 问题2: 误提交到错误分支
**解决**:
```bash
# 撤销最后一次提交但保留修改
git reset --soft HEAD~1

# 切换到正确分支
git checkout data-structures

# 重新提交
git add .
git commit -m "feat: 正确的提交信息"
```

### 问题3: 分支落后于main
**解决**:
```bash
# 切换到main分支并更新
git checkout main
git pull origin main

# 切换回开发分支并合并
git checkout data-structures
git merge main

# 解决可能的冲突后推送
git push origin data-structures
```

## 📊 分支状态检查

### 检查分支同步状态
```bash
# 查看本地分支与远程分支的差异
git status

# 查看提交历史
git log --oneline --graph

# 查看分支图
git log --oneline --graph --all
```

### 检查代码质量
```bash
# 编译检查
javac -d bin src/com/socialnetwork/**/*.java

# 运行测试（如果有）
java -cp bin org.junit.runner.JUnitCore TestSuite
```

## 🎉 成功标志

### 分支管理成功的标志
- ✅ 每个小组都有自己的独立分支
- ✅ 每日代码及时提交和推送
- ✅ Pull Request描述清晰完整
- ✅ 代码合并过程顺利无冲突
- ✅ 主分支始终保持稳定可用

### 项目完成标志
- ✅ 所有功能模块开发完成
- ✅ 单元测试覆盖率达标
- ✅ 界面交互流畅
- ✅ 文档完整清晰
- ✅ 项目可以成功运行

---

**最后更新**: 2024-12-01  
**文档版本**: v1.0