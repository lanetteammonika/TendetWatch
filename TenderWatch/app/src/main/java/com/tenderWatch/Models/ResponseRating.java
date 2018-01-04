package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lcom47 on 4/1/18.
 */

public class ResponseRating {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("avg")
    @Expose
    private Double avg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
    }

}


