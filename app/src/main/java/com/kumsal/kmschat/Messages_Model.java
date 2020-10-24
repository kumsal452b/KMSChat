package com.kumsal.kmschat;

public class Messages_Model {
    private String message,time,type,from, seen;

    public Messages_Model(String message, String time, String type, String from, String seen) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.from = from;
        this.seen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }
}
