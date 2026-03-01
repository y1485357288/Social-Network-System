# 数据结构组 - 开发文档

## 👥 小组信息
- **小组名称**: 数据结构组
- **负责成员**: 1人
- **开发周期**: 4天
- **GitHub分支**: `data-structures`

## 📋 负责模块

### 核心数据结构
1. **AdjacencyList.java** - 邻接表图结构
2. **HashTable.java** - 哈希表数据结构  
3. **User.java** - 用户数据模型

## 🎯 开发目标

### 第1天目标
- [ ] 邻接表基础框架搭建
- [ ] 哈希表基础功能实现
- [ ] 基础单元测试编写

### 第2天目标  
- [ ] 邻接表完整功能实现
- [ ] 哈希表性能优化
- [ ] 完整单元测试覆盖

### 第3天目标
- [ ] 与算法组接口对接
- [ ] 性能测试和优化
- [ ] 文档编写

### 第4天目标
- [ ] 最终代码整理
- [ ] 合并到main分支
- [ ] 项目文档完善

## 🔧 技术实现要求

### AdjacencyList.java 功能要求
```java
// 基础功能
void addUser(int userId)
void addFriendship(int user1, int user2)
List<Integer> getFriends(int userId)
List<Integer> getAllUsers()
boolean hasUser(int userId)
int getEdgeCount()

// 高级功能（可选）
void removeUser(int userId)
void removeFriendship(int user1, int user2)
int getDegree(int userId)
```

### HashTable.java 功能要求
```java
// 基础功能
void put(int key, User value)
User get(int key)
int size()
boolean containsKey(int key)

// 性能要求
// 负载因子控制在0.75以下
// 使用链地址法解决冲突
```

### User.java 数据模型
```java
public class User {
    private int id;
    private String name;
    private List<String> interests;
    
    // 构造函数、getter/setter方法
}
```

## 📊 单元测试要求

### 测试覆盖率目标
- **行覆盖率**: ≥90%
- **分支覆盖率**: ≥85%
- **方法覆盖率**: 100%

### 测试用例设计
```java
// AdjacencyList测试
@Test void testAddUser()
@Test void testAddFriendship() 
@Test void testGetFriends()
@Test void testGetAllUsers()
@Test void testHasUser()
@Test void testEdgeCount()

// HashTable测试
@Test void testPutAndGet()
@Test void testSize()
@Test void testContainsKey()
@Test void testCollisionHandling()
```

## 📈 性能指标

### 时间复杂度要求
- **邻接表操作**: O(1) 或 O(degree)
- **哈希表操作**: 平均O(1)
- **内存使用**: 合理控制，避免内存泄漏

### 内存使用监控
- 使用Java VisualVM监控内存
- 避免循环引用
- 及时释放无用对象

## 🔄 接口规范

### 与算法组接口
```java
// 提供给算法组的方法
List<Integer> getFriends(int userId)        // 一度人脉
List<Integer> getAllUsers()                 // 所有用户
boolean hasUser(int userId)                 // 用户存在性检查
```

### 与GUI组接口
```java
// 提供给GUI组的方法
User getUserInfo(int userId)               // 用户详细信息
int getUserCount()                         // 用户数量统计
```

## 📝 开发规范

### 代码规范
- 遵循Java命名规范
- 每个方法都要有注释
- 使用有意义的变量名
- 避免魔法数字

### 提交规范
```
feat: 添加新功能
fix: 修复bug  
docs: 文档更新
test: 测试相关
refactor: 代码重构
```

## 🚀 开发环境

### 必备工具
- JDK 8+
- IDE (IntelliJ IDEA推荐)
- JUnit 4/5
- Git

### 推荐插件
- Checkstyle (代码规范检查)
- JaCoCo (测试覆盖率)
- SonarLint (代码质量)

## 📞 协作方式

### 每日站会
- **时间**: 每天上午9:00
- **内容**: 昨日进展、今日计划、遇到的问题

### 代码审查
- 提交Pull Request前必须经过审查
- 重点关注算法正确性和性能
- 确保单元测试通过

### 问题解决
- 遇到技术问题先在小组内讨论
- 无法解决的问题及时上报
- 保持代码库的稳定性

---

**最后更新**: 2024-12-01  
**文档版本**: v1.0