package com.cb.project.gracefulmovies.server.api;


import com.cb.project.gracefulmovies.model.BoxOfficeResult;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 票房接口
 * <p/>
 * Created by woxingxiao on 2017-03-07.
 */
public interface BoxOfficeApi {
    @Headers("Accept-Encoding:utf-8")
    @GET(" ")
    Observable<BoxOfficeResult> dayBoxOfficeGet(@Query("appid") String appId);

}
