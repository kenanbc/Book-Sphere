package com.example.booksbazar.model;

public class ChatPreview {
    private String chatId;
    private String otherUserId;
    private String lastMessage;
    private long timestamp;

    public ChatPreview() {

    }

    public ChatPreview(String chatId, String otherUserId, String lastMessage, long timestamp) {
        this.chatId = chatId;
        this.otherUserId = otherUserId;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getChatId() {
        return chatId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

