package com.example.nonprofitapp;

import java.io.Serializable;

public class DataWrapper implements Serializable {
    private String foodBank;
    private int bankButtonID;
    private String bag;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    //setters
    public void setFoodBank(String bank){
        foodBank = bank;
    }
    public void setBag(String pack){
        bag = pack;
    }
    public void setYear(int twoThousandWhaaat){
        year = twoThousandWhaaat;
    }
    public void setMonth(int newMoons){
        month = newMoons;
    }
    public void setDay(int moonPhase){
        day = moonPhase;
    }
    public void setHour(int militaryTime){
        hour = militaryTime;
    }
    public void setMinute(int zeroToSixtyRealQuick){
        minute = zeroToSixtyRealQuick;
    }


    //getters
    public String getFoodBank(){
        return foodBank;
    }
    public String getBag(){
        return bag;
    }
    public int getYear(){
        return year;
    }
    public int getMonth(){
        return month;
    }
    public int getDay(){
        return day;
    }
    public int getHour(){
        return hour;
    }
    public int getMinute(){
        return minute;
    }

}
