package com.example.lcom48.tenderwatch.Retrofit;

/**
 * Created by lcom48 on 25/11/17.
 */

import com.example.lcom48.tenderwatch.Models.LoginPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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
    @POST("/auth/glogin")
    @FormUrlEncoded
    Call<LoginPost> savePost(@Field("token") String idToken,
                             @Field("role") String role,
                             @Field("deviceId") String deviceId);
}

