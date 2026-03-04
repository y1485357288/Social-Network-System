package com.socialnetwork.data;

import com.socialnetwork.core.AdjacencyList;
import com.socialnetwork.core.HashTable;
import com.socialnetwork.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataLoader {
    public static void loadUsersFromFile(String filePath, HashTable userTable) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // 跳过标题行（支持中英文逗号）
                if (isFirstLine && (line.contains("用户 ID") || line.contains("姓名") || line.contains("兴趣标签") || line.contains("用户 ID，姓名"))) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                // 支持中英文逗号分隔
                String[] parts = line.split("[,，]");
                if (parts.length < 3) {
                    throw new IOException("Invalid user data format: " + line);
                }
                
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    
                    // 解析兴趣标签（使用分号分隔）
                    List<String> interests = new ArrayList<>();
                    String interestsStr = parts[2].trim();
                    if (!interestsStr.isEmpty()) {
                        String[] interestArray = interestsStr.split(";");
                        for (String interest : interestArray) {
                            String trimmedInterest = interest.trim();
                            if (!trimmedInterest.isEmpty()) {
                                interests.add(trimmedInterest);
                            }
                        }
                    }
                    
                    User user = new User(id, name, interests);
                    userTable.put(id, user);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid user ID: " + parts[0], e);
                }
            }
        }
    }
    
    public static void loadFriendshipsFromFile(String filePath, AdjacencyList graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // 跳过标题行（支持中英文逗号）
                if (isFirstLine && (line.contains("用户 ID1") || line.contains("用户 ID2") || line.contains("用户 ID1，用户 ID2"))) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;
                
                // 支持中英文逗号分隔
                String[] parts = line.split("[,，]");
                if (parts.length < 2) {
                    throw new IOException("Invalid friendship data format: " + line);
                }
                
                try {
                    int userId1 = Integer.parseInt(parts[0].trim());
                    int userId2 = Integer.parseInt(parts[1].trim());
                    graph.addFriendship(userId1, userId2);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid user ID in friendship data: " + line, e);
                }
            }
        }
    }
    
    public static void loadFromFiles(String usersFile, String friendshipsFile, HashTable userTable, AdjacencyList graph) throws IOException {
        loadUsersFromFile(usersFile, userTable);
        loadFriendshipsFromFile(friendshipsFile, graph);
    }
    
    /**
     * 保存用户数据到文件
     */
    public static void saveUsersToFile(String filePath, HashTable userTable) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filePath));
        writer.println("用户 ID，姓名，兴趣标签");
        
        // 遍历哈希表保存所有用户
        for (User user : userTable.getAllUsers()) {
            writer.print(user.getId() + ",");
            writer.print(user.getName() + ",");
            if (user.getInterests() != null && !user.getInterests().isEmpty()) {
                for (int i = 0; i < user.getInterests().size(); i++) {
                    if (i > 0) writer.print(";");
                    writer.print(user.getInterests().get(i));
                }
            }
            writer.println();
        }
        
        writer.close();
        System.out.println("用户数据已保存到：" + filePath + "，共 " + userTable.size() + " 个用户");
    }
    
    /**
     * 保存好友关系到文件
     */
    public static void saveFriendshipsToFile(String filePath, AdjacencyList graph) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filePath));
        writer.println("用户 ID1，用户 ID2");
        
        Set<Integer> allUsers = graph.getAllUsers();
        int edgeCount = 0;
        for (int userId : allUsers) {
            List<Integer> friends = graph.getFriends(userId);
            for (int friendId : friends) {
                if (userId < friendId) { // 避免重复保存
                    writer.println(userId + "," + friendId);
                    edgeCount++;
                }
            }
        }
        
        writer.close();
        System.out.println("好友关系已保存到：" + filePath + "，共 " + edgeCount + " 条关系");
    }
    
    /**
     * 保存当前状态到文件
     */
    public static void saveData(String usersFile, String friendshipsFile, HashTable userTable, AdjacencyList graph) throws IOException {
        saveUsersToFile(usersFile, userTable);
        saveFriendshipsToFile(friendshipsFile, graph);
    }
}