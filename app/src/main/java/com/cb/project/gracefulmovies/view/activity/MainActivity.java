package com.cb.project.gracefulmovies.view.activity;

import android.app.Application;
import android.content.*;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.cb.project.gracefulmovies.R;
import com.cb.project.gracefulmovies.dialog.LoginDialogFragment;
import com.cb.project.gracefulmovies.dialog.PhoneDialogFragment;
import com.cb.project.gracefulmovies.model.LoginResult;
import com.cb.project.gracefulmovies.model.Uorder;
import com.cb.project.gracefulmovies.presenter.IMainActivityPresenter;
import com.cb.project.gracefulmovies.presenter.impl.MainActivityPresenterImpl;
import com.cb.project.gracefulmovies.util.PrefUtil;
import com.cb.project.gracefulmovies.util.Util;
import com.cb.project.gracefulmovies.view.adapter.TabPagerAdapter;
import com.cb.project.gracefulmovies.view.fragment.MovieListFragment;
import com.cb.project.gracefulmovies.view.iview.IMainActivity;
import com.cb.project.gracefulmovies.view.service.LocationService;
import com.cb.project.gracefulmovies.view.service.UserService;
import com.cb.project.gracefulmovies.view.widget.UnScrollableViewPager;
import org.polaric.colorful.Colorful;

import java.util.Calendar;
import java.util.List;

/**
 * 首页
 * <p/>
 * Created by woxingxiao on 2017-01-25.
 */
