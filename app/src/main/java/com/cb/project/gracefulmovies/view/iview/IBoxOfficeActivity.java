package com.cb.project.gracefulmovies.view.iview;

import com.cb.project.gracefulmovies.model.BoxOfficeModel;
import com.cb.project.gracefulmovies.model.BoxOfficeModel;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public interface IBoxOfficeActivity {

    void onDataReady(List<BoxOfficeModel> modelList);

    void onDataError(int code, String msg);
}
