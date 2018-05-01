package com.cb.project.gracefulmovies.server.api;


import com.cb.project.gracefulmovies.model.PlanModel;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import java.util.List;


public interface PlanApi {
    @POST(" ")
    Observable<List<PlanModel>> getmoviePlan(@Query("name") String name);
}
