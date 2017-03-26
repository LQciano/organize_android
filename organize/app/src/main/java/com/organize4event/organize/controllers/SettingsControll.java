package com.organize4event.organize.controllers;

import android.content.Context;

import com.organize4event.organize.commons.ApiClient;
import com.organize4event.organize.listeners.ControllResponseListener;
import com.organize4event.organize.models.Setting;
import com.organize4event.organize.models.UserSetting;
import com.organize4event.organize.services.SettingsService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SettingsControll extends Controll {
    public SettingsControll(Context context) {
        super(context);
    }

    public void getSettings(String locale, final ControllResponseListener listener){
        SettingsService service = ApiClient.getRetrofit().create(SettingsService.class);
        service.getSettings(locale).enqueue(new Callback<ArrayList<Setting>>() {
            @Override
            public void onResponse(Response<ArrayList<Setting>> response, Retrofit retrofit) {
                ArrayList<Setting> settings = (ArrayList<Setting>) response.body();
                Error error = parserError(settings.get(0));
                if (error == null){
                    listener.sucess(settings);
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

    public void getUserSettings(int user_id, final ControllResponseListener listener){
        SettingsService service = ApiClient.getRetrofit().create(SettingsService.class);
        service.getUserSettings(user_id).enqueue(new Callback<ArrayList<UserSetting>>() {
            @Override
            public void onResponse(Response<ArrayList<UserSetting>> response, Retrofit retrofit) {
                ArrayList<UserSetting> userSettings = (ArrayList<UserSetting>) response.body();
                Error error = parserError(userSettings.get(0));
                if (error == null){
                    listener.sucess(userSettings);
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

    public void saveUserSettings(UserSetting userSetting, int checking, final ControllResponseListener listener){
        SettingsService service = ApiClient.getRetrofit().create(SettingsService.class);
        service.saveUserSetting(userSetting.getUser().getId(),
                userSetting.getSettings().getId(),
                checking,
                userSetting.getValue()).enqueue(new Callback<UserSetting>() {
            @Override
            public void onResponse(Response<UserSetting> response, Retrofit retrofit) {
                UserSetting userSetting = (UserSetting) response.body();
                Error error = parserError(userSetting);
                if (error == null){
                    listener.sucess(userSetting);
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

    public void checkingUserSettings(UserSetting userSetting, int checking, final ControllResponseListener listener){
        SettingsService service = ApiClient.getRetrofit().create(SettingsService.class);
        service.checkingUserSetting(userSetting.getId(), checking).enqueue(new Callback<UserSetting>() {
            @Override
            public void onResponse(Response<UserSetting> response, Retrofit retrofit) {
                UserSetting userSetting = (UserSetting) response.body();
                Error error = parserError(userSetting);
                if (error == null){
                    listener.sucess(userSetting);
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
