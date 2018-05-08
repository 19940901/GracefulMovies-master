package com.cb.project.gracefulmovies.presenter;

import com.cb.project.gracefulmovies.view.iview.IMyOrderActivity;

public interface IOrderpresenter {
    void register(IMyOrderActivity activity);

    void loadData();

    void unregister();
}
