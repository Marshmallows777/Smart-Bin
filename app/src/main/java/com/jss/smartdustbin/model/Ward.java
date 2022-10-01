package com.jss.smartdustbin.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Ward {

    private String id;
    private String name;
    private String description;
    private ArrayList<String> supervisorList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getSupervisorList() {
        return supervisorList;
    }

    public void setSupervisorList(ArrayList<String> supervisorList) {
        this.supervisorList = supervisorList;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

