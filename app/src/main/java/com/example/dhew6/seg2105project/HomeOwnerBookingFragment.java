package com.example.dhew6.seg2105project;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class HomeOwnerBookingFragment extends Fragment {

    ViewPager viewPager;
    TextView nameHeader, roleHeader, phoneTextView, ratingTextView, addressTextView, descTextView, companyTextView, spNameHeader,
    spRoleHeader, licenseTextView, spTimesHeader, spTimesTextView;
    ConstraintLayout bookLayout;
    ListView serviceListView;
    EditText searchEditText;

    HomeOwner ho;
    ServiceProvider sp;
    DatabaseHelper myDB;
    CustomAdapter customAdapter;

    String hoUsername, spUsername, weekday;
    boolean validData;

    public HomeOwnerBookingFragment() {
        // Required empty public constructor
    }

    public static HomeOwnerBookingFragment newInstance() {

        HomeOwnerBookingFragment frag = new HomeOwnerBookingFragment();
        return frag;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_owner_booking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = getActivity().findViewById(R.id.viewPagerHome);
        nameHeader = getView().findViewById(R.id.ownerNameHeader);
        roleHeader = getView().findViewById(R.id.ownerRoleHeader);
        bookLayout = getView().findViewById(R.id.bookLayout);
        phoneTextView = getView().findViewById(R.id.spBookPhoneTV);
        ratingTextView = getView().findViewById(R.id.spBookRatingTV);
        addressTextView = getView().findViewById(R.id.spBookAddressTV);
        descTextView = getView().findViewById(R.id.spBookDescTV);
        companyTextView = getView().findViewById(R.id.spBookCompanyTV);
        spNameHeader = getView().findViewById(R.id.spBookNameHeader);
        spRoleHeader = getView().findViewById(R.id.spBoolRoleHeader);
        licenseTextView = getView().findViewById(R.id.spBookLicenseTV);
        spTimesHeader = getView().findViewById(R.id.spBookTimesHeader);
        spTimesTextView = getView().findViewById(R.id.spBookTimesTV);
        serviceListView = getView().findViewById(R.id.spBookListView);
        searchEditText = getView().findViewById(R.id.spBookSearchEditText);

        validData = false;
        myDB = new DatabaseHelper(getContext());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 1){

                    validData = retrieveData();
                    if(validData) {
                        getInfo();
                        search();
                        listviewClickListener();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }

    private void search(){

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getInfo(){

        ho = (HomeOwner) myDB.getUser(hoUsername);
        String hoName = ho.getName();
        String hoRole = ho.getType();

        nameHeader.setText(hoName);
        roleHeader.setText(hoRole);

        sp = getFromSharedPreferences(spUsername);

        String name = sp.getName();
        String role = sp.getType();
        String company = sp.getCompany();
        String desc = sp.getDesc();
        String address = sp.getAddress();
        String phone = sp.getPhone();
        boolean licensed = sp.isLicensed();
        String rating = sp.getRating()[0] + " - " + sp.getRating()[1];
        String timeHeaderString = "Available Times on " + weekday;
        String times = sp.getTimeMap().get(weekday).toString();


        if(company.equals("") || company == null)
            company = "User hasn't updated his company";
        if(desc.equals("") || desc == null)
            desc = "User hasn't updated his description";
        if(address.equals("") || address == null)
            address = "User hasn't updated his address";
        if(phone.equals("") || phone == null)
            phone = "User hasn't updated his phone number";
        if(times.equals("") || times == null)
            times = "User is not available on " + weekday;

        spNameHeader.setText(name);
        spRoleHeader.setText(role);
        companyTextView.setText(company);
        descTextView.setText(desc);
        addressTextView.setText(address);
        phoneTextView.setText(phone);
        ratingTextView.setText(rating);
        spTimesHeader.setText(timeHeaderString);
        spTimesTextView.setText(times);

        if(licensed){
            licenseTextView.setText("Yes");
        }else if(!licensed){
            licenseTextView.setText("No");
        }

        ArrayList<Service> list = sp.getServices();
        customAdapter = new CustomAdapter(getActivity(), list);
        serviceListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();


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

    public boolean retrieveData(){

        SharedPreferences preferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        hoUsername = preferences.getString("hoUsername", null);
        spUsername = preferences.getString("spUsername", null);
        weekday = preferences.getString("spWeekday", null);
        if(hoUsername == null || spUsername == null || weekday == null){
            return false;
        }

        return true;

    }

    private void listviewClickListener(){

        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<Service> list = sp.getServices();
                String servicename = list.get(position).getName();
                String servicerate = Double.toString(list.get(position).getRate());
                inflateDialog(servicename, servicerate);

            }
        });

    }

    private void inflateDialog(String servicename, String rate){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout_booking, null);
        final EditText startEditText = alertLayout.findViewById(R.id.bookingStartET);
        final EditText endEditText = alertLayout.findViewById(R.id.bookingEndET);
        final TextView availTextView = alertLayout.findViewById(R.id.bookingTimeTV);

        String times = sp.getTimeMap().get(weekday).toString();
        availTextView.setText("Service: " + servicename + " - Rate: $" + rate + "\n Provider is available at these times: " + times);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Book a Service");
        alert.setView(alertLayout);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String startTime = startEditText.getText().toString();
                String endTime = endEditText.getText().toString();
                if(validTime(startTime, endTime)){
                    Toast.makeText(getActivity(), "Your service has been booked. Your service provider will contact you shortly.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    private boolean validTime(String startTime, String endTime){

        String regex = "^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(startTime);
        Matcher matcher1 = pattern.matcher(endTime);
        boolean matches = matcher.matches() && matcher1.matches();
        boolean empty = startTime.trim().equals("") || endTime.trim().equals("");
        boolean within = true;


        if(empty){
            Toast.makeText(getActivity(), "Field(s) are blank.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!matches){
            Toast.makeText(getActivity(), "Invalid time format. Correct Format (HH:MM) with a 24 hour clock", Toast.LENGTH_SHORT).show();
            return false;
        }

        String[] startParamArray = startTime.split(":");
        int startHourParam = Integer.parseInt(startParamArray[0]);
        int startMinuteParam = Integer.parseInt(startParamArray[1]);

        String[] endParamArray = endTime.split(":");
        int endHourParam = Integer.parseInt(endParamArray[0]);
        int endMinuteParam = Integer.parseInt(endParamArray[1]);

        int totalStartParam = startHourParam * 60 + startMinuteParam;
        int totalEndParam = endHourParam * 60 + endMinuteParam;
        int paramElapsedTime = totalEndParam - totalStartParam;

        if(totalEndParam <= totalStartParam){
            Toast.makeText(getActivity(), "End time must be larger later then start time", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < sp.getTimeMap().get(weekday).size(); i++){

            ArrayList<String> times = sp.getTimeMap().get(weekday);
            String[] timeArrayAvail = times.get(i).split(":");

            int startHourAvail = Integer.parseInt(timeArrayAvail[0]);
            int startMinutesAvail = Integer.parseInt(timeArrayAvail[1].substring(0,2));
            int endHourAvail = Integer.parseInt(timeArrayAvail[1].substring(5));
            int endMinutesAvail = Integer.parseInt(timeArrayAvail[2]);
            int totalStartAvail = startHourAvail * 60 + startMinutesAvail;
            int totalEndAvail = endHourAvail * 60 + endMinutesAvail;
            int availElapsed = totalEndAvail - totalStartAvail;

            if(startHourParam <= startHourAvail || startHourAvail >= endHourAvail){
                within = false;
                continue;
            }
            else if(endHourParam >= endHourAvail || endHourParam <= startHourAvail){
                within = false;
                continue;
            }else if(availElapsed < paramElapsedTime){
                within = false;
                continue;
            }
            within = true;
            break;
        }

        if(!within){
            Toast.makeText(getActivity(), "You don't have a time within the availabilities", Toast.LENGTH_SHORT).show();
        }
        return within;

    }

}
