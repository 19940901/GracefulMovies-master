package com.cb.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * <>
 * Created by woxingxiao on 2017-03-07.
 */
//{"Irank":"1","MovieName":"头号玩家","BoxOffice":"1035.13",
// "sumBoxOffice":"39640.36","movieDay":"4","boxPer":"64.91","time":"2018-04-02 9:31:31"}
public class BoxOfficeModel {

    @SerializedName(value = "Irank", alternate = {"MovieRank", "rank"})
    private String rank; // 排名
    @SerializedName("MovieName")
    private String name; // 名字
    @SerializedName(value = "BoxOffice", alternate = {"boxOffice", "WeekAmount"})
    private String boxOffice; // 票房
    @SerializedName(value = "sumBoxOffice", alternate = "SumWeekAmount")
    private String sumBoxOffice; // 总票房
    @SerializedName(value = "movieDay", alternate = "movieDays")
    private String movieDays; // 上映天数、月内天数
    @SerializedName("boxPer")
    private String boxPer;//票房环比

    @SerializedName("time")
    private String time; // 上映日期

    private int rankInt = -1;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getSumBoxOffice() {
        return sumBoxOffice;
    }

    public void setSumBoxOffice(String sumBoxOffice) {
        this.sumBoxOffice = sumBoxOffice;
    }

    public String getBoxPer() {
        return boxPer;
    }

    public void setBoxPer(String boxPer) {
        this.boxPer = boxPer;
    }

    public String getMovieDays() {
        return movieDays;
    }

    public void setMovieDays(String movieDays) {
        this.movieDays = movieDays;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRankInt() {
        if (rankInt < 0 && getRank() != null && !getRank().isEmpty()) {
            try {
                rankInt = Integer.valueOf(getRank());
            } catch (NumberFormatException e) {
                rankInt = 0;
            }
        }
        return rankInt;
    }
}
