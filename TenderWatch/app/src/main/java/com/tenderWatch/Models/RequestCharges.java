package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by lcom47 on 24/1/18.
 */

public class RequestCharges {
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("source")
    @Expose
    private String source;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
