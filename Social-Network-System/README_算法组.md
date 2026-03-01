# 算法组 - 开发文档

## 👥 小组信息
- **小组名称**: 算法组
- **负责成员**: 1人
- **开发周期**: 4天
- **GitHub分支**: `algorithms`

## 📋 负责模块

### 核心算法
1. **GraphAlgorithm.java** - 图算法实现
2. **RecommendationEngine.java** - 智能推荐算法
3. **DataLoader.java** - 数据加载器

## 🎯 开发目标

### 第1天目标
- [ ] 设计算法接口规范
- [ ] 实现基础BFS算法
- [ ] 创建基础测试用例

### 第2天目标  
- [ ] 实现社交距离计算
- [ ] 开发推荐算法基础
- [ ] 性能测试和优化

### 第3天目标
- [ ] 与GUI组功能集成
- [ ] 算法性能测试
- [ ] 推荐算法完善

### 第4天目标
- [ ] 最终算法优化
- [ ] 合并到main分支
- [ ] 算法文档完善

## 🔧 技术实现要求

### GraphAlgorithm.java 功能要求
```java
// 人脉查询算法
List<Integer> getFirstDegreeFriends(int userId)      // 一度人脉
List<Integer> getSecondDegreeFriends(int userId)     // 二度人脉
List<Integer> getMultiDegreeFriends(int userId, int degree) // 多度人脉

// 社交距离算法
int calculateSocialDistance(int user1, int user2)    // 最短距离
List<Integer> getShortestPath(int user1, int user2)  // 最短路径

// 性能要求
// BFS时间复杂度: O(V+E)
// 空间复杂度: O(V)
```

### RecommendationEngine.java 功能要求
```java
// 推荐算法
List<RecommendedUser> recommendUsers(int userId, int k) // 推荐k个用户

// 相似度计算
double calculateInterestSimilarity(User user1, User user2)
List<Integer> getCommonFriends(int user1, int user2)

// 推荐理由生成
String generateRecommendationReason(User target, User current)
```

### DataLoader.java 功能要求
```java
// 数据加载
void loadFromFiles(String usersFile, String friendshipsFile, 
                   HashTable userTable, AdjacencyList graph)

// 数据验证
boolean validateUserData(String line)
boolean validateFriendshipData(String line)

// 异常处理
// 文件不存在、格式错误、数据不一致等
```

## 📊 性能测试要求

### 测试数据集
- **小数据集**: 10个用户，15个关系
- **中数据集**: 50个用户，100个关系  
- **大数据集**: 200个用户，500个关系

### 性能指标
```java
// BFS算法性能
- 时间复杂度: O(V+E)
- 空间复杂度: O(V)
- 实际运行时间: <100ms (小数据集)

// 推荐算法性能
- 响应时间: <500ms
- 内存使用: 合理控制
- 准确率: >80%
```

### 测试用例设计
```java
// GraphAlgorithm测试
@Test void testFirstDegreeFriends()
@Test void testSecondDegreeFriends()
@Test void testSocialDistance()
@Test void testShortestPath()

// RecommendationEngine测试
@Test void testRecommendationAccuracy()
@Test void testInterestSimilarity()
@Test void testCommonFriends()

// DataLoader测试
@Test void testDataLoading()
@Test void testDataValidation()
@Test void testErrorHandling()
```

## 🔄 接口规范

### 与数据结构组接口
```java
// 依赖数据结构组提供的方法
List<Integer> getFriends(int userId)        // 从AdjacencyList
User getUserInfo(int userId)               // 从HashTable
List<Integer> getAllUsers()                // 从AdjacencyList
```

### 与GUI组接口
```java
// 提供给GUI组的方法
List<Integer> queryFriends(int userId, int degree) // 人脉查询
int calculateDistance(int user1, int user2)        // 距离计算
List<RecommendedUser> getRecommendations(int userId, int count) // 推荐
```

## 🎯 算法优化策略

### BFS算法优化
1. **访问标记**: 避免重复访问
2. **队列优化**: 使用LinkedList
3. **提前终止**: 找到目标后立即返回

### 推荐算法优化
1. **缓存机制**: 缓存相似度计算结果
2. **并行计算**: 多线程处理推荐
3. **增量更新**: 只计算变化部分

### 数据加载优化
1. **批量处理**: 减少IO操作
2. **内存映射**: 大文件处理
3. **数据验证**: 提前发现错误

## 📈 质量保证

### 代码质量
- **算法正确性**: 100%通过测试用例
- **代码可读性**: 清晰注释和命名
- **异常处理**: 完善的错误处理机制

### 性能质量
- **响应时间**: 满足用户交互要求
- **内存使用**: 合理的内存管理
- **可扩展性**: 支持更大数据集

## 🔧 开发工具

### 性能分析工具
- **JProfiler**: 内存和CPU分析
- **VisualVM**: JVM监控
- **JMH**: 微基准测试

### 测试工具
- **JUnit**: 单元测试框架
- **Mockito**: 模拟对象测试
- **AssertJ**: 断言库

## 📝 开发规范

### 算法实现规范
```java
// 方法注释模板
/**
 * 计算两个用户的社交距离
 * @param user1 用户1ID
 * @param user2 用户2ID
 * @return 社交距离，-1表示无关联
 * @throws IllegalArgumentException 用户不存在
 */
public int calculateSocialDistance(int user1, int user2) {
    // 实现代码
}
```

### 测试规范
- 每个公有方法都要有测试用例
- 测试边界条件和异常情况
- 测试性能指标

## 🚀 开发里程碑

### 里程碑1: 基础算法实现（第2天）
- [ ] BFS算法完整实现
- [ ] 社交距离计算正确
- [ ] 基础推荐功能

### 里程碑2: 性能优化（第3天）
- [ ] 算法性能达标
- [ ] 推荐准确率>80%
- [ ] 异常处理完善

### 里程碑3: 集成测试（第4天）
- [ ] 与GUI组集成成功
- [ ] 所有功能测试通过
- [ ] 性能测试报告完成

---

**最后更新**: 2024-12-01  
**文档版本**: v1.0