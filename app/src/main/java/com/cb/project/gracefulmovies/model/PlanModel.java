package com.cb.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanModel implements Serializable{
    private String startTime;
    private String sumTime;
    private int price;
    private String hall_Name;
    @SerializedName("plan_id")
    private String hall_id;

    public String getHall_id() {
        return hall_id;
    }

    public void setHall_id(String hall_id) {
        this.hall_id = hall_id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getHall_Name() {
        return hall_Name;
    }

    public void setHall_Name(String hall_Name) {
        this.hall_Name = hall_Name;
    }
}
