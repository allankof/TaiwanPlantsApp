package com.example.taiwanplantapplication.bean;

public class Register {
    String id;
    String user;
    String pass;
    String email;

    public Register() {
    }

    public Register(String id, String user, String pass, String email) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
