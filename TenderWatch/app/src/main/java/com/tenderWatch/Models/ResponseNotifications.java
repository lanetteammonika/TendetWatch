package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lcom47 on 1/1/18.
 */

public class ResponseNotifications {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("tender")
    @Expose
    private Tender tender;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("read")
    @Expose
    private Boolean read;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

}

