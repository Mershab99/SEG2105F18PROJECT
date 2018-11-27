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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


public class HomeOwnerMainFragment extends Fragment {

    HomeOwner ho;
    DatabaseHelper myDB;
    TextView nameHeader, roleHeader, contentsHeader;
    Spinner weekdaySpinner, searchTypeSpinner;
    ArrayList<ServiceProvider> spList;
    ListView listView, contentsListView;
    final int SEARCH_NAME = 0;
    final int SEARCH_TIME = 1;
    final int SEARCH_RATING = 2;
    int searchType;
    CustomAdapter customAdapter;
    ArrayAdapter arrayAdapter, contentsArrayAdapter;


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
        contentsHeader = getView().findViewById(R.id.hoContentsTextView);
        contentsListView = getView().findViewById(R.id.contentHOTextView);

        getInfo();

        searchType = SEARCH_NAME;

        searchSpinnerListener();

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

        for (int i = 0; i < userList.size(); i++) {

            if (userList.get(i).getType().equals("Service Provider")) {
                String user = userList.get(i).getUsername();
                ServiceProvider sp = getFromSharedPreferences(user);
                spList.add(sp);
                System.out.println(spList);
            }

        }

        populateWeekdaySpinner();
        populateSearchTypeSpinner();
        weekdaySpinnerListener();
        updateListView();
        mainLVListener();

    }

    public void populateWeekdaySpinner() {

        String[] items = new String[]{
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        weekdaySpinner.setAdapter(arrayAdapter);

    }

    public void populateSearchTypeSpinner() {

        String[] items = new String[]{
                "Service Name", "Time", "Rating"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        searchTypeSpinner.setAdapter(arrayAdapter);

    }

    private void updateListView() {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < spList.size(); i++) {
            list.add(spList.get(i).getName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }

    private void updateContents() {

        if (searchType == SEARCH_NAME) {

            contentsHeader.setText("Providers who offer this Service.");

            HashMap<String, Service> map = getMapFromSharedPreferences();

            if (map == null) {
                Toast.makeText(getActivity(), "No Services are Available Right Now", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<Service> services = new ArrayList<>(map.values());
                customAdapter = new CustomAdapter(getActivity(), services);
                listView.setAdapter(customAdapter);
                customAdapter.notifyDataSetChanged();
            }

        }

        if (searchType == SEARCH_TIME) {

            String weekday = weekdaySpinner.getSelectedItem().toString();
            contentsHeader.setText("Providers available during this time on " + weekday);
            ArrayList<String> list = findTimes(weekday);
            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();

        }

        if (searchType == SEARCH_RATING) {

            contentsHeader.setText("Provider with specified rating.");
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < spList.size(); i++) {
                String[] rating = spList.get(i).getRating();
                if (!list.contains(rating[0])) {
                    list.add(rating[0]);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getActivity(), "None of the providers have ratings.", Toast.LENGTH_SHORT).show();
            }
            arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        }

    }

    private void mainLVListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<String> listToTransfer = new ArrayList<>();

                if (searchType == SEARCH_NAME) {

                    Service s = customAdapter.getItem(position);
                    for (int i = 0; i < spList.size(); i++) {
                        ServiceProvider sp = spList.get(i);
                        ArrayList<Service> services = sp.getServices();
                        if (services == null) {
                            continue;
                        } else if (services.contains(s)) {
                            listToTransfer.add(sp.getName() + " - " + sp.getCompany() + " - " + sp.getPhone());
                        }
                    }

                    contentsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listToTransfer);
                    contentsListView.setAdapter(contentsArrayAdapter);
                    contentsArrayAdapter.notifyDataSetChanged();

                }

                if (searchType == SEARCH_TIME) {


                    String s = arrayAdapter.getItem(position).toString();
                    String weekday = weekdaySpinner.getSelectedItem().toString();
                    ArrayList<String> list = new ArrayList<>();

                    for (int i = 0; i < spList.size(); i++) {
                        ServiceProvider sp = spList.get(i);
                        ArrayList<String> times = sp.getTimeMap().get(weekday);
                        if(times.contains(s)){
                            list.add(sp.getName() + " - " + sp.getCompany() + " - " + sp.getPhone());
                        }
                    }
                    contentsArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
                    contentsListView.setAdapter(contentsArrayAdapter);
                    contentsArrayAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private ArrayList<String> findTimes(String key) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < spList.size(); i++) {
            if (spList.get(i).getTimeMap() == null) {
                continue;
            } else {
                ArrayList<String> timesList = spList.get(i).getTimeMap().get(key);
                for (int j = 0; j < timesList.size(); j++) {
                    if (!list.contains(timesList.get(j))) {
                        list.add(timesList.get(j));
                    }
                }
            }
        }
        return list;
    }


    private void searchSpinnerListener() {

        searchTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                searchType = position;
                updateContents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void weekdaySpinnerListener() {

        weekdaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (searchType == SEARCH_TIME) {
                    updateContents();
                } else {
                    return;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private ServiceProvider getFromSharedPreferences(String username) {

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedSP = prefs.getString(username, "noWork");
        if (storedSP.equals("noWork")) {
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<ServiceProvider>() {
        }.getType();
        ServiceProvider serviceProvider = gson.fromJson(storedSP, type);
        return serviceProvider;
    }

    public HashMap<String, Service> getMapFromSharedPreferences() {

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedHashMap = prefs.getString("hashString", "noWork");
        if (storedHashMap.equals("noWork")) {
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Service>>() {
        }.getType();
        HashMap<String, Service> map = gson.fromJson(storedHashMap, type);
        return map;

    }

}
