package com.jss.smartdustbin.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jss.smartdustbin.model.Dustbin;
import com.jss.smartdustbin.model.User;
import com.jss.smartdustbin.model.Ward;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Jsonparser {


    public static List<Dustbin> responseStringToDustbinList(String dustbinStringResponse) {
        List<Dustbin> dustbinList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(dustbinStringResponse);
            JSONArray jsonArray = jsonObject.getJSONArray("content");
            for (int j = 0; j < jsonArray.length(); j++) {
                Dustbin dustbin;
                try {
                    dustbin = toDustbinObject(jsonArray.getJSONObject(j).toString());
                    dustbinList.add(dustbin);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dustbinList;


    }

    public static String get(JSONObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            try {
                String string = jsonObject.getString(key);
                return string;
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }


    public static Dustbin toDustbinObject(String jsonString) {
        Dustbin dustbin = new Dustbin();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            dustbin.setId(get(jsonObject, "id"));
            dustbin.setBin(get(jsonObject, "bin"));
            dustbin.setLandmark(get(jsonObject, "landmark"));
            dustbin.setLocality(get(jsonObject, "locality"));

            JSONObject locationJsonObject = jsonObject.getJSONObject("location");
            dustbin.setLatitude(get(locationJsonObject, "x"));
            dustbin.setLongitude(get(locationJsonObject, "y"));

            JSONObject statusJsonObject = jsonObject.getJSONObject("status");
            dustbin.setGarbageLevel(get(statusJsonObject, "percentage"));
            String dateString = get(statusJsonObject, "lastUpdatedAt");
            dustbin.setLastUpdated(Helper.getDateFromString(dateString));
            dustbin.setComment(get(statusJsonObject, "comment"));

            JSONObject installedByJsonObject = jsonObject.getJSONObject(("installedBy"));
            User installedByUser = new User();
            installedByUser.setId(get(installedByJsonObject, "id"));
            installedByUser.setUserName(get(installedByJsonObject, "username"));
            installedByUser.setFirstName(get(installedByJsonObject, "firstName"));
            installedByUser.setLastName(get(installedByJsonObject, "lastName"));

            dustbin.setInstalledByUser(installedByUser);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dustbin;
    }

    public static List<Ward> responseStringToWardList(String wardStringResponse) {
        List<Ward> wardList = new ArrayList<>();
        try { ;
            JSONArray jsonArray = new JSONArray(wardStringResponse);
            for (int j = 0; j < jsonArray.length(); j++) {
                Ward ward;
                try {
                    ward = toWardObject(jsonArray.getJSONObject(j).toString());
                    wardList.add(ward);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wardList;


    }

    private static Ward toWardObject(String jsonString) {
       Ward ward = new Ward();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            ward.setId(get(jsonObject, "id"));
            ward.setName(get(jsonObject, "name"));
            ward.setDescription(get(jsonObject, "description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ward;
    }

    public static User getUserFromResponse(String response) {
        User user = new User();
        try{
            JSONObject jsonObject = new JSONObject(response);
            user.setUserName(get(jsonObject, "username"));
            user.setFirstName(get(jsonObject, "firstName"));
            user.setLastName(get(jsonObject, "lastName"));
        } catch (JSONException e){
            e.printStackTrace();
        }


        return  user;

    }
}
