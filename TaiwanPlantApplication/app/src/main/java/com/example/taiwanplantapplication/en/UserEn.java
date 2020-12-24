package com.example.taiwanplantapplication.en;

public enum UserEn {
    ID("id"),
    NAME("name"),
    PASS("pass"),
    EMAIL("email");

    private String code;
    UserEn(String en) {
        this.code=en;
    }

    public String getCode(){
        return code;
    }
}
