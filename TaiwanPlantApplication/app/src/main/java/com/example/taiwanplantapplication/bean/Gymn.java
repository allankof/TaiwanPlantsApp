package com.example.taiwanplantapplication.bean;

public class Gymn {

    String id;
    String name;
    String family;
    String location;
    String date;
    String tude;
    String number;
    //String imgPath;
    String des;

    public Gymn() {
    }

    public Gymn(String id, String name, String family, String location, String date, String tude, String number, String des) {
        this.id = id;
        this.name = name;
        this.family = family;
        this.location = location;
        this.date = date;
        this.tude = tude;
        this.number = number;
        this.des = des;
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

    public String getTude() {
        return tude;
    }

    public void setTude(String tude) {
        this.tude = tude;
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
}
