package com.jss.smartdustbin.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jss.smartdustbin.API;
import com.jss.smartdustbin.activity.LoginActivity;
import com.jss.smartdustbin.activity.UserHomeActivity;
import com.jss.smartdustbin.interfaces.VolleyCallback;
import com.jss.smartdustbin.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Helper {

    private static final String TAG = "Helper";

    public static void sendFCMToken(String FCMToken, String accessToken, Context context) {

        //final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");
        StringRequest fcmTokenPostReq = new StringRequest(Request.Method.POST,API.BASE + API.FCM_TOKEN_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, " onResponse: " + response);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fcm_token_send", true);
                editor.apply();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " onErrorResponse: " + error.toString());
                //Toast.makeText(UserHomeActivity.this, "Something went wrong! Please login again", Toast.LENGTH_SHORT).show();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("fcm_token_send", false);
                editor.apply();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("token", FCMToken);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);

                return params;
            }
        };

        SmartDustbinApplication.getInstance().addToRequestQueue(fcmTokenPostReq);
    }


    public static String getDateFromString(String dateString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        long time = sdf.parse(dateString).getTime();
        long now = System.currentTimeMillis();

        CharSequence ago =
                DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return ago.toString();
    }

    public static int getGarbageStatusFromLevel(String garbageLevel){
        int garbagePer = Integer.parseInt(garbageLevel);
        if(garbagePer < 50)
            return 1;
        else if(garbagePer <= 74)
           return 2;
        else return 3;
    }

    public static boolean isFcmUpdatedOnserver(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("fcm_token_send", false);
    }


}


