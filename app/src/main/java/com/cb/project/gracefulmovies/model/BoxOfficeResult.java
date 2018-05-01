package com.cb.project.gracefulmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <><p/>
 * Created by woxingxiao on 2017-03-07.
 */

public class BoxOfficeResult {

    @SerializedName("error_code")
    private int error_code;

    @SerializedName("data")
    private List<BoxOfficeModel> result;

    @SerializedName("reason")
    private String reason;



    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<BoxOfficeModel> getResult() {
        return result;
    }

    public void setResult(List<BoxOfficeModel> result) {
        this.result = result;
    }
}
