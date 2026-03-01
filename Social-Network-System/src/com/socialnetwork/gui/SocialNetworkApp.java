package com.socialnetwork.gui;

import com.socialnetwork.core.AdjacencyList;
import com.socialnetwork.core.GraphAlgorithm;
import com.socialnetwork.core.HashTable;
import com.socialnetwork.core.RecommendationEngine;
import com.socialnetwork.data.DataLoader;
import com.socialnetwork.model.User;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class SocialNetworkApp extends JFrame {
    private AdjacencyList graph;
    private HashTable userTable;
    private GraphAlgorithm graphAlgorithm;
    private RecommendationEngine recommendationEngine;
    
    // UI组件
    private JComboBox<Integer> userComboBox;
    private JTextArea resultArea;
    private JPanel functionPanel;
    private VisualizationPanel visualizationPanel;
    private int currentUserId = -1;
    
    public SocialNetworkApp() {
        graph = new AdjacencyList();
        userTable = new HashTable();
        graphAlgorithm = new GraphAlgorithm(graph);
        recommendationEngine = new RecommendationEngine(graph, userTable);
        
        initUI();
        loadSampleData();
    }
    
    private void initUI() {
        setTitle("社交网络图谱分析系统");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 设置现代外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 240, 240));
        
        // 顶部控制面板
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // 中间功能面板
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        
        // 左侧功能按钮面板
        functionPanel = createFunctionPanel();
        centerPanel.add(functionPanel, BorderLayout.WEST);
        
        // 右侧结果展示区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultArea.setBackground(new Color(255, 255, 255));
        resultArea.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(400, 0));
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // 底部可视化面板（带滚动条）
        visualizationPanel = new VisualizationPanel(graph, userTable);
        visualizationPanel.setPreferredSize(new Dimension(1200, 600)); // 设置更大的默认尺寸
        
        JScrollPane visualizationScrollPane = new JScrollPane(visualizationPanel);
        visualizationScrollPane.setPreferredSize(new Dimension(0, 400));
        visualizationScrollPane.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(new Color(100, 149, 237), 2), "社交网络可视化", 
                TitledBorder.CENTER, TitledBorder.TOP, 
                new Font("微软雅黑", Font.BOLD, 14), new Color(70, 130, 180)),
            new EmptyBorder(5, 5, 5, 5)
        ));
        visualizationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        visualizationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        mainPanel.add(visualizationScrollPane, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // 创建左侧面板用于标题和用户选择
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(new Color(70, 130, 180));
        
        // 标题
        JLabel titleLabel = new JLabel("社交网络图谱分析系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        // 添加分隔符
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setForeground(new Color(255, 255, 255, 100));
        separator.setPreferredSize(new Dimension(2, 30));
        leftPanel.add(separator);
        
        // 用户选择
        JLabel userLabel = new JLabel("选择用户:");
        userLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        userLabel.setForeground(Color.WHITE);
        leftPanel.add(userLabel);
        
        userComboBox = new JComboBox<>();
        userComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userComboBox.setPreferredSize(new Dimension(120, 30));
        userComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userComboBox.getSelectedItem() != null) {
                    currentUserId = (Integer) userComboBox.getSelectedItem();
                    updateUserInfo();
                    visualizationPanel.setCenterUser(currentUserId);
                }
            }
        });
        leftPanel.add(userComboBox);
        
        // 数据加载按钮 - 添加描述
        JButton loadButton = createFunctionButton("加载数据", "导入用户和关系数据", new Color(46, 204, 113));
        loadButton.addActionListener(e -> loadData());
        
        // 创建右侧面板用于放置加载按钮
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(new Color(70, 130, 180));
        rightPanel.add(loadButton);
        
        // 将左侧面板和加载按钮添加到顶部面板
        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    private JPanel createFunctionPanel() {
        JPanel functionPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        functionPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(new Color(100, 149, 237), 2), "功能菜单", 
                TitledBorder.CENTER, TitledBorder.TOP, 
                new Font("微软雅黑", Font.BOLD, 14), new Color(70, 130, 180)),
            new EmptyBorder(15, 15, 15, 15)
        ));
        functionPanel.setPreferredSize(new Dimension(250, 0));
        functionPanel.setBackground(Color.WHITE);
        
        // 创建功能按钮 - 使用文字图标避免Emoji问题
        JButton firstDegreeBtn = createFunctionButton("一度人脉", "查询直接好友列表", new Color(52, 152, 219));
        JButton secondDegreeBtn = createFunctionButton("二度人脉", "查询好友的好友", new Color(155, 89, 182));
        JButton distanceBtn = createFunctionButton("社交距离", "计算最短路径", new Color(241, 196, 15));
        JButton recommendBtn = createFunctionButton("智能推荐", "基于兴趣匹配", new Color(230, 126, 34));
        JButton multiDegreeBtn = createFunctionButton("多度人脉", "自定义查询深度", new Color(231, 76, 60));
        JButton visualizeBtn = createFunctionButton("可视化", "刷新网络图谱", new Color(26, 188, 156));
        
        // 添加按钮事件
        firstDegreeBtn.addActionListener(e -> queryFirstDegreeFriends());
        secondDegreeBtn.addActionListener(e -> querySecondDegreeFriends());
        distanceBtn.addActionListener(e -> calculateSocialDistance());
        recommendBtn.addActionListener(e -> recommendUsers());
        multiDegreeBtn.addActionListener(e -> queryMultiDegreeFriends());
        visualizeBtn.addActionListener(e -> updateVisualization());
        
        // 添加按钮到面板
        functionPanel.add(firstDegreeBtn);
        functionPanel.add(secondDegreeBtn);
        functionPanel.add(distanceBtn);
        functionPanel.add(recommendBtn);
        functionPanel.add(multiDegreeBtn);
        functionPanel.add(visualizeBtn);
        
        return functionPanel;
    }
    
    private JButton createFunctionButton(String title, String description, Color color) {
        // 创建一个包含图标、标题和描述的面板
        JPanel buttonPanel = new JPanel(new BorderLayout(2, 2));
        buttonPanel.setBackground(color);
        buttonPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(255, 255, 255, 50), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        buttonPanel.setPreferredSize(new Dimension(180, 60)); // 固定按钮尺寸
        
        // 标题标签
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);
        
        // 描述标签
        JLabel descLabel = new JLabel(description, JLabel.CENTER);
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 9));
        descLabel.setForeground(new Color(240, 240, 240));
        
        // 添加到面板
        buttonPanel.add(titleLabel, BorderLayout.NORTH);
        buttonPanel.add(descLabel, BorderLayout.CENTER);
        
        // 创建透明按钮覆盖整个面板
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel, BorderLayout.CENTER);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                buttonPanel.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(color.darker(), 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void loadSampleData() {
        try {
            DataLoader.loadFromFiles("users.csv", "friendships.csv", userTable, graph);
            updateUserComboBox();
            resultArea.setText("✓ 示例数据加载成功！\n\n");
                resultArea.append("系统统计信息：\n");
                resultArea.append("   用户数量: " + userTable.size() + "\n");
                resultArea.append("   好友关系数量: " + graph.getEdgeCount() + "\n\n");
                resultArea.append("提示: 请从上方选择用户开始分析...");
        } catch (IOException e) {
            resultArea.setText("✗ 数据加载失败: " + e.getMessage() + "\n");
        }
    }
    
    private void loadData() {
        // 创建进度对话框
        JDialog progressDialog = new JDialog(this, "数据加载中...", true);
        progressDialog.setSize(300, 150);
        progressDialog.setLocationRelativeTo(this);
        
        JPanel progressPanel = new JPanel(new BorderLayout(10, 10));
        progressPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel progressLabel = new JLabel("正在加载数据，请稍候...", JLabel.CENTER);
        progressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        progressPanel.add(progressLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressDialog.add(progressPanel);
        
        // 在新线程中执行数据加载
        Thread loadThread = new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
                
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("选择用户数据文件");
                
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String usersFile = fileChooser.getSelectedFile().getPath();
                    
                    fileChooser.setDialogTitle("选择好友关系数据文件");
                    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        String friendshipsFile = fileChooser.getSelectedFile().getPath();
                        
                        DataLoader.loadFromFiles(usersFile, friendshipsFile, userTable, graph);
                        
                        SwingUtilities.invokeLater(() -> {
                            updateUserComboBox();
                            resultArea.setText("✓ 数据加载成功！\n\n");
                            resultArea.append("系统统计信息：\n");
                            resultArea.append("   用户数量: " + userTable.size() + "\n");
                            resultArea.append("   好友关系数量: " + graph.getEdgeCount() + "\n");
                            progressDialog.dispose();
                        });
                    } else {
                        SwingUtilities.invokeLater(progressDialog::dispose);
                    }
                } else {
                    SwingUtilities.invokeLater(progressDialog::dispose);
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    resultArea.setText("✗ 数据加载失败: " + e.getMessage() + "\n");
                    progressDialog.dispose();
                });
            }
        });
        
        loadThread.start();
    }
    
    private void updateUserComboBox() {
        userComboBox.removeAllItems();
        for (int userId : graph.getAllUsers()) {
            userComboBox.addItem(userId);
        }
        if (userComboBox.getItemCount() > 0) {
            userComboBox.setSelectedIndex(0);
        }
    }
    
    private void updateUserInfo() {
        if (currentUserId != -1) {
            User user = userTable.get(currentUserId);
            if (user != null) {
                resultArea.setText("当前用户信息：\n");
                resultArea.append("   ID: " + currentUserId + "\n");
                resultArea.append("   姓名: " + user.getName() + "\n");
                resultArea.append("   兴趣: " + (user.getInterests() != null ? String.join(", ", user.getInterests()) : "无") + "\n");
                resultArea.append("   好友数量: " + graph.getFriends(currentUserId).size() + "\n\n");
                resultArea.append("提示: 请选择左侧功能进行分析...");
            }
        }
    }
    
    // 各种功能方法的实现...
    private void queryFirstDegreeFriends() {
        if (currentUserId == -1) {
            resultArea.setText("✗ 请先选择用户！");
            return;
        }
        
        List<Integer> friends = graphAlgorithm.getFirstDegreeFriends(currentUserId);
        resultArea.setText("一度人脉查询结果：\n\n");
        resultArea.append("用户: " + userTable.get(currentUserId).getName() + " (ID: " + currentUserId + ")\n");
        resultArea.append("直接好友数量: " + friends.size() + "\n\n");
        
        for (int i = 0; i < friends.size(); i++) {
            int friendId = friends.get(i);
            User friend = userTable.get(friendId);
            if (friend != null) {
                resultArea.append((i + 1) + ". ID: " + friendId + ", 姓名: " + friend.getName() + "\n");
            }
        }
    }
    
    private void querySecondDegreeFriends() {
        if (currentUserId == -1) {
            resultArea.setText("✗ 请先选择用户！");
            return;
        }
        
        List<Integer> secondDegreeFriends = graphAlgorithm.getSecondDegreeFriends(currentUserId);
        resultArea.setText("二度人脉查询结果：\n\n");
        resultArea.append("用户: " + userTable.get(currentUserId).getName() + " (ID: " + currentUserId + ")\n");
        resultArea.append("二度人脉数量: " + secondDegreeFriends.size() + "\n\n");
        
        for (int i = 0; i < secondDegreeFriends.size(); i++) {
            int friendId = secondDegreeFriends.get(i);
            User friend = userTable.get(friendId);
            if (friend != null) {
                resultArea.append((i + 1) + ". ID: " + friendId + ", 姓名: " + friend.getName() + "\n");
            }
        }
    }
    
    private void calculateSocialDistance() {
        if (currentUserId == -1) {
            resultArea.setText("✗ 请先选择用户！");
            return;
        }
        
        String targetIdStr = JOptionPane.showInputDialog(this, "请输入目标用户ID:", "社交距离计算", JOptionPane.QUESTION_MESSAGE);
        if (targetIdStr != null && !targetIdStr.trim().isEmpty()) {
            try {
                int targetId = Integer.parseInt(targetIdStr.trim());
                
                if (!userTable.containsKey(targetId)) {
                    resultArea.setText("✗ 目标用户ID不存在！");
                    return;
                }
                
                int distance = graphAlgorithm.calculateSocialDistance(currentUserId, targetId);
                List<Integer> path = graphAlgorithm.getShortestPath(currentUserId, targetId);
                
                resultArea.setText("社交距离计算结果：\n\n");
                resultArea.append("起始用户: " + userTable.get(currentUserId).getName() + " (ID: " + currentUserId + ")\n");
                resultArea.append("目标用户: " + userTable.get(targetId).getName() + " (ID: " + targetId + ")\n\n");
                
                if (distance == -1) {
                    resultArea.append("✗ 社交距离: 无社交关联\n");
                } else {
                    resultArea.append("✓ 社交距离: " + distance + "\n");
                    resultArea.append("最短路径: ");
                    for (int i = 0; i < path.size(); i++) {
                        int userId = path.get(i);
                        resultArea.append(userTable.get(userId).getName() + " (ID: " + userId + ")");
                        if (i < path.size() - 1) {
                            resultArea.append(" → ");
                        }
                    }
                    resultArea.append("\n");
                }
            } catch (NumberFormatException e) {
                resultArea.setText("✗ 请输入有效的用户ID！");
            }
        }
    }
    
    private void recommendUsers() {
        if (currentUserId == -1) {
            resultArea.setText("✗ 请先选择用户！");
            return;
        }
        
        String kStr = JOptionPane.showInputDialog(this, "请输入推荐数量K:", "5", JOptionPane.QUESTION_MESSAGE);
        if (kStr != null && !kStr.trim().isEmpty()) {
            try {
                int k = Integer.parseInt(kStr.trim());
                
                List<RecommendationEngine.RecommendedUser> recommendations = recommendationEngine.recommendUsers(currentUserId, k);
                resultArea.setText("智能推荐结果：\n\n");
                resultArea.append("用户: " + userTable.get(currentUserId).getName() + " (ID: " + currentUserId + ")\n");
                resultArea.append("推荐数量: " + recommendations.size() + "\n\n");
                
                for (int i = 0; i < recommendations.size(); i++) {
                    RecommendationEngine.RecommendedUser recommended = recommendations.get(i);
                    User user = userTable.get(recommended.getUserId());
                    if (user != null) {
                        resultArea.append((i + 1) + ". ID: " + recommended.getUserId() + ", 姓名: " + user.getName() + "\n");
                        resultArea.append("   推荐理由: " + recommended.getReason() + "\n");
                        resultArea.append("   匹配度: " + String.format("%.2f", recommended.getScore()) + "\n\n");
                    }
                }
            } catch (NumberFormatException e) {
                resultArea.setText("✗ 请输入有效的推荐数量！");
            }
        }
    }
    
    private void queryMultiDegreeFriends() {
        if (currentUserId == -1) {
            resultArea.setText("✗ 请先选择用户！");
            return;
        }
        
        String degreeStr = JOptionPane.showInputDialog(this, "请输入查询深度:", "3", JOptionPane.QUESTION_MESSAGE);
        if (degreeStr != null && !degreeStr.trim().isEmpty()) {
            try {
                int degree = Integer.parseInt(degreeStr.trim());
                
                List<Integer> multiDegreeFriends = graphAlgorithm.getMultiDegreeFriends(currentUserId, degree);
                resultArea.setText(degree + "度人脉查询结果：\n\n");
                resultArea.append("用户: " + userTable.get(currentUserId).getName() + " (ID: " + currentUserId + ")\n");
                resultArea.append(degree + "度人脉数量: " + multiDegreeFriends.size() + "\n\n");
                
                for (int i = 0; i < multiDegreeFriends.size(); i++) {
                    int friendId = multiDegreeFriends.get(i);
                    User friend = userTable.get(friendId);
                    if (friend != null) {
                        resultArea.append((i + 1) + ". ID: " + friendId + ", 姓名: " + friend.getName() + "\n");
                    }
                }
            } catch (NumberFormatException e) {
                resultArea.setText("✗ 请输入有效的查询深度！");
            }
        }
    }
    
    private void updateVisualization() {
        if (currentUserId != -1) {
            visualizationPanel.setCenterUser(currentUserId);
            resultArea.append("\n可视化已更新，中心用户: " + userTable.get(currentUserId).getName());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SocialNetworkApp app = new SocialNetworkApp();
            app.setVisible(true);
        });
    }
}