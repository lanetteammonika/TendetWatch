package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sender {

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
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("androidDeviceId")
    @Expose
    private List<Object> androidDeviceId = null;
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("isPayment")
    @Expose
    private Boolean isPayment;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
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

    public List<Object> getAndroidDeviceId() {
        return androidDeviceId;
    }

    public void setAndroidDeviceId(List<Object> androidDeviceId) {
        this.androidDeviceId = androidDeviceId;
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
