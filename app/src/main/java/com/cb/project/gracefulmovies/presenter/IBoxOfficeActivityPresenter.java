package com.cb.project.gracefulmovies.presenter;

import com.cb.project.gracefulmovies.view.iview.IBoxOfficeActivity;
import com.cb.project.gracefulmovies.view.iview.IBoxOfficeActivity;

/**
 * <p/>
 * Created by cb on 2018-4-30.
 */
public interface IBoxOfficeActivityPresenter {

    void register(IBoxOfficeActivity activity);

    void loadData();

    void unregister();
}
