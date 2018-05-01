package com.cb.project.gracefulmovies.presenter.impl;

import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.server.ApiException;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.server.ApiSubscriber;
import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.presenter.IMovieListPresenter;
import com.cb.project.gracefulmovies.server.ApiException;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.server.ApiSubscriber;
import com.cb.project.gracefulmovies.view.iview.IMovieListActivity;

import java.util.List;

public class MovieListPresenterImpl implements IMovieListPresenter {

    private IMovieListActivity mactivity;
    private ApiSubscriber<List<PlanModel>> mSubscriber;
    private int mErrCode = -1;
    private static String name;

    public MovieListPresenterImpl() {
    }

    public MovieListPresenterImpl(String name) {
        this.name = name;
    }

    @Override
    public void register(IMovieListActivity activity) {
        this.mactivity=activity;
    }

    @Override
    public void loadData() {
        mSubscriber=new ApiSubscriber<List<PlanModel>>() {
            @Override
            public void onNext(List<PlanModel> planModel) {
                if(null!=mactivity){
                    mactivity.onDataReady(planModel);
                }
            }
            @Override
            public void onError(Throwable e) {
                if (e instanceof ApiException)
                    mErrCode = ((ApiException) e).getCode();
                super.onError(e);
            }

            @Override
            protected void onError(String msg) {
                if (mactivity != null) {
                    mactivity.onDataError(mErrCode, msg);
                }
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        };
        ApiHelper.loadPlans(name).subscribe(mSubscriber);


    }

    @Override
    public void unregister() {
        if (mSubscriber != null && mSubscriber.isUnsubscribed())
            mSubscriber.unsubscribe();
        mactivity = null;
    }
}
