package com.socialnetwork.core;

import java.util.*;

public class AdjacencyList {
    private Map<Integer, List<Integer>> adjList;
    
    public AdjacencyList() {
        adjList = new HashMap<>();
    }
    
    public void addUser(int userId) {
        if (!adjList.containsKey(userId)) {
            adjList.put(userId, new ArrayList<>());
        }
    }
    
    public void removeUser(int userId) {
        adjList.remove(userId);
        for (List<Integer> friends : adjList.values()) {
            friends.remove(Integer.valueOf(userId));
        }
    }
    
    public void addFriendship(int userId1, int userId2) {
        addUser(userId1);
        addUser(userId2);
        
        if (!adjList.get(userId1).contains(userId2)) {
            adjList.get(userId1).add(userId2);
        }
        if (!adjList.get(userId2).contains(userId1)) {
            adjList.get(userId2).add(userId1);
        }
    }
    
    public void removeFriendship(int userId1, int userId2) {
        if (adjList.containsKey(userId1)) {
            adjList.get(userId1).remove(Integer.valueOf(userId2));
        }
        if (adjList.containsKey(userId2)) {
            adjList.get(userId2).remove(Integer.valueOf(userId1));
        }
    }
    
    public List<Integer> getFriends(int userId) {
        return adjList.getOrDefault(userId, new ArrayList<>());
    }
    
    public boolean hasUser(int userId) {
        return adjList.containsKey(userId);
    }
    
    public boolean areFriends(int userId1, int userId2) {
        return adjList.containsKey(userId1) && adjList.get(userId1).contains(userId2);
    }
    
    public Set<Integer> getAllUsers() {
        return adjList.keySet();
    }
    
    public int getUserCount() {
        return adjList.size();
    }
    
    public int getEdgeCount() {
        int count = 0;
        for (List<Integer> friends : adjList.values()) {
            count += friends.size();
        }
        return count / 2; // 无向图，每条边被计算两次
    }
}