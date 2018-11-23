package com.example.ashish.jobintree.main.rest;

import com.example.ashish.jobintree.main.helpers.Skills;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIInterface {



    @GET
    Call<ResponseBody> vacancyDetails(@Url String url);


    @POST("vacancy/recommended")
    Call<ResponseBody> recommendedVacancies(@Query("string") String skills);

    @POST
    Call<ResponseBody> getVacancy(@Url String url, @Body Skills object);

    // vacancy on category and location
    @GET
    Call<ResponseBody> vacancy(@Url String url);


    //All vacancies list
    @GET("vacancy")
    Call<ResponseBody> vacancyList();


    //signup api path
    @FormUrlEncoded
    @POST("signup/")
    Call<ResponseBody> signup(@Field("name") String name,@Field("email") String email,@Field("phone") String phone);

    
    //login api path
    @FormUrlEncoded
    @POST("login/")
    Call<ResponseBody> login(@Field("phone") String phone);
}
