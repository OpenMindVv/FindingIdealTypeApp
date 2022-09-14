package com.example.findingidealtypeapp.chattingroom;

public class ChattingData {

    private String profileImage;
    private String userId;
    private String content;
    private String time;
    private int viewType;

    public ChattingData(String profileImage, String userId, String content, String time, int viewType){
        this.profileImage = profileImage;
        this.userId = userId;
        this.content = content;
        this.time = time;
        this.viewType = viewType;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
