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
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.model.Order;
import com.cb.project.gracefulmovies.model.Uorder;
import com.cb.project.gracefulmovies.presenter.IOrderpresenter;
import com.cb.project.gracefulmovies.presenter.impl.OrderPresenterImpl;
import com.cb.project.gracefulmovies.server.ApiHelper;
import com.cb.project.gracefulmovies.view.adapter.MyOrderAdaptor;
import com.cb.project.gracefulmovies.view.iview.IMyOrderActivity;
import org.polaric.colorful.Colorful;

import java.util.List;

public class MyOrderActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IMyOrderActivity {
    @BindView(com.cb.project.gracefulmovies.R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(com.cb.project.gracefulmovies.R.id.recycler_view)
    RecyclerView mRecyclerView;


    private IOrderpresenter orderpresenter;
    private MyOrderAdaptor mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initializeToolbar();
        FloatingActionButton fab = (FloatingActionButton) findViewById(com.cb.project.gracefulmovies.R.id.fab);
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

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("id");
        //int user_id = order.getUser_id();
        int id = Integer.valueOf(user_id);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyOrderAdaptor();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(null);

        orderpresenter = new OrderPresenterImpl(id);
        orderpresenter.register(this);
        orderpresenter.loadData();

    }

    @Override
    public void onRefresh() {
        orderpresenter.loadData();
        mAdapter.setLoading(true);
    }

    @Override
    public void onDataReady(List<Order> modelList) {
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


        orderpresenter.unregister();
        orderpresenter = null;
        ApiHelper.releasePlanApi();
    }
}
