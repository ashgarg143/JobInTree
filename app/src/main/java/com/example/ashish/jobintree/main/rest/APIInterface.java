package com.example.ashish.jobintree.main.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {


    //signup 
    @FormUrlEncoded
    @POST("signup")
    Call<ResponseBody> signup(@Field("name") String name,@Field("email") String email,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("phone") String phone);
}
