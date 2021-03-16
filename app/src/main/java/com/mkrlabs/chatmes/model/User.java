package com.mkrlabs.chatmes.model;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private String uid;
    private long lastSeenTime;
    private boolean status;

    public User(String firstName, String lastName, String email, String imageUrl, String uid, boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.status = status;
    }

    public User(String firstName, String lastName, String email, String imageUrl, String uid, long lastSeenTime, boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.lastSeenTime = lastSeenTime;
        this.status = status;
    }

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public  String getUserFullName(){
        return  firstName+ " "+ lastName;
    }

    public long getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(long lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }
}
