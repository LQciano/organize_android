package com.organize4event.organize.controllers;

import android.content.Context;

import com.organize4event.organize.commons.ApiClient;
import com.organize4event.organize.listeners.ControllResponseListener;
import com.organize4event.organize.models.Plan;
import com.organize4event.organize.services.PlanService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class PlanControll extends Controll {
    public PlanControll(Context context) {
        super(context);
    }

    public void getPlan(String locale, final ControllResponseListener listener){
        PlanService service = ApiClient.getRetrofit().create(PlanService.class);
        service.getPlan(locale).enqueue(new Callback<ArrayList<Plan>>() {
            @Override
            public void onResponse(Response<ArrayList<Plan>> response, Retrofit retrofit) {
                ArrayList<Plan> plans = (ArrayList<Plan>) response.body();
                Error error = parserError(plans.get(0));
                if (error == null){
                    listener.success(plans);
                }
                else {
                    listener.fail(error);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.fail(new Error(t.getMessage()));
            }
        });
    }

    public void getPlanId(String locale, int code_enum, final ControllResponseListener listener){
        PlanService service = ApiClient.getRetrofit().create(PlanService.class);
        service.getPLanId(locale, code_enum).enqueue(new Callback<Plan>() {
            @Override
            public void onResponse(Response<Plan> response, Retrofit retrofit) {
                Plan plan = (Plan) response.body();
                Error error = parserError(plan);
                if (error == null){
                    listener.success(plan);
                }
                else{
                    listener.fail(error);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.fail(new Error(t.getMessage()));
            }
        });
    }
}
