package com.cb.project.gracefulmovies.view.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cb.project.gracefulmovies.R;

import org.polaric.colorful.Colorful;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public class AboutActivity extends BaseActivity {

    @BindView(com.cb.project.gracefulmovies.R.id.about_header_img)
    ImageView mHeaderImg;
    @BindView(com.cb.project.gracefulmovies.R.id.version_name_text)
    TextView mVersionNameText;
    @BindView(com.cb.project.gracefulmovies.R.id.about_github_img)
    ImageView mGitHubImg;
    @BindView(com.cb.project.gracefulmovies.R.id.about_license_text)
    TextView mLicenseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusBar();

        setContentView(com.cb.project.gracefulmovies.R.layout.activity_about);
        ButterKnife.bind(this);

        initializeToolbar();

        Glide.with(this).load(com.cb.project.gracefulmovies.R.drawable.pic_movie_projector).into(mHeaderImg);
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            String s = String.format(Locale.CHINA, "版本：%s（Build %d）", info.versionName, info.versionCode);
            mVersionNameText.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
            mVersionNameText.setText("");
        }
        if (Colorful.getThemeDelegate().isNight()) {
            Drawable drawable = mGitHubImg.getDrawable();
            if (drawable != null) {
                DrawableCompat.setTint(drawable, Color.WHITE);
                mGitHubImg.setImageDrawable(drawable);
            }
        }
        mLicenseText.setText(Html.fromHtml(getString(com.cb.project.gracefulmovies.R.string.license)));
    }

    @OnClick({com.cb.project.gracefulmovies.R.id.about_gmail_img, com.cb.project.gracefulmovies.R.id.about_github_img, com.cb.project.gracefulmovies.R.id.about_license_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case com.cb.project.gracefulmovies.R.id.about_gmail_img:
                String s1 = "邮箱";
                String s2 = getString(com.cb.project.gracefulmovies.R.string.gmail_address);

                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("content", s2);
                cmb.setPrimaryClip(clipData);

                showToast(getString(com.cb.project.gracefulmovies.R.string.hint_clipboard, s1, s2));
                break;
            case com.cb.project.gracefulmovies.R.id.about_github_img:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(getString(com.cb.project.gracefulmovies.R.string.github_page)));
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
                break;
            case com.cb.project.gracefulmovies.R.id.about_license_text:
                navigate(OpenLicenseActivity.class);
                break;
        }
    }

}
