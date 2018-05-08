package com.cb.project.gracefulmovies.model;

/**
 * @ClassName Order
 * @Author redPeanuts
 * @Data 2018/4/18 9:24
 * @Version 1.0
 * @describtion 用户购物车界面
 **/

public class Order {
    private Integer order_id;
    private String movie_Name;
    private Integer price;

    private String seatNumber;

    private String startTime;

    private String sumtime;

    private String hall_name;

    private String state;
    private String verifyCode;

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getMovie_Name() {
        return movie_Name;
    }

    public void setMovie_Name(String movieName) {
        this.movie_Name = movieName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSumtime() {
        return sumtime;
    }

    public void setSumtime(String sumtime) {
        this.sumtime = sumtime;
    }

    public String getHall_name() {
        return hall_name;
    }

    public void setHall_name(String hall_name) {
        this.hall_name = hall_name;
    }

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
}
