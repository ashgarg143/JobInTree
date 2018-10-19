package com.example.ashish.jobintree.main;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager sharedPrefManager;
    private static Context context;

    private static final String SHARED_PREF_NAME = "MySharedPref";

    private SharedPrefManager(Context context) {
        SharedPrefManager.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }

    public void LoginUser(String name,String email,String phone){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("phone",phone);

        editor.apply();
    }

    public String getName(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString("name",null);
    }

    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}