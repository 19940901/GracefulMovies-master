package com.cb.project.gracefulmovies.view.iview;

import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.model.PlanModel;

import java.util.List;

public interface IMovieListActivity {
    void onDataReady(List<PlanModel> modelList);

    void onDataError(int code, String msg);
}
