package com.tenderWatch.Retrofit;

/**
 * Created by lcom48 on 25/11/17.
 */

import com.tenderWatch.Drawer.MainDrawer;
import com.tenderWatch.Models.GetCategory;
import com.tenderWatch.Models.GetCountry;
import com.tenderWatch.Models.LoginPost;
import com.tenderWatch.Models.Message;
import com.tenderWatch.Models.Register;
import com.tenderWatch.Models.Success;
import com.tenderWatch.Models.Tender;
import com.tenderWatch.Models.User;
import com.tenderWatch.SharedPreference.SharedPreference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.util.ArrayList;
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
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
//let parameters: Parameters = [“email” : self.txfEmail.text!,
//        “password” : self.txfPassword.text!,
//        “role” : appDelegate.isClient! ? “client” : “contractor”,
//        “deviceId”: appDelegate.token!]
//        5:05
////facebook
//        5:05
//        let param: Parameters = [“token”: email!,
//        “role”: appDelegate.isClient! ? “client” : “contractor”]
//        self.Login(F_LOGIN, param)
//var G_LOGIN: String = "auth/glogin"

/**
 * Created by Belal on 10/2/2017.
 */

public interface Api {
    //String BASE_URL = "https://simplifiedcoding.net/demos/";
    //  String BASE_URL = "http://jsonplaceholder.typicode.com/";
//    @GET("marvel")
//    Call<List<Hero>> getHeroes();
    @POST("auth/glogin")
    @FormUrlEncoded
    Call<Register> savePostGoogle(@Field("token") String idToken,
                             @Field("role") String role,
                             @Field("deviceId") String deviceId);
    @POST("auth/facelogin")
    @FormUrlEncoded
    Call<Register> savePostFB(@Field("token") String idToken,
                             @Field("role") String role,
                             @Field("deviceId") String deviceId);
    @POST("auth/login")
    @FormUrlEncoded
    Call<Register> savePost(@Field("email") String email,
                               @Field("password") String password,
                               @Field("role") String role,
                               @Field("deviceId") String deviceId);
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
                               @Part MultipartBody.Part deviceId,
                               @Part MultipartBody.Part image
                               );
    @Multipart
    @POST("auth/register")
    Call<Register> uploadContractor(@Part MultipartBody.Part email,
                               @Part MultipartBody.Part password,
                               @Part MultipartBody.Part country,
                               @Part MultipartBody.Part contactNo,
                               @Part MultipartBody.Part occupation,
                               @Part MultipartBody.Part aboutMe,
                               @Part MultipartBody.Part role,
                               @Part MultipartBody.Part deviceId,
                               @Part MultipartBody.Part image,
                                    @Part MultipartBody.Part selections,
                                     @Part MultipartBody.Part subscribe
    );


    @DELETE("users")
    Call<ResponseBody> logout(
            @Header("Authorization") String token,
            @Header("deviceId") String deviceId,
            @Header("role") String role
    );

    @Multipart
    @POST("users/{userId}")
    Call<User> UpdateUser(
            @Header("Authorization") String token,
            @Path("userId") String userId,
            @Part MultipartBody.Part country,
            @Part MultipartBody.Part contactNo,
            @Part MultipartBody.Part occupation,
            @Part MultipartBody.Part aboutMe,
            @Part MultipartBody.Part image
    );

    @POST("users/changePassword/{userId}")
    @FormUrlEncoded
    Call<Success> ChangePassword(
            @Header("Authorization") String token,
            @Path("userId") String userId,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword
    );
    @POST("tender/getTenders")
    Call<Tender> getAllTender(
            @Header("Authorization") String token
    );
}

