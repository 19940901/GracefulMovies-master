package com.cb.project.gracefulmovies.view.iview;

import com.cb.project.gracefulmovies.model.Order;
import com.cb.project.gracefulmovies.model.Uorder;

import java.util.List;

public interface IMyOrderActivity {
    void onDataReady(List<Order> modelList);

    void onDataError(int code, String msg);
}
