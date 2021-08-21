package com.example.noembly;

public class user_info {

    String userID;
    String username;
    String imgURL;
    String sifre;

    public user_info(String userID, String username, String imgURL, String sifre) {
        this.userID = userID;
        this.username = username;
        this.imgURL = imgURL;
        this.sifre = sifre;
    }

    public user_info(){

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
