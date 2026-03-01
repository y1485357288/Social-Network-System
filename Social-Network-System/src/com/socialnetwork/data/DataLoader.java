package com.socialnetwork.data;

import com.socialnetwork.core.AdjacencyList;
import com.socialnetwork.core.HashTable;
import com.socialnetwork.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static void loadUsersFromFile(String filePath, HashTable userTable) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    throw new IOException("Invalid user data format: " + line);
                }
                
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    
                    List<String> interests = new ArrayList<>();
                    if (parts.length > 2) {
                        for (int i = 2; i < parts.length; i++) {
                            String interest = parts[i].trim();
                            if (!interest.isEmpty()) {
                                interests.add(interest);
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
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split(",");
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
}