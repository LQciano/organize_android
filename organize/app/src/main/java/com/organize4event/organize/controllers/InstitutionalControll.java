package com.organize4event.organize.controllers;

import android.content.Context;

import com.organize4event.organize.commons.ApiClient;
import com.organize4event.organize.listeners.ControllResponseListener;
import com.organize4event.organize.models.Contact;
import com.organize4event.organize.models.ContactType;
import com.organize4event.organize.models.Institutional;
import com.organize4event.organize.services.InstitutionalService;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class InstitutionalControll extends Controll {
    public InstitutionalControll(Context context) {
        super(context);
    }

    public void getInstitutional(String locale, final ControllResponseListener listener) {
        InstitutionalService service = ApiClient.getRetrofit().create(InstitutionalService.class);
        service.getInstitutional(locale).enqueue(new Callback<Institutional>() {
            @Override
            public void onResponse(Response<Institutional> response, Retrofit retrofit) {
                Institutional institutional = (Institutional) response.body();
                Error error = parserError(institutional);
                if (error == null) {
                    listener.success(institutional);
                } else {
                    listener.fail(error);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.fail(new Error(t.getMessage()));
            }
        });
    }

    public void getContactType(String locale, final ControllResponseListener listener) {
        InstitutionalService service = ApiClient.getRetrofit().create(InstitutionalService.class);
        service.getContactType(locale).enqueue(new Callback<ArrayList<ContactType>>() {
            @Override
            public void onResponse(Response<ArrayList<ContactType>> response, Retrofit retrofit) {
                ArrayList<ContactType> contactTypes = (ArrayList<ContactType>) response.body();
                Error error = parserError(contactTypes.get(0));
                if (error == null) {
                    listener.success(contactTypes);
                } else {
                    listener.fail(error);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                listener.fail(new Error(t.getMessage()));
            }
        });
    }

    public void getContact(String locale, final ControllResponseListener listener) {
        InstitutionalService service = ApiClient.getRetrofit().create(InstitutionalService.class);
        service.getContact(locale).enqueue(new Callback<ArrayList<Contact>>() {
            @Override
            public void onResponse(Response<ArrayList<Contact>> response, Retrofit retrofit) {
                ArrayList<Contact> contacts = (ArrayList<Contact>) response.body();
                Error error = parserError(contacts.get(0));
                if (error == null) {
                    listener.success(contacts);
                } else {
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
