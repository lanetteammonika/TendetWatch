package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lcom47 on 24/1/18.
 */

public class RequestPayment {
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
    @SerializedName("selections")
    @Expose
    private HashMap<String, ArrayList<String>> selections;

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public HashMap<String, ArrayList<String>> getSelections() {
        return selections;
    }

    public void setSelections(HashMap<String, ArrayList<String>> selections) {
        this.selections = selections;
    }

}
