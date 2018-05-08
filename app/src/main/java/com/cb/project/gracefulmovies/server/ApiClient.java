package com.cb.project.gracefulmovies.server;

import android.app.Application;
import android.content.Context;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.cb.project.gracefulmovies.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
class ApiClient {

    private Retrofit.Builder mRetrofitBuilder;
    private OkHttpClient.Builder mOkHttpClientBuilder;

    ApiClient() {
        mRetrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());

        mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.connectTimeout(15, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            mOkHttpClientBuilder.addNetworkInterceptor(
                    new LoggingInterceptor.Builder()
                            .loggable(BuildConfig.DEBUG)
                            .setLevel(Level.BODY)
                            .log(Platform.INFO)
                            .request("Request")
                            .response("Response")

                            .build()
            );
        }
    }

    <S> S createApi(String baseUrl, Class<S> ApiClass) {
        mRetrofitBuilder.baseUrl(baseUrl);

        return createApi(ApiClass);
    }

    <S> S createApi(Class<S> ApiClass) {
        return mRetrofitBuilder
                .client(mOkHttpClientBuilder.build())
                .build()
                .create(ApiClass);
    }

    <S> S createApi(String baseUrl, Class<S> ApiClass,Context context){
        //持久化cookie
        //mOkHttpClientBuilder.interceptors().add(new AddCookiesInterceptor(this,"text"));
        mRetrofitBuilder.baseUrl(baseUrl);
        ClearableCookieJar cookieJar=new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(context));
        mOkHttpClientBuilder.cookieJar(cookieJar);

       return createApi(ApiClass);

    }

}
