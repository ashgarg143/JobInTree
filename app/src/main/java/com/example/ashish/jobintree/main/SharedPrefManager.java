package com.example.ashish.jobintree.main;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager sharedPrefManager;
    private static Context context;

    private static final String SHARED_PREF_STRING_NAME = "name";
    private static final String SHARED_PREF_STRING_EMAIL = "email";
    private static final String SHARED_PREF_STRING_PHONE = "phone";

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

        editor.putString(SHARED_PREF_STRING_NAME,name);
        editor.putString(SHARED_PREF_STRING_EMAIL,email);
        editor.putString(SHARED_PREF_STRING_PHONE,phone);

        editor.apply();
    }

    public String getName(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_STRING_NAME,null);
    }

    public String getEmail(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_STRING_EMAIL,null);
    }

    public String getPhone(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREF_STRING_EMAIL,null);
    }

    public void logout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}