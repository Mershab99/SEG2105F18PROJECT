package com.example.dhew6.seg2105project;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ServiceAvailibilityFragment extends Fragment {

    TextView roleHeader, nameHeader, weekdayHeader, emptyTextView;
    ListView weekdayListView;
    Spinner daySpinner;
    Button addButton;
    ServiceProvider sp;
    DatabaseHelper myDB;
    ArrayAdapter adapter;

    int fromHour, fromMinute, toHour, toMinute, currentListIndex;
    String fromTime, toTime;
    TimePickerDialog.OnTimeSetListener fromTimeListener, toTimeListener;
    static ArrayList<String> currentList;
    static HashMap<String, ArrayList<String>> weekdayMap;

    public ServiceAvailibilityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_availibility, container, false);
    }

    public static ServiceAvailibilityFragment newInstance() {

        ServiceAvailibilityFragment frag = new ServiceAvailibilityFragment();
        return frag;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        daySpinner = getView().findViewById(R.id.selectDaySpinner);
        addButton = getView().findViewById(R.id.serviceAvailAddButton);
        weekdayListView = getView().findViewById(R.id.daysOfWeekListView);
        weekdayHeader = getView().findViewById(R.id.weekListviewHeader);
        emptyTextView = getView().findViewById(R.id.emptyWeekTextView);

        myDB = new DatabaseHelper(getContext());
        getInfo();

        if(sp.getTimeMap() == null){
            weekdayMap = new HashMap<>();
            weekdayMap.put("Monday", new ArrayList<String>());
            weekdayMap.put("Tuesday", new ArrayList<String>());
            weekdayMap.put("Wednesday", new ArrayList<String>());
            weekdayMap.put("Thursday", new ArrayList<String>());
            weekdayMap.put("Friday", new ArrayList<String>());
            weekdayMap.put("Saturday", new ArrayList<String>());
            weekdayMap.put("Sunday", new ArrayList<String>());
        }else{
            weekdayMap = sp.getTimeMap();
        }
        currentList = new ArrayList<>();


        currentListIndex = -1;
        fromTime = toTime = "";

        populateSpinner();
        updateWeekday();
        implementDialogs();
        addButtonListener();
        deleteOnLongClick();
        editOnItemClick();

    }


    public void getInfo() {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");

        nameHeader = getView().findViewById(R.id.nameServiceHeader1);
        roleHeader = getView().findViewById(R.id.roleServiceHeader1);

        sp = (ServiceProvider) myDB.getUser(username);
        String name = sp.getName();
        String role = sp.getType();

        //address, phone, desc, licensed, company
        ServiceProvider temp = getFromSharedPreferences();
        if (temp != null) {

            sp.setCompany(temp.getCompany());
            sp.setLicensed(temp.isLicensed());
            sp.setAddress(temp.getAddress());
            sp.setDesc(temp.getDesc());
            sp.setPhone(temp.getPhone());
            sp.setTimeMap(temp.getTimeMap());

        }

        nameHeader.setText(name);
        roleHeader.setText(role);

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

    private void updateSharedPreferences(ServiceProvider s){

        Gson gson = new Gson();
        String arrayString = gson.toJson(s);

        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        prefs.edit().putString(sp.getUsername(), arrayString).apply();

    }

    public void updateWeekday() {

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String value = (String) parent.getItemAtPosition(position);
                weekdayHeader.setText(value);
                currentList = weekdayMap.get(value);
                showEmpty();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void populateSpinner() {

        String[] items = new String[]{
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        daySpinner.setAdapter(adapter);

    }

    private void implementDialogs() {

        fromTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                fromHour = hourOfDay;
                fromMinute = minute;
                fromTime = fromHour + ":" + fromMinute;

                if(fromMinute < 10)
                    fromTime = fromHour + ":0" + fromMinute;
                else
                    fromTime = fromHour + ":" + fromMinute;

            }
        };

        toTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                toHour = hourOfDay;
                toMinute = minute;

                if(toMinute < 10)
                    toTime = toHour + ":0" + toMinute;
                else
                    toTime = toHour + ":" + toMinute;

                int totalToTime = toHour * 60 + toMinute;
                int fromToTime = fromHour * 60 + fromMinute;
                if(totalToTime <= fromToTime){
                    Toast.makeText(getActivity(), "End time is longer or equal than your start time.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String availability = fromTime + " - " + toTime;
                if(currentListIndex == -1){
                    currentList.add(availability);
                }else{
                    currentList.add(currentListIndex, availability);
                }

                appendListToAdapter();
                showEmpty();
                updateMap();

            }
        };
    }

    public void addButtonListener() {

        final Calendar c = Calendar.getInstance();
        currentListIndex = -1;

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int fromHourC = c.get(Calendar.HOUR_OF_DAY);
                int fromMinuteC = c.get(Calendar.MINUTE);
                int toHourC = c.get(Calendar.HOUR_OF_DAY);
                int toMinuteC = c.get(Calendar.MINUTE);
                TimePickerDialog toTimePickerDialog = new TimePickerDialog(getActivity(), toTimeListener, toHourC, toMinuteC, true);
                toTimePickerDialog.show();
                TimePickerDialog fromTimePickerDialog = new TimePickerDialog(getActivity(), fromTimeListener, fromHourC, fromMinuteC, true);
                fromTimePickerDialog.show();

            }
        });

    }

    private void appendListToAdapter() {

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, currentList);
        weekdayListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void showEmpty() {

        if (currentList.isEmpty())
            emptyTextView.setVisibility(View.VISIBLE);
        else
            emptyTextView.setVisibility(View.INVISIBLE);
        appendListToAdapter();

    }

    private void updateMap(){
        String weekday = daySpinner.getSelectedItem().toString();
        weekdayMap.put(weekday, currentList);
        sp.setTimeMap(weekdayMap);
        updateSharedPreferences(sp);
    }

    public void deleteOnLongClick(){

        weekdayListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Delete Time?");
                alert.setMessage("Are you sure you want to delete this time slot?");
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentList.remove(position);
                        updateMap();
                        appendListToAdapter();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                return false;
            }
        });
    }

    public void editOnItemClick(){

        weekdayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Edit Time?");
                alert.setMessage("Are you sure you want to edit this time slot?");
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.setPositiveButton("Yes, Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentListIndex = position;
                        currentList.remove(position);
                        TimePickerDialog toTimePickerDialog = new TimePickerDialog(getActivity(), toTimeListener, toHour, toMinute, true);
                        toTimePickerDialog.show();
                        TimePickerDialog fromTimePickerDialog = new TimePickerDialog(getActivity(), fromTimeListener, fromHour, fromMinute, true);
                        fromTimePickerDialog.show();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

    }

}
