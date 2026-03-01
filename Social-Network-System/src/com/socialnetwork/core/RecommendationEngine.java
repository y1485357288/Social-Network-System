package com.socialnetwork.core;

import com.socialnetwork.model.User;

import java.util.*;

public class RecommendationEngine {
    private AdjacencyList graph;
    private HashTable userTable;
    
    public RecommendationEngine(AdjacencyList graph, HashTable userTable) {
        this.graph = graph;
        this.userTable = userTable;
    }
    
    public static class RecommendedUser {
        private int userId;
        private double score;
        private String reason;
        
        public RecommendedUser(int userId, double score, String reason) {
            this.userId = userId;
            this.score = score;
            this.reason = reason;
        }
        
        public int getUserId() {
            return userId;
        }
        
        public double getScore() {
            return score;
        }
        
        public String getReason() {
            return reason;
        }
    }
    
    public List<RecommendedUser> recommendUsers(int targetUserId, int k) {
        List<RecommendedUser> recommendations = new ArrayList<>();
        User targetUser = userTable.get(targetUserId);
        if (targetUser == null) {
            return recommendations;
        }
        
        Set<Integer> existingFriends = new HashSet<>(graph.getFriends(targetUserId));
        existingFriends.add(targetUserId);
        
        for (int userId : graph.getAllUsers()) {
            if (!existingFriends.contains(userId)) {
                User user = userTable.get(userId);
                if (user != null) {
                    double score = calculateRecommendationScore(targetUser, user, targetUserId, userId);
                    if (score > 0) {
                        String reason = generateRecommendationReason(targetUser, user, targetUserId, userId);
                        recommendations.add(new RecommendedUser(userId, score, reason));
                    }
                }
            }
        }
        
        Collections.sort(recommendations, (a, b) -> Double.compare(b.getScore(), a.getScore()));
        return recommendations.subList(0, Math.min(k, recommendations.size()));
    }
    
    private double calculateRecommendationScore(User targetUser, User user, int targetUserId, int userId) {
        double score = 0;
        
        // 基于共同兴趣的分数
        if (targetUser.getInterests() != null && user.getInterests() != null) {
            Set<String> targetInterests = new HashSet<>(targetUser.getInterests());
            Set<String> userInterests = new HashSet<>(user.getInterests());
            
            int commonInterests = 0;
            for (String interest : targetInterests) {
                if (userInterests.contains(interest)) {
                    commonInterests++;
                }
            }
            
            if (!targetInterests.isEmpty()) {
                score += (double) commonInterests / targetInterests.size() * 0.6;
            }
        }
        
        // 基于共同好友的分数
        Set<Integer> targetFriends = new HashSet<>(graph.getFriends(targetUserId));
        Set<Integer> userFriends = new HashSet<>(graph.getFriends(userId));
        
        int commonFriends = 0;
        for (int friend : targetFriends) {
            if (userFriends.contains(friend)) {
                commonFriends++;
            }
        }
        
        if (!targetFriends.isEmpty()) {
            score += (double) commonFriends / targetFriends.size() * 0.4;
        }
        
        return score;
    }
    
    private String generateRecommendationReason(User targetUser, User user, int targetUserId, int userId) {
        StringBuilder reason = new StringBuilder();
        
        // 生成基于共同兴趣的理由
        if (targetUser.getInterests() != null && user.getInterests() != null) {
            Set<String> targetInterests = new HashSet<>(targetUser.getInterests());
            Set<String> userInterests = new HashSet<>(user.getInterests());
            
            List<String> commonInterests = new ArrayList<>();
            for (String interest : targetInterests) {
                if (userInterests.contains(interest)) {
                    commonInterests.add(interest);
                }
            }
            
            if (!commonInterests.isEmpty()) {
                reason.append("共同兴趣：");
                for (int i = 0; i < commonInterests.size(); i++) {
                    reason.append(commonInterests.get(i));
                    if (i < commonInterests.size() - 1) {
                        reason.append("、");
                    }
                }
                reason.append("；");
            }
        }
        
        // 生成基于共同好友的理由
        Set<Integer> targetFriends = new HashSet<>(graph.getFriends(targetUserId));
        Set<Integer> userFriends = new HashSet<>(graph.getFriends(userId));
        
        int commonFriends = 0;
        for (int friend : targetFriends) {
            if (userFriends.contains(friend)) {
                commonFriends++;
            }
        }
        
        if (commonFriends > 0) {
            reason.append("共同好友：").append(commonFriends).append("人");
        }
        
        return reason.toString();
    }
}