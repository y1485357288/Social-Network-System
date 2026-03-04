package com.socialnetwork.gui;

import com.socialnetwork.core.AdjacencyList;
import com.socialnetwork.core.GraphAlgorithm;
import com.socialnetwork.core.HashTable;
import com.socialnetwork.data.DataLoader;
import com.socialnetwork.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class VisualizationPanel extends JPanel {
    private AdjacencyList graph;
    private HashTable userTable;
    private GraphAlgorithm graphAlgorithm;
    private Map<Integer, Point> nodePositions;
    private Map<Integer, Point> originalNodePositions;
    private int centerUserId;
    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;
    private Point dragStart = null;
    
    public VisualizationPanel(AdjacencyList graph, HashTable userTable) {
        this.graph = graph;
        this.userTable = userTable;
        this.graphAlgorithm = new GraphAlgorithm(graph);
        this.nodePositions = new HashMap<>();
        this.originalNodePositions = new HashMap<>();
        this.centerUserId = -1;
        
        generateNodePositions();
        
        // 添加鼠标监听器用于拖拽
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragStart = null;
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    Point current = e.getPoint();
                    offsetX += current.x - dragStart.x;
                    offsetY += current.y - dragStart.y;
                    dragStart = current;
                    repaint();
                }
            }
        });
        
        // 添加鼠标滚轮监听器用于缩放
        addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            double oldScale = scale;
            
            if (rotation < 0) {
                // 放大
                scale = Math.min(scale * 1.2, 3.0);
            } else {
                // 缩小
                scale = Math.max(scale / 1.2, 0.5);
            }
            
            // 调整偏移量以保持缩放中心
            Point mousePos = e.getPoint();
            offsetX = (int) ((offsetX - mousePos.x) * (scale / oldScale) + mousePos.x);
            offsetY = (int) ((offsetY - mousePos.y) * (scale / oldScale) + mousePos.y);
            
            repaint();
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int userId = findUserAt(e.getPoint());
                if (userId != -1) {
                    User user = userTable.get(userId);
                    if (user != null) {
                        JOptionPane.showMessageDialog(VisualizationPanel.this,
                            "用户信息:\nID: " + userId + "\n姓名: " + user.getName() + "\n" +
                            "好友数量: " + graph.getFriends(userId).size(),
                            "用户详情",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // 检查是否点击了边
                    int[] edge = findEdgeAt(e.getPoint());
                    if (edge != null) {
                        int userId1 = edge[0];
                        int userId2 = edge[1];
                        User user1 = userTable.get(userId1);
                        User user2 = userTable.get(userId2);
                        
                        if (user1 != null && user2 != null) {
                            int option = JOptionPane.showConfirmDialog(VisualizationPanel.this,
                                "确定要删除 " + user1.getName() + " 和 " + user2.getName() + " 之间的好友关系吗？",
                                "删除好友关系",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.WARNING_MESSAGE);
                            
                            if (option == JOptionPane.YES_OPTION) {
                                graph.removeFriendship(userId1, userId2);
                                repaint();
                                JOptionPane.showMessageDialog(VisualizationPanel.this,
                                    "好友关系已删除！",
                                    "操作成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                                
                                // 保存到文件
                                try {
                                    DataLoader.saveData("user_sample.csv", "friend_sample.txt", userTable, graph);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    private int findUserAt(Point point) {
        // 考虑缩放和偏移
        double inverseScale = 1.0 / scale;
        int transformedX = (int) ((point.x - offsetX) * inverseScale);
        int transformedY = (int) ((point.y - offsetY) * inverseScale);
        
        for (Map.Entry<Integer, Point> entry : nodePositions.entrySet()) {
            Point pos = entry.getValue();
            if (Math.abs(pos.x - transformedX) < 20 && Math.abs(pos.y - transformedY) < 20) {
                return entry.getKey();
            }
        }
        return -1;
    }
    
    private int[] findEdgeAt(Point point) {
        // 考虑缩放和偏移
        double inverseScale = 1.0 / scale;
        int transformedX = (int) ((point.x - offsetX) * inverseScale);
        int transformedY = (int) ((point.y - offsetY) * inverseScale);
        
        for (int userId : graph.getAllUsers()) {
            Point userPos = nodePositions.get(userId);
            if (userPos != null) {
                for (int friendId : graph.getFriends(userId)) {
                    Point friendPos = nodePositions.get(friendId);
                    if (friendPos != null && userId < friendId) { // 避免重复检查
                        if (isPointOnLine(transformedX, transformedY, userPos.x, userPos.y, friendPos.x, friendPos.y)) {
                            return new int[]{userId, friendId};
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private boolean isPointOnLine(int x, int y, int x1, int y1, int x2, int y2) {
        // 计算点到线段的距离
        double distance = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / 
            Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
        
        // 检查点是否在线段的边界内
        boolean withinX = (x >= Math.min(x1, x2) - 10 && x <= Math.max(x1, x2) + 10);
        boolean withinY = (y >= Math.min(y1, y2) - 10 && y <= Math.max(y1, y2) + 10);
        
        return distance < 10 && withinX && withinY;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // 应用缩放和偏移变换
        g2d.translate(offsetX, offsetY);
        g2d.scale(scale, scale);
        
        // 绘制边
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(1.0f / (float)scale));
        for (int userId : graph.getAllUsers()) {
            Point userPos = nodePositions.get(userId);
            if (userPos != null) {
                for (int friendId : graph.getFriends(userId)) {
                    Point friendPos = nodePositions.get(friendId);
                    if (friendPos != null && userId < friendId) { // 避免重复绘制边
                        g2d.drawLine(userPos.x, userPos.y, friendPos.x, friendPos.y);
                    }
                }
            }
        }
        
        // 绘制节点
        for (Map.Entry<Integer, Point> entry : nodePositions.entrySet()) {
            int userId = entry.getKey();
            Point pos = entry.getValue();
            
            // 确定节点颜色
            Color color;
            if (userId == centerUserId) {
                color = Color.RED;
            } else if (centerUserId != -1 && graph.getFriends(centerUserId).contains(userId)) {
                color = Color.BLUE;
            } else if (centerUserId != -1) {
                boolean isSecondDegree = false;
                for (int friendId : graph.getFriends(centerUserId)) {
                    if (graph.getFriends(friendId).contains(userId)) {
                        isSecondDegree = true;
                        break;
                    }
                }
                if (isSecondDegree) {
                    color = Color.GREEN;
                } else {
                    color = Color.GRAY;
                }
            } else {
                color = Color.GRAY;
            }
            
            // 绘制节点
            g2d.setColor(color);
            g2d.fillOval(pos.x - 10, pos.y - 10, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(pos.x - 10, pos.y - 10, 20, 20);
            
            // 绘制用户ID
            User user = userTable.get(userId);
            if (user != null) {
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2d.drawString(user.getName(), pos.x - 15, pos.y + 30);
            }
        }
        
        // 绘制缩放和拖拽提示
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        g2d.drawString("鼠标滚轮缩放 | 拖拽移动", 10, getHeight() - 20);
    }
    
    private void generateNodePositions() {
        nodePositions.clear();
        Random random = new Random(42); // 固定种子以保持一致性
        
        int width = 800;
        int height = 600;
        int centerX = width / 2;
        int centerY = height / 2;
        
        if (centerUserId != -1 && graph.hasUser(centerUserId)) {
            // 中心节点位置
            nodePositions.put(centerUserId, new Point(centerX, centerY));
            
            // 计算所有节点到中心节点的距离
            Map<Integer, Integer> distanceMap = new HashMap<>();
            for (int userId : graph.getAllUsers()) {
                if (userId != centerUserId) {
                    int distance = graphAlgorithm.calculateSocialDistance(centerUserId, userId);
                    distanceMap.put(userId, distance);
                }
            }
            
            // 按距离分组
            Map<Integer, List<Integer>> distanceGroups = new TreeMap<>();
            for (Map.Entry<Integer, Integer> entry : distanceMap.entrySet()) {
                int userId = entry.getKey();
                int distance = entry.getValue();
                
                if (distance == -1) {
                    // 无关联的节点，距离设为最大值
                    distance = Integer.MAX_VALUE;
                }
                
                distanceGroups.computeIfAbsent(distance, k -> new ArrayList<>()).add(userId);
            }
            
            // 按照距离从近到远排列节点
            int currentRadius = 0;
            for (Map.Entry<Integer, List<Integer>> entry : distanceGroups.entrySet()) {
                int distance = entry.getKey();
                List<Integer> usersAtDistance = entry.getValue();
                
                if (distance == Integer.MAX_VALUE) {
                    // 无关联节点，放在最外层
                    currentRadius = 400;
                } else {
                    // 根据距离确定半径：距离越大，半径越大
                    currentRadius = 100 + distance * 80;
                }
                
                // 将节点均匀分布在对应半径的圆上
                int userCount = usersAtDistance.size();
                for (int i = 0; i < userCount; i++) {
                    int userId = usersAtDistance.get(i);
                    double angle = 2 * Math.PI * i / userCount;
                    int x = centerX + (int) (currentRadius * Math.cos(angle));
                    int y = centerY + (int) (currentRadius * Math.sin(angle));
                    nodePositions.put(userId, new Point(x, y));
                }
            }
        } else {
            // 没有中心节点，随机分布所有节点
            for (int userId : graph.getAllUsers()) {
                int x = 100 + random.nextInt(width - 200);
                int y = 100 + random.nextInt(height - 200);
                nodePositions.put(userId, new Point(x, y));
            }
        }
    }
    
    public void setCenterUser(int userId) {
        this.centerUserId = userId;
        generateNodePositions();
        repaint();
    }
}