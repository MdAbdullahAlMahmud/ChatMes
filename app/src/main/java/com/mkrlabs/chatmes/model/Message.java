package com.mkrlabs.chatmes.model;

public class Message {
    private String messageId,message,senderId,type;
    private long timestamp;

    public Message() {
    }

    public Message(String messageId, String message, String senderId, String type, long timestamp) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
