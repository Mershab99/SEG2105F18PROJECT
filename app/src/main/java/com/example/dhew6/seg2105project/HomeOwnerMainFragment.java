package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class HomeOwnerMainFragment extends Fragment {

    HomeOwner ho;
    DatabaseHelper myDB;
    TextView nameHeader, roleHeader;
    Spinner weekdaySpinner, searchTypeSpinner;
    ArrayList<ServiceProvider> spList;
    ListView listView;

    public HomeOwnerMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_owner_main, container, false);
    }

    public static HomeOwnerMainFragment newInstance() {

        HomeOwnerMainFragment frag = new HomeOwnerMainFragment();
        return frag;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDB = new DatabaseHelper(getContext());
        weekdaySpinner = getView().findViewById(R.id.hoWeekDaySpinner);
        searchTypeSpinner = getView().findViewById(R.id.spSearchSpinner);
        listView = getView().findViewById(R.id.hoListView);

        getInfo();

    }

    public void getInfo() {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");

        nameHeader = getView().findViewById(R.id.ownerNameHeader);
        roleHeader = getView().findViewById(R.id.ownerRoleHeader);

        ho = (HomeOwner) myDB.getUser(username);
        String name = ho.getName();
        String role = ho.getType();

        nameHeader.setText(name);
        roleHeader.setText(role);

        ArrayList<User> userList = myDB.displayAllUsers();
        spList = new ArrayList<>();
        System.out.println(userList);

        for(int i = 0; i < userList.size(); i++){

            if (userList.get(i).getType().equals("Service Provider")){
                spList.add((ServiceProvider) userList.get(i));
            }

        }



        populateWeekdaySpinner();
        populateSearchTypeSpinner();

    }

    public void populateWeekdaySpinner(){

        String[] items = new String[]{
                "All Days", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        weekdaySpinner.setAdapter(adapter);

    }

    public void populateSearchTypeSpinner(){

        String[] items = new String[]{
                "Time", "Service Name", "Rating"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        searchTypeSpinner.setAdapter(adapter);

    }


    private ServiceProvider getFromSharedPreferences() {

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedSP = prefs.getString(ho.getUsername(), "noWork");
        if (storedSP.equals("noWork")) {
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<ServiceProvider>() {
        }.getType();
        ServiceProvider serviceProvider = gson.fromJson(storedSP, type);
        return serviceProvider;
    }

}
