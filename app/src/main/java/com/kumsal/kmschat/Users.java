package com.kumsal.kmschat;

public class Users {
    private String name,status,image;
    private String userID, online;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public  Users(){

    }
    public Users(String name, String status, String image,String userID, String online) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.userID=userID;
        this.online=online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
