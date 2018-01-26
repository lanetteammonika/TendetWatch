package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseBankList {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("url")
    @Expose
    private String url;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
