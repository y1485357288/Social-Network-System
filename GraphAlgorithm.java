package com.socialnetwork.core;

import java.util.*;

public class GraphAlgorithm {
    private AdjacencyList graph;
    
    public GraphAlgorithm(AdjacencyList graph) {
        this.graph = graph;
    }
    
    public List<Integer> getFirstDegreeFriends(int userId) {
        return graph.getFriends(userId);
    }
    
    public List<Integer> getSecondDegreeFriends(int userId) {
        Set<Integer> firstDegree = new HashSet<>(graph.getFriends(userId));
        Set<Integer> secondDegree = new HashSet<>();
        
        for (int friend : firstDegree) {
            for (int friendOfFriend : graph.getFriends(friend)) {
                if (friendOfFriend != userId && !firstDegree.contains(friendOfFriend)) {
                    secondDegree.add(friendOfFriend);
                }
            }
        }
        
        return new ArrayList<>(secondDegree);
    }
    
    public int calculateSocialDistance(int startId, int endId) {
        if (startId == endId) {
            return 0;
        }
        
        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        
        distance.put(startId, 0);
        queue.offer(startId);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDist = distance.get(current);
            
            for (int neighbor : graph.getFriends(current)) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, currentDist + 1);
                    if (neighbor == endId) {
                        return distance.get(neighbor);
                    }
                    queue.offer(neighbor);
                }
            }
        }
        
        return -1; // 无社交关联
    }
    
    public List<Integer> getShortestPath(int startId, int endId) {
        if (startId == endId) {
            return Collections.singletonList(startId);
        }
        
        Map<Integer, Integer> parent = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        
        parent.put(startId, -1);
        queue.offer(startId);
        
        boolean found = false;
        while (!queue.isEmpty() && !found) {
            int current = queue.poll();
            
            for (int neighbor : graph.getFriends(current)) {
                if (!parent.containsKey(neighbor)) {
                    parent.put(neighbor, current);
                    if (neighbor == endId) {
                        found = true;
                        break;
                    }
                    queue.offer(neighbor);
                }
            }
        }
        
        if (!found) {
            return Collections.emptyList();
        }
        
        List<Integer> path = new ArrayList<>();
        int current = endId;
        while (current != -1) {
            path.add(current);
            current = parent.get(current);
        }
        Collections.reverse(path);
        return path;
    }
    
    public List<Integer> getMultiDegreeFriends(int userId, int degree) {
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        
        visited.add(userId);
        distance.put(userId, 0);
        queue.offer(userId);
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            int currentDist = distance.get(current);
            
            if (currentDist >= degree) {
                continue;
            }
            
            for (int neighbor : graph.getFriends(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    distance.put(neighbor, currentDist + 1);
                    queue.offer(neighbor);
                }
            }
        }
        
        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : distance.entrySet()) {
            if (entry.getValue() == degree) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}