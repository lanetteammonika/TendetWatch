package com.tenderWatch.Retrofit;

/**
 * Created by lcom48 on 25/11/17.
 */

import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.AllContractorTender;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.LoginPost;
import com.tenderWatch.Models.Message;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.ResponseNotifications;
import com.tenderWatch.Models.ResponseRating;
import com.tenderWatch.Models.SubScriptionResponse;
import com.tenderWatch.Models.Success;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.UpdateTender;
import com.tenderWatch.Models.UploadTender;
import com.tenderWatch.Models.User;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.transform.Result;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Belal on 10/2/2017.
 */

public interface Api {
    @POST("auth/glogin")
    @FormUrlEncoded
    Call<Register> savePostGoogle(@Field("token") String idToken,
                                  @Field("role") String role,
                                  @Field("androidDeviceId") String deviceId);

    @POST("auth/facelogin")
    @FormUrlEncoded
    Call<Register> savePostFB(@Field("token") String idToken,
                              @Field("role") String role,
                              @Field("androidDeviceId") String deviceId);

    @POST("auth/login")
    @FormUrlEncoded
    Call<Register> savePost(@Field("email") String email,
                            @Field("password") String password,
                            @Field("role") String role,
                            @Field("androidDeviceId") String deviceId);

    @POST("auth/forgot")
    @FormUrlEncoded
    Call<LoginPost> forgotPassword(@Field("email") String email,
                                   @Field("role") String role);

    @POST("auth/checkEmail")
    @FormUrlEncoded
    Call<Message> checkEmailExit(@Field("email") String email,
                                 @Field("role") String role);

    @GET("auth/country")
    Call<ArrayList<GetCountry>> getCountryData();

    @GET("auth/category")
    Call<ArrayList<GetCategory>> getCategoryData();

    @Multipart
    @POST("auth/register")
    Call<Register> uploadImage(@Part MultipartBody.Part email,
                               @Part MultipartBody.Part password,
                               @Part MultipartBody.Part country,
                               @Part MultipartBody.Part contactNo,
                               @Part MultipartBody.Part occupation,
                               @Part MultipartBody.Part aboutMe,
                               @Part MultipartBody.Part role,
                               @Part MultipartBody.Part androidDeviceId,
                               @Part MultipartBody.Part image);

    @Multipart
    @POST("auth/register")
    Call<Register> uploadContractor(@Part MultipartBody.Part email,
                                    @Part MultipartBody.Part password,
                                    @Part MultipartBody.Part country,
                                    @Part MultipartBody.Part contactNo,
                                    @Part MultipartBody.Part occupation,
                                    @Part MultipartBody.Part aboutMe,
                                    @Part MultipartBody.Part role,
                                    @Part MultipartBody.Part androidDeviceId,
                                    @Part MultipartBody.Part image,
                                    @Part MultipartBody.Part selections,
                                    @Part MultipartBody.Part subscribe);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "users", hasBody = true)
    Call<ResponseBody> logout(@Header("Authorization") String token,
                              @Field("androidDeviceId") String deviceId,
                              @Field("role") String role);

    @Multipart
    @POST("users/{userId}")
    Call<User> UpdateUser(
            @Header("Authorization") String token,
            @Path("userId") String userId,
            @Part MultipartBody.Part country,
            @Part MultipartBody.Part contactNo,
            @Part MultipartBody.Part occupation,
            @Part MultipartBody.Part aboutMe,
            @Part MultipartBody.Part image);

    @GET("users/{userId}")
    Call<User> getUserDetail(
            @Header("Authorization") String token,
            @Path("userId") String userId);

    @POST("users/changePassword/{userId}")
    @FormUrlEncoded
    Call<Success> ChangePassword(
            @Header("Authorization") String token,
            @Path("userId") String userId,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword);

    @POST("tender/getTenders")
    Call<ArrayList<Tender>> getAllTender(
            @Header("Authorization") String token);

    @POST("tender/getTenders")
    Call<ArrayList<AllContractorTender>> getAllContractorTender(
            @Header("Authorization") String token);

    @Multipart
    @POST("tender")
    Call<UploadTender> uploadTender(
            @Header("Authorization") String token,
            @Part MultipartBody.Part email,
            @Part MultipartBody.Part tenderName,
            @Part MultipartBody.Part city,
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part contactNo,
            @Part MultipartBody.Part landlineNo,
            @Part MultipartBody.Part address,
            @Part MultipartBody.Part country,
            @Part MultipartBody.Part category,
            @Part MultipartBody.Part isFollowTender,
            @Part MultipartBody.Part image);

    @Multipart
    @PUT("tender/{tenderDetailId}")
    Call<UpdateTender> updateTender(
            @Header("Authorization") String token,
            @Path("tenderDetailId") String id,
            @Part MultipartBody.Part email,
            @Part MultipartBody.Part tenderName,
            @Part MultipartBody.Part city,
            @Part MultipartBody.Part description,
            @Part MultipartBody.Part contactNo,
            @Part MultipartBody.Part landlineNo,
            @Part MultipartBody.Part address,
            @Part MultipartBody.Part country,
            @Part MultipartBody.Part category,
            @Part MultipartBody.Part isFollowTender,
            @Part MultipartBody.Part image);

    @DELETE("tender/{tenderDetailId}")
    Call<ResponseBody> removeTender(
            @Header("Authorization") String token,
            @Path("tenderDetailId") String id);

    @GET("service/userServices")
    Call<SubScriptionResponse> getSubscriptionDetails(
            @Header("Authorization") String token);

    @GET("notification")
    Call<ArrayList<ResponseNotifications>> getNotifications(
            @Header("Authorization") String token);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "notification/delete", hasBody = true)
    Call<ResponseBody> deleteNotification(@Header("Authorization") String token,
                                          @Field("notification") ArrayList<String> notification);

    @POST("review")
    @FormUrlEncoded
    Call<ResponseRating> giveRating(
            @Header("Authorization") String token,
            @Field("user") String clientId,
            @Field("rating") String Rating);

    @GET("tender/{tenderDetailId}")
    Call<UpdateTender> getTender(
            @Header("Authorization") String token,
            @Path("tenderDetailId") String id);

    @PUT("tender/interested/{tenderId}")
    Call<ResponseBody> callInterested(
            @Header("Authorization") String token,
            @Path("tenderId") String id);

    @PUT("tender/favorite/{tenderId}")
    Call<UpdateTender> addFavorite(
            @Header("Authorization") String token,
            @Path("tenderId") String id);

    @GET("tender/getTenders")
    Call<ArrayList<AllContractorTender>> getAllFavoriteTender(
            @Header("Authorization") String token);

    @PUT("notification/{notificationId}")
    Call<ResponseBody> readNotification(
            @Header("Authorization") String token,
            @Path("notificationId") String id);

    @DELETE("tender/favorite/{favoriteId}")
    Call<ResponseBody> removeFavorite(
            @Header("Authorization") String token,
            @Path("favoriteId") String id);

    @POST("payments/pesapal")
    Call<JSONObject> getPesaPalURL();
}

