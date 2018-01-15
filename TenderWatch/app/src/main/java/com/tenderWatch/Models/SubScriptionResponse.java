package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lcom47 on 26/12/17.
 */

public class SubScriptionResponse {
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
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("contactNo")
    @Expose
    private String contactNo;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("invoiceURL")
    @Expose
    private String invoiceURL;
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("isPayment")
    @Expose
    private Boolean isPayment;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
    @SerializedName("stripeDetails")
    @Expose
    private StripeDetails stripeDetails;
    @SerializedName("deviceId")
    @Expose
    private List<String> deviceId = null;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getInvoiceURL() {
        return invoiceURL;
    }

    public void setInvoiceURL(String invoiceURL) {
        this.invoiceURL = invoiceURL;
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

    public StripeDetails getStripeDetails() {
        return stripeDetails;
    }

    public void setStripeDetails(StripeDetails stripeDetails) {
        this.stripeDetails = stripeDetails;
    }

    public List<String> getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(List<String> deviceId) {
        this.deviceId = deviceId;
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
//-----------------------------------com.example.Sources.java-----------------------------------
//
//        package com.example;
//
//        import java.util.List;
//        import com.google.gson.annotations.Expose;
//        import com.google.gson.annotations.SerializedName;

class Sources {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("url")
    @Expose
    private String url;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
//-----------------------------------com.example.StripeDetails.java-----------------------------------
//
//        package com.example;
//
//        import com.google.gson.annotations.Expose;
//        import com.google.gson.annotations.SerializedName;

class StripeDetails {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("account_balance")
    @Expose
    private Integer accountBalance;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("currency")
    @Expose
    private Object currency;
    @SerializedName("default_source")
    @Expose
    private Object defaultSource;
    @SerializedName("delinquent")
    @Expose
    private Boolean delinquent;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("discount")
    @Expose
    private Object discount;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("livemode")
    @Expose
    private Boolean livemode;
    @SerializedName("shipping")
    @Expose
    private Object shipping;
    @SerializedName("sources")
    @Expose
    private Sources sources;
    @SerializedName("subscriptions")
    @Expose
    private Subscriptions subscriptions;

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

    public Integer getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Integer accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Object getCurrency() {
        return currency;
    }

    public void setCurrency(Object currency) {
        this.currency = currency;
    }

    public Object getDefaultSource() {
        return defaultSource;
    }

    public void setDefaultSource(Object defaultSource) {
        this.defaultSource = defaultSource;
    }

    public Boolean getDelinquent() {
        return delinquent;
    }

    public void setDelinquent(Boolean delinquent) {
        this.delinquent = delinquent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(Boolean livemode) {
        this.livemode = livemode;
    }

    public Object getShipping() {
        return shipping;
    }

    public void setShipping(Object shipping) {
        this.shipping = shipping;
    }

    public Sources getSources() {
        return sources;
    }

    public void setSources(Sources sources) {
        this.sources = sources;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Subscriptions subscriptions) {
        this.subscriptions = subscriptions;
    }

}
//-----------------------------------com.example.Subscriptions.java-----------------------------------

//       package com.example;


class Subscriptions {

    @SerializedName("object")
    @Expose
    private String object;
    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("url")
    @Expose
    private String url;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

