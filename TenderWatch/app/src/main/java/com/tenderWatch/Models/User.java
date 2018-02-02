package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lcom48 on 8/12/17.
 */

public class User {
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
    @SerializedName("isPayment")
    @Expose
    private Boolean isPayment;
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("androidDeviceId")
    @Expose
    private List<String> androidDeviceId = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("profilePhoto")
    @Expose
    private String profilePhoto;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("avg")
    @Expose
    private Double avg;
    @SerializedName("review")
    @Expose
    private Review review;
    @SerializedName("invoiceURL")
    @Expose
    private String invoiceURL;


    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public Boolean getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(Boolean isPayment) {
        this.isPayment = isPayment;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public List<String> getAndroidDeviceId() {
        return androidDeviceId;
    }

    public void setAndroidDeviceId(List<String> androidDeviceId) {
        this.androidDeviceId = androidDeviceId;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getInvoiceURL() {
        return invoiceURL;
    }

    public void setInvoiceURL(String invoiceURL) {
        this.invoiceURL = invoiceURL;
    }

}

