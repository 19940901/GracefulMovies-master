package com.cb.project.gracefulmovies.server.api;

import com.cb.project.gracefulmovies.model.Uorder;
import com.cb.project.gracefulmovies.model.orderResult;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface AddOrderapi {
    @POST(" ")
    Observable<orderResult> addorder(@Query("state")String state,
                                     @Query("price")int price,
                                     @Query("plan_id")int plan_id,
                                     @Query("seatNumber")String seatNumber,
                                     @Query("user_id")int user_id);
}
