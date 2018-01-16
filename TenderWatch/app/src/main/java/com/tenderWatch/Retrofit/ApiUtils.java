package com.tenderWatch.Retrofit;

/**
 * Created by lcom48 on 25/11/17.
 */

public class ApiUtils {

    private ApiUtils() {}
//var BASE_URL: String = "http://52.66.136.45:4000/api/"http://192.168.201.28:4000/api/
//var BASE_URL: String = "http://lanetteam.com:4000/api/"
//    lcom47@lcom47:~$ sudo service mongodb start
//    lcom47@lcom47:~$ sudo mongod --dbpath=/var/lib/mongodb

//var BASE_URL: String = "http://192.168.200.46:4000/api/"

    public static final String BASE_URL = "http://192.168.201.28:4000/api/";

    public static Api getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(Api.class);
    }
}

