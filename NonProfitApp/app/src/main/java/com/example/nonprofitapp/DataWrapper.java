
package com.example.nonprofitapp;

import android.graphics.Color;

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
    private int progress; // int from 0 to 3 describing not started, in progress, and completed.
    private boolean isCompleted;
    private String uid;

    /**
     * Uses buttonId to remain compatible with firebase data, but it isn't used.
     * @param displayName
     * @param foodBank
     * @param buttonId
     * @param bag
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param color
     */
    public DataWrapper(String displayName, String foodBank, String buttonId, String bag,
                       int year, int month, int day, int hour, int minute, int color) {
        this.displayName = displayName;
        this.foodBank = foodBank;
        this.bag = bag;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.color = color;
        isCompleted = false;
        progress = 0;
    }

    /**
     * To instantiate a DataWrapper at the beginning of our app, we need to be able to create
     * an empty object.
     */
    public DataWrapper() {
        this("","","","",-1,-1,-1,
                -1,-1, Color.TRANSPARENT);
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

    public int getDay() { return day; }

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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getBankButtonID() {
        return bankButtonID;
    }

    public void setBankButtonID(String bankButtonID) {
        this.bankButtonID = bankButtonID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}