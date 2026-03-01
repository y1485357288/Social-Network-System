package com.socialnetwork.model;

import java.util.List;

public class User {
    private int id;
    private String name;
    private List<String> interests;
    
    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public User(int id, String name, List<String> interests) {
        this.id = id;
        this.name = name;
        this.interests = interests;
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getInterests() {
        return interests;
    }
    
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    
    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}