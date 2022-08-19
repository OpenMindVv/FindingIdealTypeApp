package com.example.findingidealtypeapp.userServiceApi.MyPageService;

import com.google.gson.annotations.SerializedName;

public class MyPageResponse {
    @SerializedName("result")
    private String result;

    @SerializedName("name")
    private String name;

    @SerializedName("follow")
    private String follow;

    @SerializedName("following")
    private String following;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
