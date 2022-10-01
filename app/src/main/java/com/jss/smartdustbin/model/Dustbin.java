package com.jss.smartdustbin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dustbin implements Parcelable {
    private String id;
    private String bin;
    private String landmark;
    private String state, city, locality;
    private String latitude;
    private String longitude;
    private String garbageLevel;
    private String lastUpdated;
    private String comment;
    private User installedByUser;

    public Dustbin(String garbageLevel, String lastUpdated) {
        this.garbageLevel = garbageLevel;
        this.lastUpdated = lastUpdated;
    }
    public Dustbin() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGarbageLevel() {
        return garbageLevel;
    }

    public void setGarbageLevel(String garbageLevel) {
        this.garbageLevel = garbageLevel;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getInstalledByUser() {
        return installedByUser;
    }

    public void setInstalledByUser(User installedByUser) {
        this.installedByUser = installedByUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(bin);
        dest.writeString(landmark);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(garbageLevel);
        dest.writeParcelable(installedByUser, flags);
        dest.writeString(lastUpdated);
        dest.writeString(comment);

    }

    protected Dustbin(Parcel in) {
        id = in.readString();
        bin = in.readString();
        landmark = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        garbageLevel = in.readString();
        installedByUser = in.readParcelable(getClass().getClassLoader());
        lastUpdated = in.readString();
        comment = in.readString();
    }

    public static final Creator<Dustbin> CREATOR = new Creator<Dustbin>() {
        @Override
        public Dustbin createFromParcel(Parcel in) {
            return new Dustbin(in);
        }

        @Override
        public Dustbin[] newArray(int size) {
            return new Dustbin[size];
        }
    };
}
