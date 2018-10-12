package com.example.ashish.jobintree.main.rest;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL ="http://13.251.185.41:8080/JobInTree-0.0.1-SNAPSHOT/";

    private Retrofit retrofit;

    private static APIClient retrofitClient;

    public static APIClient getRetrofitClient() {
        if (retrofitClient==null){
            retrofitClient = new APIClient();
        }
        return retrofitClient;
    }

    private APIClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIInterface connectUser(){
        return retrofit.create(APIInterface.class);
    }
}
