package com.example.dhew6.seg2105project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ServiceAvailibilityFragment extends Fragment {

    public ServiceAvailibilityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_availibility, container, false);
    }

    public static ServiceMainFragment newInstance(){
        ServiceMainFragment frag = new ServiceMainFragment();
        return frag;
    }

}