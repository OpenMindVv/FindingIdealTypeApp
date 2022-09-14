package com.example.findingidealtypeapp.chatting;

import java.io.Serializable;

public class ChatRoom implements Serializable {
    String chatRoomId;
    String myId;
    String receiverId;
    String receiverName;
    String lastMessage;   //마지막 대화내용
    String date;

    public ChatRoom(String chatRoomId, String myId, String receiverId, String receiverName, String lastMessage, String date){
        this.chatRoomId = chatRoomId;
        this.myId = myId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public String getMyId() {
        return myId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
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

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", myId='" + myId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}