public class MainActivity extends CheckPermissionsActivity implements NavigationView
        .OnNavigationItemSelectedListener, IMainActivity {

    @BindView(com.cb.project.gracefulmovies.R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(com.cb.project.gracefulmovies.R.id.view_pager)
    UnScrollableViewPager mViewPager;
    @BindView(com.cb.project.gracefulmovies.R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(com.cb.project.gracefulmovies.R.id.now_radio)
    RadioButton mNowBtn;
    @BindView(com.cb.project.gracefulmovies.R.id.coming_radio)
    RadioButton mComingBtn;
    private boolean loginFlag = true;

    private View mStatusView;
    private ImageView mStatusLoadingImg;
    private ImageView mStatusNoDataImg;
    private Button mStatusReloadBtn;

    private SwitchCompat mSwitch;
    private TextView mCityText;

    private MyReceiver mReceiver;
    private String mAutoSwitchedHint;

    private IMainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearSp();

        //测试login
        setContentView(com.cb.project.gracefulmovies.R.layout.activity_main);
       // PhoneDialogFragment fragment = new PhoneDialogFragment();
        //登录检查
        new UserService(this).loadDatas(this);





        transparentStatusBar();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(getString(com.cb.project.gracefulmovies.R.string.action_locate_succeed)));

        setContentView(com.cb.project.gracefulmovies.R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(com.cb.project.gracefulmovies.R.id.toolbar);
        setSupportActionBar(toolbar);
        int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resId);

        FrameLayout.LayoutParams lp1 = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        lp1.topMargin = statusBarHeight;
        toolbar.setLayoutParams(lp1);

        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) mRadioGroup.getLayoutParams();
        lp2.topMargin = Util.dp2px(56) + statusBarHeight;
        mRadioGroup.setLayoutParams(lp2);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                com.cb.project.gracefulmovies.R.string.navigation_drawer_open, com.cb.project.gracefulmovies.R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(com.cb.project.gracefulmovies.R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        mSwitch = (SwitchCompat) navView.getHeaderView(0).findViewById(com.cb.project.gracefulmovies.R.id.day_night_mode_switch);
        mCityText = (TextView) navView.getHeaderView(0).findViewById(com.cb.project.gracefulmovies.R.id.nav_city_text);
        ImageView img = (ImageView) navView.getHeaderView(0).findViewById(com.cb.project.gracefulmovies.R.id.nav_header_img);
        Glide.with(this).load(com.cb.project.gracefulmovies.R.drawable.pic_movies).into(img);

        showStatusView(true);

        MovieListFragment[] fragments = new MovieListFragment[2];
        fragments[0] = MovieListFragment.newInstance(0);
        fragments[1] = MovieListFragment.newInstance(1);

        TabPagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), fragments);
        adapter.setTabTitles(new String[]{getString(com.cb.project.gracefulmovies.R.string.has_released), getString(com.cb.project.gracefulmovies.R.string.going_to_release)});
        mViewPager.setAdapter(adapter);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int position = checkedId == com.cb.project.gracefulmovies.R.id.now_radio ? 0 : 1;
                if (mViewPager.getCurrentItem() == position)
                    return;

                mViewPager.setCurrentItem(position);
            }
        });
        String font = "font.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), font);
        mNowBtn.setTypeface(typeface);
        mComingBtn.setTypeface(typeface);

        mCityText.setText(PrefUtil.getCityShort());
        mCityText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocatedCityDialog(false, false);
            }
        });

        if (savedInstanceState == null) {
            LocationService.start(this);

            if (checkAutoDayNightMode()) {
                getPresenter().loadMovieData();
            }
        } else {
            getPresenter().loadMovieData();

            String hint = savedInstanceState.getString("hint", null);
            if (hint != null) {
                Snackbar.make(mDrawerLayout, hint, 2000)
                        .setAction(getString(com.cb.project.gracefulmovies.R.string.revoke), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PrefUtil.setAutoDayNightMode(false);
                                switchDayNightModeSmoothly(!Colorful.getThemeDelegate().isNight(), false);
                            }
                        })
                        .show();
            }
        }
    }

    private void showStatusView(boolean loading) {
        if (mStatusView == null) {
            mStatusView = ((ViewStub) findViewById(com.cb.project.gracefulmovies.R.id.view_stub)).inflate();
            mStatusLoadingImg = (ImageView) mStatusView.findViewById(com.cb.project.gracefulmovies.R.id.status_loading_img);
            mStatusReloadBtn = (Button) mStatusView.findViewById(com.cb.project.gracefulmovies.R.id.status_reload_button);
            mStatusNoDataImg = (ImageView) mStatusView.findViewById(com.cb.project.gracefulmovies.R.id.status_no_data_img);

            mStatusReloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusView(true);
                    getPresenter().loadMovieData();
                }
            });
        }
        if (loading) {
            Animation animation = AnimationUtils.loadAnimation(this, com.cb.project.gracefulmovies.R.anim.anim_rotate);
            animation.setInterpolator(new LinearInterpolator());
            mStatusLoadingImg.setAnimation(animation);
            mStatusLoadingImg.setVisibility(View.VISIBLE);
            mStatusReloadBtn.setVisibility(View.INVISIBLE);
            mStatusNoDataImg.setVisibility(View.INVISIBLE);
        } else {
            mStatusLoadingImg.clearAnimation();
            mStatusLoadingImg.setVisibility(View.INVISIBLE);
            mStatusReloadBtn.setVisibility(View.VISIBLE);
            mStatusNoDataImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSwitch.setChecked(Colorful.getThemeDelegate().isNight());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mDrawerLayout.addDrawerListener(new MyDrawerListener());
                mDrawerLayout.closeDrawer(GravityCompat.START);

                if (PrefUtil.isAutoDayNightMode()) {
                    Toast.makeText(MainActivity.this, getString(com.cb.project.gracefulmovies.R.string.hint_auto_day_night_disabled),
                            Toast.LENGTH_LONG).show();
                    PrefUtil.setAutoDayNightMode(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.cb.project.gracefulmovies.R.menu.main, menu);
        final MenuItem item1 = menu.findItem(com.cb.project.gracefulmovies.R.id.action_search);
        item1.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item1);
            }
        });
        final MenuItem item2 = menu.findItem(com.cb.project.gracefulmovies.R.id.action_box_office);
        item2.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item2);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == com.cb.project.gracefulmovies.R.id.action_search) {
            navigateWithRippleCompat(
                    this,
                    new Intent(this, SearchActivity.class),
                    item.getActionView(),
                    Colorful.getThemeDelegate().getPrimaryColor().getColorRes()
            );
            return true;
        } else if (item.getItemId() == com.cb.project.gracefulmovies.R.id.action_box_office) {
            navigateWithRippleCompat(
                    this,
                    new Intent(this, BoxOfficeActivity.class),
                    item.getActionView(),
                    Colorful.getThemeDelegate().getPrimaryColor().getColorRes()
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case com.cb.project.gracefulmovies.R.id.nav_theme:
                navigate(ThemeActivity.class);
                break;
            case com.cb.project.gracefulmovies.R.id.nav_settings:
                navigate(SettingsActivity.class);
                break;
            case com.cb.project.gracefulmovies.R.id.nav_share: // 分享
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, getString(com.cb.project.gracefulmovies.R.string.share_out_description));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享"));
                break;
            case com.cb.project.gracefulmovies.R.id.nav_evaluate: // 评分
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent2, "评价"));
                break;
            case com.cb.project.gracefulmovies.R.id.nav_about:
                navigate(AboutActivity.class);
                break;
            case  R.id.nav_mine:
                SharedPreferences user = getSharedPreferences("user", Application.MODE_PRIVATE);
                String name = user.getString("name", "");
                String id = user.getString("id", "");
                if (null != name && null != id && !name.isEmpty() && !id.isEmpty()) {

                    Intent intent1=new Intent(this,MyOrderActivity.class);
                    intent1.putExtra("id",id);
                    startActivity(intent1);

                } else {
                    LoginDialogFragment fragment = LoginDialogFragment.newInstance("login");
                    fragment.show(getFragmentManager(), "login");
                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.cb.project.gracefulmovies.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 检测是否自动日夜模式，如果是自动将根据时间判断是否切换
     *
     * @return 是否立即加载数据
     */
    private boolean checkAutoDayNightMode() {
        boolean firstTime = PrefUtil.checkFirstTime();
        if (firstTime)
            PrefUtil.setNotFirstTime();
        boolean auto = PrefUtil.isAutoDayNightMode();
        if (firstTime || !auto)
            return true;

        int[] dayTime = PrefUtil.getDayNightModeStartTime(true);
        int[] nightTime = PrefUtil.getDayNightModeStartTime(false);
        Calendar cal = Calendar.getInstance();
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        if (Colorful.getThemeDelegate().isNight()) {
            if ((dayTime[0] < h && h < nightTime[0]) || (dayTime[0] == h && dayTime[1] <= m)) {
                switchDayNightModeSmoothly(false, true);
                return false;
            } else {
                return true;
            }
        } else {
            if ((nightTime[0] < h) || (nightTime[0] == h && nightTime[1] <= m)) {
                switchDayNightModeSmoothly(true, true);
                return false;
            } else {
                return true;
            }
        }
    }

    private void switchDayNightModeSmoothly(final boolean isDark, boolean delay) {
        if (delay) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Colorful.config(MainActivity.this).night(isDark).apply();
                    getWindow().setWindowAnimations(com.cb.project.gracefulmovies.R.style.WindowAnimationFadeInOut);
                    mAutoSwitchedHint = "已自动切换为" +
                            (isDark ? getString(com.cb.project.gracefulmovies.R.string.night_mode) : getString(com.cb.project.gracefulmovies.R.string.day_mode));
                    recreate();
                }
            }, 1000);
        } else {
            Colorful.config(MainActivity.this).night(isDark).apply();
            getWindow().setWindowAnimations(com.cb.project.gracefulmovies.R.style.WindowAnimationFadeInOut);
            recreate();
        }
    }

    @Override
    protected void onAllPermissionsGranted() {
        LocationService.start(this);
    }

    @Override
    public IMainActivityPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new MainActivityPresenterImpl(this);
        }
        return mPresenter;
    }

    @Override
    public void onFragmentRefreshDataReady(int fragmentId) {
        String tag = "android:switcher:" + mViewPager.getId() + ":" + fragmentId;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null && fragment instanceof MovieListFragment) {
            ((MovieListFragment) fragment).onDataReady(mPresenter.getMovieModels(fragmentId));
        }

        mStatusView.setVisibility(View.GONE);
    }

    @Override
    public void onDataError(int code, String msg) {
        if (code == 209405) { // "查询不到热映电影相关信息"，以上一级城市名进行查询
            showLocatedCityDialog(false, true);
            return;
        }

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment instanceof MovieListFragment) {
                    ((MovieListFragment) fragment).onDataError();
                }
            }
        }

        showStatusView(false);
        showToast(msg);
    }

    long mStartMills;

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mStartMills > 1000) {
                showToast("再按一次，退出程序");
                mStartMills = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("hint", mAutoSwitchedHint);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.unregister();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private class MyDrawerListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerClosed(View drawerView) {
            switchDayNightModeSmoothly(mSwitch.isChecked(), false);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerStateChanged(int newState) {
        }
    }

    /**
     * 监听定位完成广播
     */
    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (getString(com.cb.project.gracefulmovies.R.string.action_locate_succeed).equals(intent.getAction())) {
                mCityText.setText(PrefUtil.getCityShort());

                showLocatedCityDialog(true, intent.getBooleanExtra(getString(com.cb.project.gracefulmovies.R.string.param_is_upper_city), false));
            }
        }
    }

    private void showLocatedCityDialog(final boolean refresh, final boolean upperCity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(com.cb.project.gracefulmovies.R.string.location_default));
        final View view = getLayoutInflater().inflate(com.cb.project.gracefulmovies.R.layout.layout_location_dialog, null);
        final TextView msgText = (TextView) view.findViewById(com.cb.project.gracefulmovies.R.id.dialog_loc_msg_text);
        final TextView manualText = (TextView) view.findViewById(com.cb.project.gracefulmovies.R.id.dialog_loc_input_manually_text);
        final View inputLayout = view.findViewById(com.cb.project.gracefulmovies.R.id.dialog_loc_input_layout);
        final EditText inputEdit = (EditText) view.findViewById(com.cb.project.gracefulmovies.R.id.dialog_loc_edit);

        if (upperCity) {
            msgText.setText(getString(com.cb.project.gracefulmovies.R.string.hint_query_by_upper_city, PrefUtil.getCity(),
                    PrefUtil.getUpperCity()));
        } else {
            msgText.setText(getString(com.cb.project.gracefulmovies.R.string.hint_located_city, PrefUtil.getCity()));
        }
        String hint = getString(com.cb.project.gracefulmovies.R.string.hint_input_city_manually);
        SpannableStringBuilder span = new SpannableStringBuilder(hint);
        span.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(this, Colorful.getThemeDelegate().getAccentColor().getColorRes())),
                hint.length() - 4, hint.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        manualText.setText(span);
        manualText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputLayout.getVisibility() != View.VISIBLE)
                    inputLayout.setVisibility(View.VISIBLE);
            }
        });

        builder.setView(view);
        builder.setNegativeButton(getString(com.cb.project.gracefulmovies.R.string.locate_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PrefUtil.clearCity();
                LocationService.start(MainActivity.this);
            }
        });
        builder.setPositiveButton(getString(com.cb.project.gracefulmovies.R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (inputLayout.getVisibility() == View.VISIBLE) {
                    if (inputEdit.getText().toString().trim().isEmpty()) {
                        showToast(getString(com.cb.project.gracefulmovies.R.string.hint_distract_input_empty));
                    } else {
                        PrefUtil.setInputtedCity(true);
                        PrefUtil.setCity(inputEdit.getText().toString().trim());
                        mCityText.setText(PrefUtil.getCityShort());
                        getPresenter().loadMovieData();
                    }
                    return;
                }

                if (upperCity) {
                    PrefUtil.setCity(PrefUtil.getUpperCity());
                    mCityText.setText(PrefUtil.getCityShort());
                }
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment instanceof MovieListFragment) {
                            if (refresh) {
                                ((MovieListFragment) fragment).onRefresh();
                            }
                            if (upperCity) {
                                ((MovieListFragment) fragment).onDataError();
                            }
                        }
                    }
                }
            }
        });
        builder.show();
    }

    public void checkCookie(LoginResult loginResult) {

        if (null != loginResult && loginResult.getState().equals("true")) {
            SharedPreferences user = getSharedPreferences("user",  Application.MODE_PRIVATE);
            SharedPreferences.Editor editor = user.edit();
            editor.putString("id", loginResult.getId());
            editor.putString("name", loginResult.getName());
            editor.commit();
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);



            builder.setTitle("登录" + loginResult.getState());

            builder.show();


        } else {
            LoginDialogFragment fragment = LoginDialogFragment.newInstance("login");
            fragment.show(getFragmentManager(), "login");
        }

    }

    public void  clearSp(){
        SharedPreferences user = getSharedPreferences("user",  Application.MODE_PRIVATE);
        SharedPreferences.Editor edit = user.edit();
        edit.clear();
        edit.commit();
    }
}
