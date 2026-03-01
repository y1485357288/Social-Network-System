package com.socialnetwork.core;

import com.socialnetwork.model.User;

import java.util.LinkedList;

public class HashTable {
    private static final int DEFAULT_CAPACITY = 100;
    private LinkedList<Entry>[] table;
    private int size;
    
    private static class Entry {
        int key;
        User value;
        
        Entry(int key, User value) {
            this.key = key;
            this.value = value;
        }
    }
    
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    public HashTable(int capacity) {
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
    }
    
    private int hash(int key) {
        return Math.abs(key) % table.length;
    }
    
    public void put(int key, User value) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];
        
        for (Entry entry : bucket) {
            if (entry.key == key) {
                entry.value = value;
                return;
            }
        }
        
        bucket.add(new Entry(key, value));
        size++;
    }
    
    public User get(int key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];
        
        for (Entry entry : bucket) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    public void remove(int key) {
        int index = hash(key);
        LinkedList<Entry> bucket = table[index];
        
        for (Entry entry : bucket) {
            if (entry.key == key) {
                bucket.remove(entry);
                size--;
                return;
            }
        }
    }
    
    public boolean containsKey(int key) {
        return get(key) != null;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * 获取所有键的集合（用于遍历和保存）
     */
    public java.util.Set<Integer> keySet() {
        java.util.Set<Integer> keys = new java.util.HashSet<>();
        for (LinkedList<Entry> bucket : table) {
            for (Entry entry : bucket) {
                keys.add(entry.key);
            }
        }
        return keys;
    }
    
    /**
     * 获取所有用户
     */
    public java.util.List<User> getAllUsers() {
        java.util.List<User> users = new java.util.ArrayList<>();
        for (LinkedList<Entry> bucket : table) {
            for (Entry entry : bucket) {
                users.add(entry.value);
            }
        }
        return users;
    }
}