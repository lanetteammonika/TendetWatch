package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("account_holder_name")
    @Expose
    private String accountHolderName;
    @SerializedName("account_holder_type")
    @Expose
    private String accountHolderType;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;
    @SerializedName("last4")
    @Expose
    private String last4;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("routing_number")
    @Expose
    private String routingNumber;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountHolderType() {
        return accountHolderType;
    }

    public void setAccountHolderType(String accountHolderType) {
        this.accountHolderType = accountHolderType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
