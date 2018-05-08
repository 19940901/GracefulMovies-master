package com.cb.project.gracefulmovies.model;

import java.io.Serializable;

/**
 * @ClassName Uorder
 * @Author redPeanuts
 * @Data 2018/4/18 11:38
 * @Version 1.0
 * @describtion 数据库uorder表
 **/

public class Uorder implements Serializable{
    private String state;
    private String verifyCode;
    private int price;
    private int plan_id;
    private String seatNumber;
    private int user_id;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(int plan_id) {
        this.plan_id = plan_id;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
