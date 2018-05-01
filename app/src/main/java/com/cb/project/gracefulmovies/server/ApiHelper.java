package com.cb.project.gracefulmovies.server;


import com.cb.project.gracefulmovies.server.api.BoxOfficeApi;
import com.cb.project.gracefulmovies.server.api.MovieApi;
import com.cb.project.gracefulmovies.server.api.NetLocApi;
import com.cb.project.gracefulmovies.model.*;
import com.cb.project.gracefulmovies.server.api.BoxOfficeApi;
import com.cb.project.gracefulmovies.server.api.MovieApi;
import com.cb.project.gracefulmovies.server.api.NetLocApi;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cb.project.gracefulmovies.server.api.PlanApi;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Api操作助手类
 * <p/>
 * Created by woxingxiao on 2017-02-10.
 */
public class ApiHelper {

    private static String API_KEY_JH;
    private static String APP_ID_YY;


    private static MovieApi movieApi;
    private static NetLocApi netLocApi;
    private static BoxOfficeApi boxOfficeApi;
    private static PlanApi planApi;

    public static void init(String apiKeyJH, String appId) {
        API_KEY_JH = apiKeyJH;
        APP_ID_YY = appId;

    }

    private static MovieApi getMovieApi() {
        if (movieApi == null) {
            movieApi = new ApiClient().createApi(MovieApi.class);
        }

        return movieApi;
    }

    private static NetLocApi getNetLocApi() {
        if (netLocApi == null) {
            netLocApi = new ApiClient().createApi("http://gc.ditu.aliyun.com/", NetLocApi.class);
        }

        return netLocApi;
    }

    private static BoxOfficeApi getBoxOfficeApi() {
        if (boxOfficeApi == null) {
            boxOfficeApi = new ApiClient().createApi("http://api.shenjian.io/", BoxOfficeApi.class);
        }

        return boxOfficeApi;
    }

    private static PlanApi getPlanApi() {
        if (planApi == null) {
            planApi = new ApiClient().createApi("http://119.23.213.11:8080/back/act/plans/", PlanApi.class);
        }
        return planApi;
    }

    /**
     * 加载所在城市的影讯
     */
    public static Observable<MovieReleaseType[]> loadMovies(String city) {
        return getMovieApi()
                .movieInfoGet(API_KEY_JH, city)
                .map(new Func1<RequestResult<MovieData>, MovieData>() {
                    @Override
                    public MovieData call(RequestResult<MovieData> requestResult) {
                        if (requestResult.getError_code() != 0) {
                            /**
                             * 抛出业务异常
                             */
                            throw new ApiException(requestResult.getError_code(), requestResult.getReason());
                        }
                        return requestResult.getResult();
                    }
                })
                .map(new Func1<MovieData, MovieReleaseType[]>() {
                    @Override
                    public MovieReleaseType[] call(MovieData movieData) {
                        return movieData.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索影讯
     */
    public static Observable<MovieSearchModel> searchMovie(String name) {
        return getMovieApi()
                .movieSearchGet(API_KEY_JH, name)
                .map(new Func1<RequestResult<MovieSearchModel>, MovieSearchModel>() {
                    @Override
                    public MovieSearchModel call(RequestResult<MovieSearchModel> requestResult) {
                        if (requestResult.getError_code() != 0) {
                            /**
                             * 抛出业务异常
                             */
                            throw new ApiException(requestResult.getError_code(), requestResult.getReason());
                        }
                        return requestResult.getResult();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 经纬度转地址
     */
    public static Observable<NetLocResult> loadNetLoc(String latLng) {
        return getNetLocApi()
                .getNetLoc(latLng)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 加载票房数据
     */
    public static Observable<List<BoxOfficeModel>> loadBoxOffice() {
        return getBoxOfficeApi()
                .dayBoxOfficeGet(APP_ID_YY)
                .map(new Func1<BoxOfficeResult, List<BoxOfficeModel>>() {
                    @Override
                    public List<BoxOfficeModel> call(BoxOfficeResult boxOfficeResult) {
                        return boxOfficeResult.getResult();
                    }
                })
//                .map(new Func1<BoxOfficeResult.result, List<BoxOfficeModel>>() {
//                    @Override
//                    public List<BoxOfficeModel> call(BoxOfficeResult.result boxOfficeData) {
////                        if (boxOfficeData.modelList != null && !boxOfficeData.modelList.isEmpty()) {
////                            Collections.sort(boxOfficeData.modelList, new Comparator<BoxOfficeModel>() {
////                                @Override
////                                public int compare(BoxOfficeModel o1, BoxOfficeModel o2) {
////                                    return o1.getRankInt() - o2.getRankInt();
////                                }
////                            });
////                        }
//                        return boxOfficeData.;
//
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 加载上映数据
     */
    public static Observable<List<PlanModel>> loadPlans(String name) {
        return getPlanApi()
                .getmoviePlan(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void releaseNetLocApi() {
        netLocApi = null;
    }

    public static void releaseBoxOfficeApi() {
        boxOfficeApi = null;
    }

    public static void releasePlanApi() {
        planApi = null;
    }

}
