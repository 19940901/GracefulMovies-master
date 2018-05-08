package com.cb.project.gracefulmovies.server.api;

import com.cb.project.gracefulmovies.model.Order;
import com.cb.project.gracefulmovies.model.Uorder;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import java.util.List;

public interface LoadOrderApi {

    @POST(" ")
    Observable<List<Order>> loadOrder(@Query("id") int id);
}
