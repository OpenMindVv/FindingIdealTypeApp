package com.example.findingidealtypeapp.chatting;

public class ChatRoom {
    String chatRoomId;
    String receiverId;
    String lastMessage;   //마지막 대화내용
    String date;

    public ChatRoom(String chatRoomId, String receiverId, String lastMessage, String date){
        this.chatRoomId = chatRoomId;
        this.receiverId = receiverId;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getDate() {
        return date;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

