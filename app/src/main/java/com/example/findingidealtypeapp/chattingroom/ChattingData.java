package com.example.findingidealtypeapp.chattingroom;

public class ChattingData {

    private String userId;
    private String content;
    private String time;
    private int viewType;

    public ChattingData(String userId, String content, String time, int viewType){
        this.userId = userId;
        this.content = content;
        this.time = time;
        this.viewType = viewType;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public int getViewType() {
        return viewType;
    }
}
