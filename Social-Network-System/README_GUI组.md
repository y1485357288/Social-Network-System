# GUI组 - 开发文档

## 👥 小组信息
- **小组名称**: GUI组
- **负责成员**: 1人
- **开发周期**: 4天
- **GitHub分支**: `gui-development`

## 📋 负责模块

### 图形用户界面
1. **Launcher.java** - 启动器界面
2. **SocialNetworkApp.java** - 新版主界面
3. **VisualizationPanel.java** - 可视化组件
4. **MainWindow.java** - 旧版界面（兼容）

## 🎯 开发目标

### 第1天目标
- [ ] 搭建主窗口框架
- [ ] 设计界面布局
- [ ] 创建基础组件

### 第2天目标  
- [ ] 实现功能面板
- [ ] 添加事件绑定
- [ ] 界面美化优化

### 第3天目标
- [ ] 集成数据结构和算法
- [ ] 用户交互逻辑实现
- [ ] 界面功能测试

### 第4天目标
- [ ] 最终界面优化
- [ ] 用户体验测试
- [ ] 项目文档完善

## 🎨 界面设计要求

### 整体布局设计
```
┌─────────────────────────────────────────────────────────────┐
│ 社交网络图谱分析系统 │ 选择用户: [下拉框]   [加载数据]       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  功能菜单区域                 结果展示区域                  │
│  ┌─────────────────┐         ┌─────────────────┐           │
│  │   一度人脉       │         │                 │           │
│  │ 查询直接好友列表 │         │   分析结果...    │           │
│  └─────────────────┘         └─────────────────┘           │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│             社交网络可视化区域（带缩放和拖拽）               │
└─────────────────────────────────────────────────────────────┘
```

### 颜色方案
- **主色调**: 蓝色系 (#4682B4)
- **辅助色**: 绿色、橙色、紫色等
- **文字色**: 黑色/白色对比
- **背景色**: 浅灰色/白色

### 字体规范
- **标题字体**: 微软雅黑，20px，粗体
- **正文字体**: 微软雅黑，12-14px
- **按钮字体**: 微软雅黑，12px

## 🔧 技术实现要求

### SocialNetworkApp.java 功能要求
```java
// 界面组件
private JComboBox<Integer> userComboBox          // 用户选择
private JTextArea resultArea                    // 结果展示
private VisualizationPanel visualizationPanel   // 可视化组件

// 功能按钮
private JButton[] functionButtons               // 6个功能按钮
private JButton loadButton                      // 数据加载按钮

// 事件处理
void loadData()                                 // 数据加载
void queryFirstDegreeFriends()                  // 一度人脉
void querySecondDegreeFriends()                 // 二度人脉
void calculateSocialDistance()                  // 社交距离
void recommendUsers()                           // 智能推荐
void queryMultiDegreeFriends()                  // 多度人脉
void updateVisualization()                      // 可视化更新
```

### VisualizationPanel.java 功能要求
```java
// 图形绘制
void paintComponent(Graphics g)                 // 绘制图形
void generateNodePositions()                    // 节点布局

// 交互功能
void setCenterUser(int userId)                  // 设置中心用户
int findUserAt(Point point)                     // 点击检测

// 缩放拖拽
double scale = 1.0                              // 缩放比例
int offsetX, offsetY                            // 偏移量
void handleMouseWheel(MouseWheelEvent e)        // 滚轮缩放
void handleMouseDrag(MouseEvent e)              // 拖拽移动
```

### 界面响应要求
- **按钮点击**: 响应时间 < 100ms
- **数据加载**: 显示进度条
- **图形渲染**: 流畅不卡顿
- **用户交互**: 即时反馈

## 🎮 用户交互设计

### 数据加载流程
1. 点击"加载数据"按钮
2. 显示进度对话框
3. 选择用户数据文件
4. 选择好友关系文件
5. 显示加载结果统计

### 功能分析流程
1. 从下拉框选择用户
2. 点击左侧功能按钮
3. 在中间区域显示结果
4. 底部可视化同步更新

### 可视化操作
1. **缩放**: 鼠标滚轮控制
2. **拖拽**: 按住左键移动
3. **点击**: 查看节点详情
4. **刷新**: 点击可视化按钮

## 🔄 接口规范

### 与算法组接口
```java
// 调用算法组功能
List<Integer> friends = graphAlgorithm.getFirstDegreeFriends(userId)
int distance = graphAlgorithm.calculateSocialDistance(user1, user2)
List<RecommendedUser> recommendations = recommendationEngine.recommendUsers(userId, k)
```

### 与数据结构组接口
```java
// 获取用户信息
User user = userTable.get(userId)
String userName = user.getName()
List<String> interests = user.getInterests()
```

### 事件处理规范
```java
// 按钮点击事件
button.addActionListener(e -> {
    // 处理逻辑
    updateUI();
});

// 鼠标事件
panel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        // 处理逻辑
    }
});
```

## 📊 用户体验指标

### 响应性能
- **界面加载**: < 2秒
- **按钮响应**: < 100ms
- **图形渲染**: 30fps以上
- **数据加载**: 显示进度反馈

### 易用性指标
- **功能发现性**: 新用户10分钟内掌握基本操作
- **操作效率**: 完成核心任务不超过3步
- **错误恢复**: 提供清晰的错误提示和恢复方法

### 可访问性
- **字体大小**: 支持系统字体缩放
- **颜色对比**: 满足WCAG 2.0标准
- **键盘操作**: 支持Tab键导航

## 🛠️ 开发工具

### UI设计工具
- **WindowBuilder**: Eclipse界面设计器
- **IntelliJ IDEA GUI Designer**: IDEA界面设计
- **Adobe XD/Figma**: 界面原型设计

### 调试工具
- **Swing Inspector**: 界面组件调试
- **JProfiler**: 性能分析
- **VisualVM**: JVM监控

### 测试工具
- **Fest Swing**: Swing自动化测试
- **JUnit**: 单元测试框架
- **AssertJ**: 断言库

## 📝 开发规范

### 代码组织规范
```java
// 类结构模板
public class SocialNetworkApp extends JFrame {
    // 1. 常量定义
    // 2. 成员变量
    // 3. 构造函数
    // 4. 初始化方法
    // 5. 事件处理方法
    // 6. 辅助方法
    // 7. 主方法
}
```

### 界面组件命名规范
```java
// 组件命名约定
JButton btnLoadData                    // 数据加载按钮
JComboBox<Integer> cmbUserSelect      // 用户选择框
JTextArea txtResult                   // 结果文本区
JPanel pnlFunctions                   // 功能面板
```

### 事件处理规范
- 使用Lambda表达式简化代码
- 避免在事件处理中进行耗时操作
- 使用SwingWorker处理后台任务

## 🚀 开发里程碑

### 里程碑1: 基础界面（第2天）
- [ ] 主窗口框架完成
- [ ] 基础组件布局正确
- [ ] 基本事件绑定

### 里程碑2: 功能完整（第3天）
- [ ] 所有功能按钮正常工作
- [ ] 可视化组件交互流畅
- [ ] 数据加载流程完善

### 里程碑3: 用户体验（第4天）
- [ ] 界面美化优化完成
- [ ] 性能测试通过
- [ ] 用户文档完善

---

**最后更新**: 2024-12-01  
**文档版本**: v1.0