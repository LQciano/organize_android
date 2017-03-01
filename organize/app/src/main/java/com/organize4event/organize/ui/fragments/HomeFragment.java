package com.organize4event.organize.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organize4event.organize.R;

import butterknife.ButterKnife;

/**
 * Created by marcelamelo on 25/02/17.
 */

public class HomeFragment extends BaseFragment{
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        context = getActivity();
        getData();

        return view;
    }

    public void getData(){
        //TODO: Wireframe - HOME - Elcio
    }
}
