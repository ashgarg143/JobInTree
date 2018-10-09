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
}