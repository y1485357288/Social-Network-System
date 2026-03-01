package com.socialnetwork.gui;

import javax.swing.*;
import java.awt.*;

public class Launcher extends JFrame {
    public Launcher() {
        setTitle("社交网络系统启动器");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("选择界面版本", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        
        JButton newUiButton = new JButton("新版界面 (推荐)");
        newUiButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        newUiButton.addActionListener(e -> {
            new SocialNetworkApp().setVisible(true);
            dispose();
        });
        
        JButton oldUiButton = new JButton("旧版界面");
        oldUiButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        oldUiButton.addActionListener(e -> {
            new MainWindow().setVisible(true);
            dispose();
        });
        
        panel.add(titleLabel);
        panel.add(newUiButton);
        panel.add(oldUiButton);
        
        add(panel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Launcher().setVisible(true);
        });
    }
}