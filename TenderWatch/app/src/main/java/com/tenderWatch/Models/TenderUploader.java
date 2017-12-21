package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lcom48 on 21/12/17.
 */

public class TenderUploader {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("profilePhoto")
    @Expose
    private String profilePhoto;
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("isPayment")
    @Expose
    private Boolean isPayment;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
    @SerializedName("deviceId")
    @Expose
    private List<String> deviceId = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public Boolean getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(Boolean isPayment) {
        this.isPayment = isPayment;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public List<String> getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(List<String> deviceId) {
        this.deviceId = deviceId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}


