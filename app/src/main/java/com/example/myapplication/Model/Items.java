package com.example.myapplication.Model;

public class Items {
    private String  paper, plastic,glass,aluminum ,date, time;

    public Items()
    {

    }

    public Items ( String paper,String plastic,String glass,String aluminum, String date, String time) {
        this.paper = paper;
        this.plastic = plastic;
        this.glass = glass;
        this.aluminum = aluminum;
        this.date = date;
        this.time = time;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public String getPlastic() {
        return plastic;
    }

    public void setPlastic(String plastic) {
        this.plastic = plastic;
    }

    public String getGlass() {
        return glass;
    }

    public void setGlass(String glass) {
        this.glass = glass;
    }

    public String getAluminum() {
        return aluminum;
    }

    public void setAluminum(String aluminum) {
        this.aluminum = aluminum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}