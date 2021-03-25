package com.mkrlabs.chatmes.model;

public class FriendModel {
    private String uid;
    private long timestamp;
    private String key;

    public FriendModel() {
    }

    public FriendModel(String uid, long timestamp, String key) {
        this.uid = uid;
        this.timestamp = timestamp;
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
