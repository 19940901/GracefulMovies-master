package com.cb.project.gracefulmovies.view.activity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.dialog.LoginDialogFragment;
import com.cb.project.gracefulmovies.model.PlanModel;
import com.cb.project.gracefulmovies.model.Uorder;
import com.cb.project.gracefulmovies.presenter.IMovieTableActivityPresenter;
import com.cb.project.gracefulmovies.presenter.impl.MovieTableActivityPresenterImpl;
import com.cb.project.gracefulmovies.view.service.UserService;
import com.qfdqc.views.seattable.SeatTable;

import java.util.List;


public class MovieTableActivity extends BaseActivity {
    public SeatTable seatTableView;

    private IMovieTableActivityPresenter mPresenter;

    private PlanModel plan;
    private String seat;

    private List<String> s;

    private UserService userService;

    private Uorder order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(com.cb.project.gracefulmovies.R.layout.activity_movie_seat);

        ButterKnife.bind(this);

        initializeToolbar();
        seatTableView = (SeatTable) findViewById(com.cb.project.gracefulmovies.R.id.seatView);
        Intent intent = getIntent();
        plan = (PlanModel) intent.getSerializableExtra("plan");
        seatTableView.setScreenName(plan.getHall_Name() + " " + plan.getHall_id() + "号厅");//设置屏幕名称
        seatTableView.setMaxSelected(1);//设置最多选中

        mPresenter = new MovieTableActivityPresenterImpl(plan.getHall_id());
        mPresenter.register(this);
        mPresenter.loadData();

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if (column == 2) {
                    return false;
                }
                return true;
            }

            /*
            逐个座位检查,当seat表中有该值时显示true,表示已经售出
             */
            @Override
            public boolean isSold(int row, int column) {
//
                String a = row + "-" + column;
                if (null != s && s.contains(a)) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                seat = (row ) + "-" + (column);
            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(10, 15);


    }

    @Override
    protected void initializeToolbar() {
        super.initializeToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // String seat=col+"-"+row;
        SharedPreferences user = getSharedPreferences("user", Application.MODE_PRIVATE);
        String name = user.getString("name", "");
        String id = user.getString("id", "");
        if (null != name && null != id && !name.isEmpty() && !id.isEmpty()) {
            //System.out.println(name + " " + id);
            order = new Uorder();
            //命名错误，hall_id就是plan_id
            order.setPlan_id(Integer.valueOf(plan.getHall_id()));
            order.setPrice(plan.getPrice());
            order.setSeatNumber(seat);
            order.setState("未付款");
            order.setUser_id(Integer.valueOf(id));
            userService = new UserService(this, order);
            userService.addOrder();
            //Intent intent=new Intent(MovieTableActivity.class,MyCartActivity.class);

        } else {
            LoginDialogFragment fragment = LoginDialogFragment.newInstance("login");
            fragment.show(getFragmentManager(), "login");
        }
        return super.onOptionsItemSelected(item);

    }

    public void soldSeats(List<String> list) {
        this.s = list;
    }

    public void addOrder(String s) {
        if (null != s && s.equals("ok")) {

            Intent intent = new Intent(this, MyOrderActivity.class);
            intent.putExtra("id", order.getUser_id());
            startActivity(intent);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(seat + plan.getHall_id() + " " + id + " " + name);
//            builder.show();
        } else {
            Toast.makeText(this, "添加订单错误，请重试", Toast.LENGTH_LONG);
        }
    }


}
