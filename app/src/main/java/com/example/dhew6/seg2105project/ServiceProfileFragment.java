package com.example.dhew6.seg2105project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ServiceProfileFragment extends Fragment {

    TextView nameHeader, roleHeader;
    DatabaseHelper myDB;

    public ServiceProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_profile, container, false);
    }

    public static ServiceProfileFragment newInstance(){
        ServiceProfileFragment frag = new ServiceProfileFragment();
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDB = new DatabaseHelper(getContext());

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");

        nameHeader = getView().findViewById(R.id.nameServiceHeader);
        roleHeader = getView().findViewById(R.id.roleServiceHeader);

        ServiceProvider sp = (ServiceProvider) myDB.getUser(username);
        String name = sp.getName();
        String role = sp.getType();

        nameHeader.setText(name);
        roleHeader.setText(role);


    }
}
