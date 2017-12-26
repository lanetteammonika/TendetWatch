package com.tenderWatch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tender implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("tenderUploader")
    @Expose
    private String tenderUploader;
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
    private Long v;
    @SerializedName("subscriber")
    @Expose
    private Object subscriber;
    @SerializedName("amendRead")
    @Expose
    private Object amendRead;
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

    /**
     * No args constructor for use in serialization
     */
    public Tender() {
    }

    /**
     * @param expiryDate
     * @param tenderUploader
     * @param favorite
     * @param landlineNo
     * @param interested
     * @param city
     * @param country
     * @param isActive
     * @param id
     * @param v
     * @param category
     * @param tenderPhoto
     * @param amendRead
     * @param email
     * @param address
     * @param createdAt
     * @param readby
     * @param description
     * @param tenderName
     * @param contactNo
     * @param subscriber
     * @param disabled
     * @param isFollowTender
     */
    public Tender(String id, String tenderUploader, String email, String tenderName, String city, String description, String contactNo, String landlineNo, String address, String country, String category, Long v, Object subscriber, Object amendRead, List<Object> interested, List<Object> readby, List<Object> favorite, List<Object> disabled, Boolean isFollowTender, String createdAt, Boolean isActive, String expiryDate, String tenderPhoto) {
        super();
        this.id = id;
        this.tenderUploader = tenderUploader;
        this.email = email;
        this.tenderName = tenderName;
        this.city = city;
        this.description = description;
        this.contactNo = contactNo;
        this.landlineNo = landlineNo;
        this.address = address;
        this.country = country;
        this.category = category;
        this.v = v;
        this.subscriber = subscriber;
        this.amendRead = amendRead;
        this.interested = interested;
        this.readby = readby;
        this.favorite = favorite;
        this.disabled = disabled;
        this.isFollowTender = isFollowTender;
        this.createdAt = createdAt;
        this.isActive = isActive;
        this.expiryDate = expiryDate;
        this.tenderPhoto = tenderPhoto;
    }

    protected Tender(Parcel in) {
        id = in.readString();
        tenderUploader = in.readString();
        email = in.readString();
        tenderName = in.readString();
        city = in.readString();
        description = in.readString();
        contactNo = in.readString();
        landlineNo = in.readString();
        address = in.readString();
        country = in.readString();
        category = in.readString();
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readLong();
        }
        byte tmpIsFollowTender = in.readByte();
        isFollowTender = tmpIsFollowTender == 0 ? null : tmpIsFollowTender == 1;
        createdAt = in.readString();
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        expiryDate = in.readString();
        tenderPhoto = in.readString();
    }

    public static final Creator<Tender> CREATOR = new Creator<Tender>() {
        @Override
        public Tender createFromParcel(Parcel in) {
            return new Tender(in);
        }

        @Override
        public Tender[] newArray(int size) {
            return new Tender[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenderUploader() {
        return tenderUploader;
    }

    public void setTenderUploader(String tenderUploader) {
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

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Object subscriber) {
        this.subscriber = subscriber;
    }

    public Object getAmendRead() {
        return amendRead;
    }

    public void setAmendRead(Object amendRead) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id,
                this.tenderUploader,
                this.email ,
        this.tenderName ,
        this.city ,
        this.description,
        this.contactNo,
        this.landlineNo ,
        this.address ,
        this.country ,
        this.category ,
                String.valueOf(this.v),
                String.valueOf(this.subscriber),
                String.valueOf(this.amendRead),
                String.valueOf(this.interested),
                String.valueOf(this.readby),
                String.valueOf(this.favorite),
                String.valueOf(this.disabled),
                String.valueOf(this.isFollowTender),
        this.createdAt,
                String.valueOf(this.isActive),
        this.expiryDate ,
        this.tenderPhoto });
    }
}
