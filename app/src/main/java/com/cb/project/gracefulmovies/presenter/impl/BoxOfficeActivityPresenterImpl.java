package com.cb.project.gracefulmovies.presenter.impl;

import com.cb.project.gracefulmovies.model.BoxOfficeModel;
import com.cb.project.gracefulmovies.server.ApiException;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.server.ApiSubscriber;
import com.cb.project.gracefulmovies.view.iview.IBoxOfficeActivity;
import com.cb.project.gracefulmovies.model.BoxOfficeModel;
import com.cb.project.gracefulmovies.presenter.IBoxOfficeActivityPresenter;
import com.cb.project.gracefulmovies.server.ApiException;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.server.ApiSubscriber;
import com.cb.project.gracefulmovies.view.iview.IBoxOfficeActivity;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */

public class BoxOfficeActivityPresenterImpl implements IBoxOfficeActivityPresenter {

    private IBoxOfficeActivity mActivity;
    private ApiSubscriber<List<BoxOfficeModel>> mSubscriber;
    private int mErrCode = -1;

    @Override
    public void register(IBoxOfficeActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void loadData() {
        mSubscriber = new ApiSubscriber<List<BoxOfficeModel>>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onNext(List<BoxOfficeModel> movieModels) {
                if (mActivity != null) {
                    mActivity.onDataReady(movieModels);
                }
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ApiException)
                    mErrCode = ((ApiException) e).getCode();
                super.onError(e);
            }

            @Override
            protected void onError(String msg) {
                if (mActivity != null) {
                    mActivity.onDataError(mErrCode, msg);
                }
            }

            @Override
            public void onFinally() {
                super.onFinally();
            }
        };

        ApiHelper.loadBoxOffice().subscribe(mSubscriber);
    }

    @Override
    public void unregister() {
        if (mSubscriber != null && mSubscriber.isUnsubscribed())
            mSubscriber.unsubscribe();
        mActivity = null;
    }
}
