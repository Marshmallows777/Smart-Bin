package com.jss.smartdustbin.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationProvider {

    public static final String TAG = "LocationProvider";
    private Context context;
    private LocationRequest locationRequest;
    private SharedPreferences pref;
    private FusedLocationProviderClient fusedLocationClient;

    public LocationProvider(Context context, LocationRequest request) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        locationRequest = request;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public boolean getLocation() {

        Log.e(TAG, "getLocation: ");

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                pref.edit().putString("latitude", String.valueOf(latitude)).apply();
                pref.edit().putString("longitude", String.valueOf(longitude)).apply();
                Log.e(TAG, "lat: " + latitude + "; long: " + longitude);

//                if (isConnected)
//                    sendLocation(latitude, longitude);
            }
        },null);

        return false;
    }
}
