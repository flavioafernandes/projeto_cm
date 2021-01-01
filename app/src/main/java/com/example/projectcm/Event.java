package com.example.projectcm;

public class Event {
    int id;
    int carid;
    int userid;
    String name;
    String description;
    String date;

    public Event(String name,String description,String date){
        this.name = name;
        this.description = description;
        this.date = date;
    }


    public Event(int id, int carid, int userid, String name, String description, String date) {
        this.id = id;
        this.carid = carid;
        this.userid = userid;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarid() {
        return carid;
    }

    public void setCarid(int carid) {
        this.carid = carid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
