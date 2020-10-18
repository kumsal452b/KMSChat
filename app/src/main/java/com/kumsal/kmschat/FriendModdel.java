package com.kumsal.kmschat;


public class FriendModdel{
    private String Date;
    private String mDisplayName;
    private String imageUrl;
    private String checkIsOnline;
    public FriendModdel(String date, String mDisplayName, String imageUrl, String checkIsOnline) {
        Date = date;
        this.mDisplayName = mDisplayName;
        this.imageUrl = imageUrl;
        this.checkIsOnline=checkIsOnline;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getmDisplayName() {
        return mDisplayName;
    }

    public void setmDisplayName(String mDisplayName) {
        this.mDisplayName = mDisplayName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCheckIsOnline() {
        return checkIsOnline;
    }

    public void setCheckIsOnline(String checkIsOnline) {
        this.checkIsOnline = checkIsOnline;
    }
}
