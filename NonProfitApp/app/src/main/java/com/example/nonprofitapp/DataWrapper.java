package com.example.nonprofitapp;

import java.io.Serializable;

public class DataWrapper implements Serializable {
    private String displayName;
    private String foodBank;
    private String bankButtonID;
    private String bag;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private int color;

    public DataWrapper(String displayName, String foodBank, String bankButtonID, String bag, int year, int month, int day, int hour, int minute, int color) {
        this.displayName = displayName;
        this.foodBank = foodBank;
        this.bankButtonID = bankButtonID;
        this.bag = bag;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFoodBank() {
        return foodBank;
    }

    public void setFoodBank(String foodBank) {
        this.foodBank = foodBank;
    }

    public String getBankButtonID() {
        return bankButtonID;
    }

    public void setBankButtonID(String bankButtonID) {
        this.bankButtonID = bankButtonID;
    }

    public String getBag() {
        return bag;
    }

    public void setBag(String bag) {
        this.bag = bag;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}
