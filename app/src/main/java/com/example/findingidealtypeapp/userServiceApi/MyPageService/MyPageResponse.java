package com.example.findingidealtypeapp.userServiceApi.MyPageService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyPageResponse {
    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("password")
    @Expose
    public String password;

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("follow")
    @Expose
    public String follow;

    @SerializedName("following")
    @Expose
    public String following;

    public MyPageResponse(String email, String password, String name, String follow, String following){
        this.email = email;
        this.password = password;
        this.name = name;
        this.follow = follow;
        this.following = following;

    }

    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑시켜줌
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getName(){
        return name;
    }
    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }
}
