package com.cb.project.gracefulmovies.presenter;


import com.cb.project.gracefulmovies.view.iview.IMovieListActivity;
import com.cb.project.gracefulmovies.view.iview.IMovieListActivity;

public interface IMovieListPresenter {
    void register(IMovieListActivity activity);

    void loadData();

    void unregister();
}
