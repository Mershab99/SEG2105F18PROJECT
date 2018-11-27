package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class ServiceMainFragment extends Fragment {

    TextView nameHeader, roleHeader, emptyAvailTextView, emptyMyServiceTextView;
    ListView servicesListView, myServicesListView;
    EditText searchMyServicesET, searchAvailServicesET;
    ServiceProvider sp;
    DatabaseHelper myDB;
    CustomAdapter customAdapterAvail, customAdapterMyServices;

    private ArrayList<Service> myServices;
    private ArrayList<Service> availServices;

    public ServiceMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_main, container, false);
    }

    public static ServiceMainFragment newInstance(){
        ServiceMainFragment frag = new ServiceMainFragment();
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        servicesListView = getView().findViewById(R.id.availServicesListView);
        myServicesListView = getView().findViewById(R.id.myServicesListView);
        emptyAvailTextView = getView().findViewById(R.id.emptyAvailServicesTextView);
        emptyMyServiceTextView = getView().findViewById(R.id.emptyMyServicesTextView);
        searchAvailServicesET = getView().findViewById(R.id.searchAvailEditText);
        searchMyServicesET = getView().findViewById(R.id.searchMyServicesEditText);

        myDB = new DatabaseHelper(getContext());
        getInfo();

        myServices = sp.getServices();
        if(myServices == null){
            myServices = new ArrayList<>();
            updateMyServicesListView();
            showEmptyMyServices();
        }else{
            updateMyServicesListView();
            showEmptyMyServices();
        }

        HashMap<String, Service> availServicesMap = getMapFromSharedPreferences();
        if(availServicesMap == null){
            availServices = new ArrayList<>();
            updateAvailListView();
            showEmptyAvail();
        }else{
            availServices = (ArrayList<Service>) removeDuplicates(availServicesMap);
            updateAvailListView();
            showEmptyAvail();
        }

        transferToMyService();
        searchMyServices();
        searchAvailServices();
        transferToAvailServices();

    }

    public void getInfo() {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");

        nameHeader = getView().findViewById(R.id.nameServiceHeader);
        roleHeader = getView().findViewById(R.id.roleServiceHeader);

        sp = (ServiceProvider) myDB.getUser(username);
        String name = sp.getName();
        String role = sp.getType();

        //address, phone, desc, licensed, company
        ServiceProvider temp = getFromSharedPreferences();
        if (temp != null) {

            sp.setServices(temp.getServices());
            sp.setCompany(temp.getCompany());
            sp.setLicensed(temp.isLicensed());
            sp.setAddress(temp.getAddress());
            sp.setDesc(temp.getDesc());
            sp.setPhone(temp.getPhone());
            sp.setTimeMap(temp.getTimeMap());
            sp.setRating(temp.getRating());

        }

        nameHeader.setText(name);
        roleHeader.setText(role);

    }

    private List<Service> removeDuplicates(HashMap<String, Service> map){

        List<Service> list1 = myServices;
        List<Service> list2 = new ArrayList<>(map.values());

        List<Service> toReturn= new ArrayList<>();

        for(Service item : list2){

            if(!list1.contains(item)){
                toReturn.add(item);
            }

        }

        return toReturn;

    }

    private void searchMyServices(){

        searchMyServicesET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapterMyServices.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void searchAvailServices(){

        searchAvailServicesET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapterAvail.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void updateAvailListView(){

        customAdapterAvail = new CustomAdapter(getActivity(), availServices);
        servicesListView.setAdapter(customAdapterAvail);
        customAdapterAvail.notifyDataSetChanged();

    }

    private void updateMyServicesListView(){

        customAdapterMyServices = new CustomAdapter(getActivity(), myServices);
        myServicesListView.setAdapter(customAdapterMyServices);
        customAdapterMyServices.notifyDataSetChanged();

    }

    private void showEmptyAvail(){

        if(availServices.isEmpty()){
            emptyAvailTextView.setVisibility(View.VISIBLE);
        }else
            emptyAvailTextView.setVisibility(View.INVISIBLE);

    }

    private void showEmptyMyServices(){

        if(myServices.isEmpty()){
            emptyMyServiceTextView.setVisibility(View.VISIBLE);
        }else
            emptyMyServiceTextView.setVisibility(View.INVISIBLE);
    }

    private void transferToMyService(){

        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Service s = availServices.get(position);

                availServices.remove(position);
                updateAvailListView();
                showEmptyAvail();

                myServices.add(s);
                updateMyServicesListView();
                showEmptyMyServices();

                sp.setServices(myServices);
                updateSharedPreferences(sp);
            }
        });

    }

    private void transferToAvailServices(){

        myServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Service s = myServices.get(position);

                myServices.remove(position);
                updateMyServicesListView();
                showEmptyMyServices();

                availServices.add(s);
                updateAvailListView();
                showEmptyAvail();

                sp.setServices(myServices);
                updateSharedPreferences(sp);

            }
        });

    }

    private ServiceProvider getFromSharedPreferences() {

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedSP = prefs.getString(sp.getUsername(), "noWork");
        if (storedSP.equals("noWork")) {
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<ServiceProvider>() {
        }.getType();
        ServiceProvider serviceProvider = gson.fromJson(storedSP, type);
        return serviceProvider;
    }

    public HashMap<String, Service> getMapFromSharedPreferences(){

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedHashMap = prefs.getString("hashString", "noWork");
        if(storedHashMap.equals("noWork")){
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Service>>(){}.getType();
        HashMap<String, Service> map = gson.fromJson(storedHashMap, type);
        return map;

    }

    private void updateSharedPreferences(ServiceProvider s){

        Gson gson = new Gson();
        String arrayString = gson.toJson(s);

        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        prefs.edit().putString(sp.getUsername(), arrayString).apply();

    }

}
