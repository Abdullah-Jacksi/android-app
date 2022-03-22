package com.example.myapplication.Model;

public class Users {
    private String user, em, pass, ph;

    public Users()
    {

    }

    public Users(String user, String em, String pass, String ph) {
        this.user = user;
        this.em = em;
        this.pass = pass;
        this.ph = ph;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEm() {
        return em;
    }

    public void setEm(String em) {
        this.em = em;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }
}