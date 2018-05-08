package com.cb.project.gracefulmovies.presenter.impl;

import com.cb.project.gracefulmovies.model.Order;
import com.cb.project.gracefulmovies.presenter.IOrderpresenter;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.server.ApiSubscriber;
import com.cb.project.gracefulmovies.view.iview.IMyOrderActivity;

import java.util.List;

public class OrderPresenterImpl implements IOrderpresenter {
    private IMyOrderActivity activity;
    private ApiSubscriber<List<Order>> subscriber;

    private static int id;
    private static int mErrCode=-1;

    public OrderPresenterImpl() {

    }


    public OrderPresenterImpl(int id) {
        this.id = id;
    }


    @Override
    public void register(IMyOrderActivity activity) {
        this.activity = activity;
    }

    @Override
    public void loadData() {
        subscriber = new ApiSubscriber<List<Order>>() {
            @Override
            public void onNext(List<Order> orders) {
                super.onNext(orders);
                if(null!=activity){
                    activity.onDataReady(orders);
                }
            }

            @Override
            protected void onError(String msg) {
                if (activity != null) {
                    activity.onDataError(mErrCode, msg);
                }
            }


        };
        ApiHelper.loadOrder(id).subscribe(subscriber);
    }

    @Override
    public void unregister() {
        if (subscriber != null && subscriber.isUnsubscribed())
            subscriber.unsubscribe();
        activity = null;
    }
}
