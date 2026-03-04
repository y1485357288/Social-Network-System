# 社交网络图谱分析及智能推荐系统

一个基于 Java 的社交网络分析系统，支持人脉查询、社交距离计算和智能推荐功能。

## ✨ 主要功能

- **人脉分析**：一度、二度、多度好友查询
- **社交距离**：基于 BFS 的最短路径计算
- **智能推荐**：基于兴趣耦合和共同好友的推荐算法
- **数据可视化**：可缩放拖拽的社交网络图谱
- **用户管理**：添加/删除用户和好友关系
- **数据持久化**：支持 CSV 格式导入导出

## 🚀 快速开始

### 环境要求
- JDK 8 或更高版本
- 512MB 以上内存

### 运行方式

**方式 1：双击运行（推荐）**
```bash
双击 run.bat
```

**方式 2：命令行**
```bash
cd Social-Network-System
java -cp out com.socialnetwork.gui.SocialNetworkApp
```

**方式 3：IDEA**
- 打开项目，运行 `SocialNetworkApp.java`

## 📁 项目结构

```
Social-Network-System/
├── src/com/socialnetwork/
│   ├── core/          # 核心算法
│   │   ├── AdjacencyList.java
│   │   ├── GraphAlgorithm.java
│   │   ├── HashTable.java
│   │   └── RecommendationEngine.java
│   ├── data/          # 数据加载
│   │   └── DataLoader.java
│   ├── gui/           # 图形界面
│   │   ├── SocialNetworkApp.java
│   │   └── VisualizationPanel.java
│   └── model/         # 数据模型
│       └── User.java
├── run.bat            # 启动脚本
├── user_sample.csv    # 用户数据（10 个用户）
└── friend_sample.txt  # 好友关系（20 条）
```

## 📊 数据格式

### 用户数据 (CSV)
```csv
用户 ID，姓名，兴趣标签
1，张三，编程;篮球;摄影
2，李四，阅读;音乐;旅行
```

### 好友关系 (TXT)
```txt
用户 ID1，用户 ID2
1,2
1,3
2,3
```

**注意**：兴趣标签用分号 `;` 分隔

## 🎯 使用说明

1. **启动系统** - 自动加载示例数据
2. **选择用户** - 从下拉框选择要分析的用户
3. **功能分析**：
   - 一度人脉：查看直接好友
   - 二度人脉：查看好友的好友
   - 社交距离：计算用户间距离
   - 智能推荐：获取个性化推荐
   - 可视化：查看网络图谱
4. **用户管理** - 添加/删除用户
5. **好友管理** - 添加/删除好友关系
6. **可视化操作**：
   - 鼠标滚轮：缩放（0.5x-3.0x）
   - 拖拽：移动视图
   - 点击边：删除好友关系

## 🛠️ 技术实现

| 模块 | 技术 | 说明 |
|------|------|------|
| 数据结构 | 邻接表、哈希表 | 图结构存储、用户信息管理 |
| 算法 | BFS、推荐算法 | 最短路径、智能推荐 |
| GUI | Java Swing | 图形界面、可视化 |
| 数据 | CSV 文件 | 数据持久化 |

## 📝 开发说明

### 核心类

- **AdjacencyList** - 邻接表图结构
- **HashTable** - 用户信息存储
- **GraphAlgorithm** - BFS 算法实现
- **RecommendationEngine** - 推荐引擎
- **SocialNetworkApp** - 主窗口
- **VisualizationPanel** - 可视化组件

### 编译项目
```bash
cd Social-Network-System
javac -d out -encoding UTF-8 src/com/socialnetwork/**/*.java
```

## 🤝 团队协作

本项目采用分支开发模式：

| 小组 | 分支 | 负责模块 |
|------|------|----------|
| 数据结构组 | `data-structures` | 邻接表、哈希表 |
| 算法组 | `algorithms` | BFS、推荐算法 |
| GUI 组 | `GUI` | 界面设计、可视化 |

## 📄 说明

- **开发语言**: Java
- **开发时间**: 2026 年
- **最后更新**: 2026-03-01
- **许可证**: MIT

## ❓ 常见问题

**Q: 数据会保存吗？**  
A: 删除操作会自动保存到文件，下次启动仍然有效。

**Q: 如何导入新数据？**  
A: 点击"加载数据"按钮选择数据文件，或直接替换 `user_sample.csv` 和 `friend_sample.txt`。

**Q: 可视化如何操作？**  
A: 鼠标滚轮缩放，左键拖拽移动，点击边可删除好友关系。

---

**开发团队**: 山口组
