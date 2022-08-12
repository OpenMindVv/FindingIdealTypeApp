package com.example.findingidealtypeapp.chatting;

public class ChatInformation {
    String userId;
    String content;
    String date;

    public ChatInformation(String userId, String content, String date){
        this.userId = userId;
        this.content = content;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}
