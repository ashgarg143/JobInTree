package com.example.ashish.jobintree.main.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL ="http://13.251.185.41:8080/JobInTree-0.0.1-SNAPSHOT/";
    //private static final String NEWS_URL = "https://newsapi.org/v2/top-headlines?country=in&apiKey=3d5998d023614120acefd255e7017c2a";

    private static final String NEWS_URL = "https://newsapi.org/v2/";


    private Retrofit retrofit;

    private static RetrofitClient retrofitClient;

    public static synchronized RetrofitClient getRetrofitClient() {
        if (retrofitClient==null){
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public APIInterface connectUser(){
        return retrofit.create(APIInterface.class);
    }
}
