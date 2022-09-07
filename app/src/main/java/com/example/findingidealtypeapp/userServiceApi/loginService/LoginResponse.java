package com.example.findingidealtypeapp.userServiceApi.loginService;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("result")
    public String resultCode;

    @SerializedName("test")
    public String testCode;

    public String getResultCode() {

        return resultCode;
    }

    public void setResultCode(String resultCode) {

        this.resultCode = resultCode;
    }

    public String getTestCode()
    {
        return testCode;
    }

    public void setTestCode(String test) {
        this.testCode = test;
    }
}
