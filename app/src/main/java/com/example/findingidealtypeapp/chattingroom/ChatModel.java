package com.example.findingidealtypeapp.chattingroom;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>();         //유저
    public Map<String, Comment> comments = new HashMap<>();      //메세지

    public static class Comment{
        public String uid;
        public String message;
        public String date;

        public String getUid() {
            return uid;
        }

        public String getMessage() {
            return message;
        }

        public String getDate() {
            return date;
        }

        @Override
        public String toString() {
            return "Comment{" +
                    "uid='" + uid + '\'' +
                    ", message='" + message + '\'' +
                    ", date='" + date + '\'' +
                    '}';
        }
    }
}

