package com.jss.smartdustbin.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SmartDustbinApplication extends Application {

    public static final String TAG = SmartDustbinApplication.class.getSimpleName();
    private static SmartDustbinApplication instance;
    private RequestQueue mRequestQueue;

    public SharedPreferences getDefaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized SmartDustbinApplication getInstance() {
        return instance;
    }

    /*public String getAccessToken() {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences();
        String token = null;
        if (sharedPreferences.contains("access_token")) {
            token = sharedPreferences.getString("access_token", "");
        }
        return token;
    }*/
    public void setFCMRegTokenInPref(String token) {
        SharedPreferences pref = getDefaultSharedPreferences();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
        Toast.makeText(getApplicationContext(), "RegToken " + token, Toast.LENGTH_SHORT).show();

    }



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelRequest(Object tag) {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }




}
