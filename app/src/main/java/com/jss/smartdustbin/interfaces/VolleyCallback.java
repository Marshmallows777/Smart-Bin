package com.jss.smartdustbin.interfaces;

public interface VolleyCallback {

    void onSuccess(String response);

    void onError(int status, String error);
}
