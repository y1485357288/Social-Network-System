package com.socialnetwork.gui;

import com.socialnetwork.core.AdjacencyList;
import com.socialnetwork.core.GraphAlgorithm;
import com.socialnetwork.core.HashTable;
import com.socialnetwork.core.RecommendationEngine;
import com.socialnetwork.data.DataLoader;
import com.socialnetwork.model.User;
import com.socialnetwork.gui.VisualizationPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainWindow extends JFrame {
    private AdjacencyList graph;
    private HashTable userTable;
    private GraphAlgorithm graphAlgorithm;
    private RecommendationEngine recommendationEngine;
    
    private JTabbedPane tabbedPane;
    private JTextArea resultArea;
    
    public MainWindow() {
        graph = new AdjacencyList();
        userTable = new HashTable();
        graphAlgorithm = new GraphAlgorithm(graph);
        recommendationEngine = new RecommendationEngine(graph, userTable);
        
        initUI();
    }
    
    private void initUI() {
        setTitle("社交网络图谱分析系统");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        
        // 数据加载选项卡
        JPanel loadPanel = createLoadPanel();
        tabbedPane.addTab("数据加载", loadPanel);
        
        // 一度人脉查询选项卡
        JPanel firstDegreePanel = createFirstDegreePanel();
        tabbedPane.addTab("一度人脉查询", firstDegreePanel);
        
        // 二度人脉查询选项卡
        JPanel secondDegreePanel = createSecondDegreePanel();
        tabbedPane.addTab("二度人脉查询", secondDegreePanel);
        
        // 社交距离计算选项卡
        JPanel distancePanel = createDistancePanel();
        tabbedPane.addTab("社交距离计算", distancePanel);
        
        // 智能推荐选项卡
        JPanel recommendationPanel = createRecommendationPanel();
        tabbedPane.addTab("智能推荐", recommendationPanel);
        
        // 可视化选项卡
        JPanel visualizationPanel = createVisualizationPanel();
        tabbedPane.addTab("社交网络可视化", visualizationPanel);
        
        // 结果展示区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private JPanel createLoadPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户文件:"), gbc);
        
        JTextField userFileField = new JTextField(30);
        userFileField.setText("users.csv");
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(userFileField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("好友关系文件:"), gbc);
        
        JTextField friendshipFileField = new JTextField(30);
        friendshipFileField.setText("friendships.csv");
        gbc.gridx = 1;
        panel.add(friendshipFileField, gbc);
        
        JButton loadButton = new JButton("加载数据");
        loadButton.addActionListener(e -> {
            try {
                DataLoader.loadFromFiles(userFileField.getText(), friendshipFileField.getText(), userTable, graph);
                resultArea.setText("数据加载成功！\n");
                resultArea.append("用户数量: " + userTable.size() + "\n");
                resultArea.append("好友关系数量: " + graph.getEdgeCount() + "\n");
            } catch (IOException ex) {
                resultArea.setText("数据加载失败: " + ex.getMessage() + "\n");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loadButton, gbc);
        
        return panel;
    }
    
    private JPanel createFirstDegreePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户ID:"), gbc);
        
        JTextField userIdField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);
        
        JButton queryButton = new JButton("查询一度人脉");
        queryButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                if (!userTable.containsKey(userId)) {
                    resultArea.setText("用户ID不存在！\n");
                    return;
                }
                
                List<Integer> friends = graphAlgorithm.getFirstDegreeFriends(userId);
                resultArea.setText("一度人脉查询结果:\n");
                resultArea.append("用户: " + userTable.get(userId).getName() + " (ID: " + userId + ")\n");
                resultArea.append("直接好友数量: " + friends.size() + "\n\n");
                
                for (int friendId : friends) {
                    User friend = userTable.get(friendId);
                    if (friend != null) {
                        resultArea.append("ID: " + friendId + ", 姓名: " + friend.getName() + "\n");
                    }
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("请输入有效的用户ID！\n");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(queryButton, gbc);
        
        return panel;
    }
    
    private JPanel createSecondDegreePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户ID:"), gbc);
        
        JTextField userIdField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);
        
        JButton queryButton = new JButton("查询二度人脉");
        queryButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                if (!userTable.containsKey(userId)) {
                    resultArea.setText("用户ID不存在！\n");
                    return;
                }
                
                List<Integer> secondDegreeFriends = graphAlgorithm.getSecondDegreeFriends(userId);
                resultArea.setText("二度人脉查询结果:\n");
                resultArea.append("用户: " + userTable.get(userId).getName() + " (ID: " + userId + ")\n");
                resultArea.append("二度人脉数量: " + secondDegreeFriends.size() + "\n\n");
                
                for (int friendId : secondDegreeFriends) {
                    User friend = userTable.get(friendId);
                    if (friend != null) {
                        resultArea.append("ID: " + friendId + ", 姓名: " + friend.getName() + "\n");
                    }
                }
            } catch (NumberFormatException ex) {
                resultArea.setText("请输入有效的用户ID！\n");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(queryButton, gbc);
        
        return panel;
    }
    
    private JPanel createDistancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("起始用户ID:"), gbc);
        
        JTextField startIdField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(startIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("目标用户ID:"), gbc);
        
        JTextField endIdField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(endIdField, gbc);
        
        JButton calculateButton = new JButton("计算社交距离");
        calculateButton.addActionListener(e -> {
            try {
                int startId = Integer.parseInt(startIdField.getText());
                int endId = Integer.parseInt(endIdField.getText());
                
                if (!userTable.containsKey(startId)) {
                    resultArea.setText("起始用户ID不存在！\n");
                    return;
                }
                if (!userTable.containsKey(endId)) {
                    resultArea.setText("目标用户ID不存在！\n");
                    return;
                }
                
                int distance = graphAlgorithm.calculateSocialDistance(startId, endId);
                List<Integer> path = graphAlgorithm.getShortestPath(startId, endId);
                
                resultArea.setText("社交距离计算结果:\n");
                resultArea.append("起始用户: " + userTable.get(startId).getName() + " (ID: " + startId + ")\n");
                resultArea.append("目标用户: " + userTable.get(endId).getName() + " (ID: " + endId + ")\n");
                
                if (distance == -1) {
                    resultArea.append("社交距离: 无社交关联\n");
                } else {
                    resultArea.append("社交距离: " + distance + "\n");
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
            } catch (NumberFormatException ex) {
                resultArea.setText("请输入有效的用户ID！\n");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(calculateButton, gbc);
        
        return panel;
    }
    
    private JPanel createRecommendationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("用户ID:"), gbc);
        
        JTextField userIdField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("推荐数量K:"), gbc);
        
        JTextField kField = new JTextField(5);
        kField.setText("5");
        gbc.gridx = 1;
        panel.add(kField, gbc);
        
        JButton recommendButton = new JButton("获取推荐");
        recommendButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int k = Integer.parseInt(kField.getText());
                
                if (!userTable.containsKey(userId)) {
                    resultArea.setText("用户ID不存在！\n");
                    return;
                }
                
                List<RecommendationEngine.RecommendedUser> recommendations = recommendationEngine.recommendUsers(userId, k);
                resultArea.setText("智能推荐结果:\n");
                resultArea.append("用户: " + userTable.get(userId).getName() + " (ID: " + userId + ")\n");
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
            } catch (NumberFormatException ex) {
                resultArea.setText("请输入有效的用户ID和推荐数量！\n");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(recommendButton, gbc);
        
        return panel;
    }
    
    private JPanel createVisualizationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // 顶部控制面板
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("中心用户ID:"));
        JTextField userIdField = new JTextField(10);
        controlPanel.add(userIdField);
        
        JButton updateButton = new JButton("更新可视化");
        controlPanel.add(updateButton);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        
        // 可视化面板
        VisualizationPanel visualizationPanel = new VisualizationPanel(graph, userTable);
        panel.add(visualizationPanel, BorderLayout.CENTER);
        
        // 按钮事件
        updateButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                if (!userTable.containsKey(userId)) {
                    resultArea.setText("用户ID不存在！\n");
                    return;
                }
                visualizationPanel.setCenterUser(userId);
                resultArea.setText("可视化已更新，中心用户: " + userTable.get(userId).getName() + " (ID: " + userId + ")\n");
            } catch (NumberFormatException ex) {
                resultArea.setText("请输入有效的用户ID！\n");
            }
        });
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}