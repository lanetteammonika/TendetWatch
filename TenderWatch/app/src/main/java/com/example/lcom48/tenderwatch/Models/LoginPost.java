package com.example.lcom48.tenderwatch.Models;

/**
 * Created by lcom48 on 25/11/17.
 */


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class LoginPost {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
