package com.example.dhew6.seg2105project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AdminServiceFragment extends Fragment {

    public AdminServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_service, container, false);
    }

    public static AdminServiceFragment newInstance(){
        AdminServiceFragment frag = new AdminServiceFragment();
        return frag;
    }

}
