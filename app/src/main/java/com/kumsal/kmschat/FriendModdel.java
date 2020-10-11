package com.kumsal.kmschat;


public class FriendModdel{
    private String Date;
    private String mKey;

    public FriendModdel(String date, String mKey) {
        Date = date;
        this.mKey = mKey;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
