package com.mkrlabs.chatmes.model;

public class Request {
    private String name;
    private String uid;
    private  long timestamp;

    public Request(String name, String uid, long timestamp, String requestKey) {
        this.name = name;
        this.uid = uid;
        this.timestamp = timestamp;
        this.requestKey = requestKey;
    }

    private  String requestKey;

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public Request() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
