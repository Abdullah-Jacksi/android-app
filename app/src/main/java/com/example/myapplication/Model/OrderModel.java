package com.example.myapplication.Model;//package io.quicktype;

import java.util.ArrayList;

public class OrderModel {
    private ArrayList<String> locationList;
    private String orderNumber;
    private String myEditText1Text;
    private String myEditText2Text;
    private ArrayList<String> myList;
    private String radioValue;
    private String status;

    public ArrayList<String> getLocationList() { return locationList; }
    public void setLocationList(ArrayList<String> value) { this.locationList = value; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String value) { this.orderNumber = value; }

    public String getMyEditText1Text() { return myEditText1Text; }
    public void setMyEditText1Text(String value) { this.myEditText1Text = value; }

    public String getMyEditText2Text() { return myEditText2Text; }
    public void setMyEditText2Text(String value) { this.myEditText2Text = value; }

    public ArrayList<String> getMyList() { return myList; }
    public void setMyList(ArrayList<String> value) { this.myList = value; }

    public String getRadioValue() { return radioValue; }
    public void setRadioValue(String value) { this.radioValue = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }
}
