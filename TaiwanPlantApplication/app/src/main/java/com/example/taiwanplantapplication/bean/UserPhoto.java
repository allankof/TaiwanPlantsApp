package com.example.taiwanplantapplication.bean;

import java.util.List;

public class UserPhoto {

    private String photoUser;
    private String photoId;
    private List<String> photoPath;

    public UserPhoto() {
    }

    /**
        *
        * @param photoUser user name
        * @param photoId user id
        * @param photoPath path
        */
    public UserPhoto(String photoUser, String photoId, List<String> photoPath) {
        this.photoUser = photoUser;
        this.photoId = photoId;
        this.photoPath = photoPath;
    }

    public String getPhotoUser() {
        return photoUser;
    }

    public void setPhotoUser(String photoUser) {
        this.photoUser = photoUser;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public List<String> getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(List<String> photoPath) {
        this.photoPath = photoPath;
    }
}
