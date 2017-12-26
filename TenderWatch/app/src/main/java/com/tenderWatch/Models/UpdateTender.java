package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lcom48 on 21/12/17.
 */

public class UpdateTender {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("tenderUploader")
    @Expose
    private TenderUploader tenderUploader;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("tenderName")
    @Expose
    private String tenderName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("landlineNo")
    @Expose
    private String landlineNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("subscriber")
    @Expose
    private Object subscriber;
    @SerializedName("amendRead")
    @Expose
    private List<Object> amendRead = null;
    @SerializedName("interested")
    @Expose
    private List<Object> interested = null;
    @SerializedName("readby")
    @Expose
    private List<Object> readby = null;
    @SerializedName("favorite")
    @Expose
    private List<Object> favorite = null;
    @SerializedName("disabled")
    @Expose
    private List<Object> disabled = null;
    @SerializedName("isFollowTender")
    @Expose
    private Boolean isFollowTender;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("expiryDate")
    @Expose
    private String expiryDate;
    @SerializedName("tenderPhoto")
    @Expose
    private String tenderPhoto;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TenderUploader getTenderUploader() {
        return tenderUploader;
    }

    public void setTenderUploader(TenderUploader tenderUploader) {
        this.tenderUploader = tenderUploader;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getLandlineNo() {
        return landlineNo;
    }

    public void setLandlineNo(String landlineNo) {
        this.landlineNo = landlineNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }

    public List<Object> getAmendRead() {
        return amendRead;
    }

    public void setAmendRead(List<Object> amendRead) {
        this.amendRead = amendRead;
    }

    public List<Object> getInterested() {
        return interested;
    }

    public void setInterested(List<Object> interested) {
        this.interested = interested;
    }

    public List<Object> getReadby() {
        return readby;
    }

    public void setReadby(List<Object> readby) {
        this.readby = readby;
    }

    public List<Object> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Object> favorite) {
        this.favorite = favorite;
    }

    public List<Object> getDisabled() {
        return disabled;
    }

    public void setDisabled(List<Object> disabled) {
        this.disabled = disabled;
    }

    public Boolean getIsFollowTender() {
        return isFollowTender;
    }

    public void setIsFollowTender(Boolean isFollowTender) {
        this.isFollowTender = isFollowTender;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getTenderPhoto() {
        return tenderPhoto;
    }

    public void setTenderPhoto(String tenderPhoto) {
        this.tenderPhoto = tenderPhoto;
    }

}
