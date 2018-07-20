package com.tenderWatch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by lcom48 on 8/12/17.
 */

public class User {
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("deviceId")
    @Expose
    private List<String> deviceId = null;
    @SerializedName("pesapalDetails")
    @Expose
    private List<PesapalDetail> pesapalDetails = null;
    @SerializedName("subscribe")
    @Expose
    private String subscribe;
    @SerializedName("isPayment")
    @Expose
    private Boolean isPayment;
    @SerializedName("payment")
    @Expose
    private Long payment;
    @SerializedName("androidDeviceId")
    @Expose
    private List<String> androidDeviceId = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
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
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("stripeDetails")
    @Expose
    private com.tenderWatch.Models.StripeDetails stripeDetails;
    @SerializedName("invoiceURL")
    @Expose
    private String invoiceURL;
    @SerializedName("profilePhoto")
    @Expose
    private String profilePhoto;
    @SerializedName("avg")
    @Expose
    private Double avg;
    @SerializedName("review")
    @Expose
    private Review review;

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<String> getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(List<String> deviceId) {
        this.deviceId = deviceId;
    }

    public List<PesapalDetail> getPesapalDetails() {
        return pesapalDetails;
    }

    public void setPesapalDetails(List<PesapalDetail> pesapalDetails) {
        this.pesapalDetails = pesapalDetails;
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

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public com.tenderWatch.Models.StripeDetails getStripeDetails() {
        return stripeDetails;
    }

    public void setStripeDetails(com.tenderWatch.Models.StripeDetails stripeDetails) {
        this.stripeDetails = stripeDetails;
    }

    public String getInvoiceURL() {
        return invoiceURL;
    }

    public void setInvoiceURL(String invoiceURL) {
        this.invoiceURL = invoiceURL;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setPayment(Boolean payment) {
        isPayment = payment;
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

    class Review {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("rating")
        @Expose
        private Integer rating;

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

    }

    public class PesapalDetail {

        /*@SerializedName("selections")
        @Expose
        private JSONObject selections;*/
        @SerializedName("subscriptionPackage")
        @Expose
        private String subscriptionPackage;
        @SerializedName("register")
        @Expose
        private String register;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("payment")
        @Expose
        private String payment;
        @SerializedName("pesapalStatus")
        @Expose
        private String pesapalStatus;

       /* public JSONObject getSelections() {
            return selections;
        }

        public void setSelections(JSONObject selections) {
            this.selections = selections;
        }*/

        public String getSubscriptionPackage() {
            return subscriptionPackage;
        }

        public void setSubscriptionPackage(String subscriptionPackage) {
            this.subscriptionPackage = subscriptionPackage;
        }

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getPesapalStatus() {
            return pesapalStatus;
        }

        public void setPesapalStatus(String pesapalStatus) {
            this.pesapalStatus = pesapalStatus;
        }

    }

    public class Selections {

        @SerializedName("59e736e29533c015987953d3")
        @Expose
        private List<String> _59e736e29533c015987953d3 = null;
        @SerializedName("59e735479533c0159879539a")
        @Expose
        private List<String> _59e735479533c0159879539a = null;
        @SerializedName("59e735009533c01598795396")
        @Expose
        private List<String> _59e735009533c01598795396 = null;

        public List<String> get59e736e29533c015987953d3() {
            return _59e736e29533c015987953d3;
        }

        public void set59e736e29533c015987953d3(List<String> _59e736e29533c015987953d3) {
            this._59e736e29533c015987953d3 = _59e736e29533c015987953d3;
        }

        public List<String> get59e735479533c0159879539a() {
            return _59e735479533c0159879539a;
        }

        public void set59e735479533c0159879539a(List<String> _59e735479533c0159879539a) {
            this._59e735479533c0159879539a = _59e735479533c0159879539a;
        }

        public List<String> get59e735009533c01598795396() {
            return _59e735009533c01598795396;
        }

        public void set59e735009533c01598795396(List<String> _59e735009533c01598795396) {
            this._59e735009533c01598795396 = _59e735009533c01598795396;
        }

    }

    public class Sources {

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
        private Long totalCount;
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

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    public class StripeDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("object")
        @Expose
        private String object;
        @SerializedName("account_balance")
        @Expose
        private Long accountBalance;
        @SerializedName("created")
        @Expose
        private Long created;
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
        @SerializedName("invoice_prefix")
        @Expose
        private String invoicePrefix;
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

        public Long getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(Long accountBalance) {
            this.accountBalance = accountBalance;
        }

        public Long getCreated() {
            return created;
        }

        public void setCreated(Long created) {
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

        public String getInvoicePrefix() {
            return invoicePrefix;
        }

        public void setInvoicePrefix(String invoicePrefix) {
            this.invoicePrefix = invoicePrefix;
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

    public class Subscriptions {

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
        private Long totalCount;
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

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

