package com.example.projectcm;

import java.util.Date;

public class Notif {

    private int id;
    private String title;
    private String body="";
    private Integer userid;
    private String notifdate;
    private Integer carid;



    public Notif(int id, Integer userid, String title, String body, String date , Integer carid){
        this.id= id;
        this.userid=userid;
        this.title = title;
        this.body = body;
        this.notifdate = date;
        this.carid = carid;


    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userid;
    }

    public void settUserId(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return notifdate;
    }

    public void setDate(String date) {
        this.notifdate = date;
    }

    public Integer getCarId() {
        return carid;
    }

    public void setCarId(Integer carId) {
        this.carid = carId;
    }

    @Override
    public String toString() {
        return title+" \t\t\t dia:"+notifdate;
    }
}
