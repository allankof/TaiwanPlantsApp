package com.example.taiwanplantapplication.bean;

import android.content.Intent;
import android.graphics.Bitmap;

import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.List;

public class InfoData {
    String id;
    String name;
    String family;
    String location;
    String date;
    String lng;
    String lat;
    String number;
    //String imgPath;
    String des;
    String[] args;
    GenericTypeIndicator<List<String>> t;

    public InfoData(){
        // Default constructor required for calls to DataSnapshot.getValue(InfoData.class)
    }

    public InfoData(String id, String name, String family, String location, String date, String lng, String lat, String number, String des) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.location = location;
        this.date = date;
        this.lng = lng;
        this.lat = lat;
        this.number = number;
        this.des = des;
    }

    public InfoData(String id, String name, String family, String location, String date, String lng, String lat, String number, String des, String[] args) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.location = location;
        this.date = date;
        this.lng = lng;
        this.lat = lat;
        this.number = number;
        this.des = des;
        this.args = args;
    }


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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

}
