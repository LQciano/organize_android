package com.organize4event.organize.services;

import com.organize4event.organize.models.Plan;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface PlanService {

    @GET("plan/{locale}")
    Call<ArrayList<Plan>> getPlan(@Path("locale") String locale);

    @GET("plan/{locale}/{code_enum}")
    Call<Plan> getPLanId(@Path("locale") String locale,
                         @Path("code_enum") int code_enum);

}
