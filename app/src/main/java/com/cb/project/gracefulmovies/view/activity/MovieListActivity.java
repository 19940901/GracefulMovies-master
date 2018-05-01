package com.cb.project.gracefulmovies.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.presenter.IMovieListPresenter;
import com.cb.project.gracefulmovies.presenter.impl.MovieListPresenterImpl;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.view.adapter.MoviePlanListAdapter;
import com.cb.project.gracefulmovies.view.iview.IMovieListActivity;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.presenter.IMovieListPresenter;
import com.cb.project.gracefulmovies.presenter.impl.MovieListPresenterImpl;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.view.adapter.MoviePlanListAdapter;
import com.cb.project.gracefulmovies.view.iview.IMovieListActivity;
import org.polaric.colorful.Colorful;

import java.util.List;


public class  MovieListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener ,IMovieListActivity {
    @BindView(com.cb.project.gracefulmovies.R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(com.cb.project.gracefulmovies.R.id.recycler_view)
    RecyclerView mRecyclerView;

    private IMovieListPresenter mPresenter;
    private MoviePlanListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cb.project.gracefulmovies.R.layout.activity_movie_list);
        ButterKnife.bind(this);
        initializeToolbar();
        FloatingActionButton fab= (FloatingActionButton) findViewById(com.cb.project.gracefulmovies.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.getData() != null && !mAdapter.getData().isEmpty())
                    mRecyclerView.smoothScrollToPosition(0);
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, Colorful.getThemeDelegate().getAccentColor().getColorRes()),
                ContextCompat.getColor(this, Colorful.getThemeDelegate().getPrimaryColor().getColorRes())
        );
        mSwipeRefreshLayout.setProgressViewEndTarget(false, (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 80, Resources.getSystem().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        Intent intent=getIntent();
        String MovieName=intent.getStringExtra("name");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MoviePlanListAdapter(MovieName);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);





        mPresenter = new MovieListPresenterImpl(MovieName);
        mPresenter.register(this);

        mPresenter.loadData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.cb.project.gracefulmovies.R.menu.box_office, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData();
        mAdapter.setLoading(true);
    }

    @Override
    public void onDataReady(List<PlanModel> modelList) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.setData(modelList);
    }

    @Override
    public void onDataError(int code, String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.clearData();
        mAdapter.setLoading(false);
        showToast(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unregister();
        mPresenter = null;
        ApiHelper.releasePlanApi();
    }
}
