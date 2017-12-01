package com.tenderWatch.Models;

/**
 * Created by lcom48 on 1/12/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCountry {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("countryName")
    @Expose
    private String countryName;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("isoCode")
    @Expose
    private String isoCode;
    @SerializedName("isoCurrencyCode")
    @Expose
    private String isoCurrencyCode;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("imageString")
    @Expose
    private String imageString;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public String getIsoCurrencyCode() {
        return isoCurrencyCode;
    }

    public void setIsoCurrencyCode(String isoCurrencyCode) {
        this.isoCurrencyCode = isoCurrencyCode;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
