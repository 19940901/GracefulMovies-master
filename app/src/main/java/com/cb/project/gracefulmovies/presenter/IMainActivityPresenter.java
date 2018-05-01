package com.cb.project.gracefulmovies.presenter;

import com.cb.project.gracefulmovies.model.MovieModel;
import com.cb.project.gracefulmovies.model.MovieModel;

import java.util.List;

/**
 * <p/>
 * Created by woxingxiao on 2017-04-01.
 */
public interface IMainActivityPresenter {

    void onFragmentInitOK(int fragmentId);

    void onFragmentRefreshRequest(int fragmentId);

    void loadMovieData();

    List<MovieModel> getMovieModels(int fragmentId);

    void unregister();
}
