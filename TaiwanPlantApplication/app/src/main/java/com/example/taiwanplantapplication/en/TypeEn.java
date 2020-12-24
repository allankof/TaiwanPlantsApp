package com.example.taiwanplantapplication.en;

/**
 * FirebaseDatabase JSON 的 Key
 */
public enum TypeEn {
    TYPE("type"),
    GYMN("gymn"),
    ANGI("angi"),
    FERN("fern"),
    OTHER("other"),
    ID("id"),
    NAME("name"),
    FAMILY("family"),
    LOCATION("loc"),
    DATE("date"),
    LNG("lng"),
    LAT("lat"),
    Number("num"),
    DES("des"),
    PHOTO("photoPath");

    private String code;

    TypeEn(String en) {
        this.code=en;
    }

    public String getCode(){
        return code;
    }
}
