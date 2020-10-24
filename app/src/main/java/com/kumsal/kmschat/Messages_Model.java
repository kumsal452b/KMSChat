package com.kumsal.kmschat;

public class Messages_Model {
    private String message,type;
    private Long time;
    private Boolean seen;
    private String image;
    public Messages_Model(String message, Long time, String type, Boolean seen,String image) {
        this.message = message;
        this.time = time;
        this.type = type;
        this.seen = seen;
        this.image=image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
