package com.example.dhew6.seg2105project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class ServiceProfileFragment extends Fragment {

    TextView nameHeader, roleHeader, addressTextView, descriptionTextView;
    EditText streetAddressEditText, pCodeEditText, cityEditText, phoneEditText, companyEditText;
    Switch licenseSwitch;
    Button enterButton, editDescButton, saveButton;
    DatabaseHelper myDB;
    ServiceProvider sp;
    boolean licensed = false;

    public ServiceProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_profile, container, false);
    }

    public static ServiceProfileFragment newInstance() {
        ServiceProfileFragment frag = new ServiceProfileFragment();
        return frag;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //retrieving views
        addressTextView = getView().findViewById(R.id.serviceAddressTextView);
        streetAddressEditText = getView().findViewById(R.id.serviceStreetEditText);
        pCodeEditText = getView().findViewById(R.id.servicePCodeEditText);
        cityEditText = getView().findViewById(R.id.serviceCityEditText);
        phoneEditText = getView().findViewById(R.id.servicePhoneEditText);
        companyEditText = getView().findViewById(R.id.serviceCompanyEditText);
        licenseSwitch = getView().findViewById(R.id.serviceLicense);
        enterButton = getView().findViewById(R.id.enterAddressButton);
        editDescButton = getView().findViewById(R.id.serviceEditDescButton);
        saveButton = getView().findViewById(R.id.serviceSaveProfileButton);
        descriptionTextView = getView().findViewById(R.id.serviceDescTextView);

        myDB = new DatabaseHelper(getContext());
        getInfo();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        licensed = false;

        enterButtonClick();
        licenseSwitchListener();
        editButtonListener();
        saveButtonListener();

    }

    public void getInfo() {

        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");

        nameHeader = getView().findViewById(R.id.nameServiceHeader);
        roleHeader = getView().findViewById(R.id.roleServiceHeader);

        sp = (ServiceProvider) myDB.getUser(username);
        String name = sp.getName();
        String role = sp.getType();
        String company, address, desc, phone;
        company = address = desc = phone = "";

        //address, phone, desc, licensed, company
        ServiceProvider temp = getFromSharedPreferences();
        if(temp != null){

            sp.setCompany(temp.getCompany());
            sp.setLicensed(temp.isLicensed());
            sp.setAddress(temp.getAddress());
            sp.setDesc(temp.getDesc());
            sp.setPhone(temp.getPhone());

            company = sp.getCompany();
            address = sp.getAddress();
            desc = sp.getDesc();
            phone = sp.getPhone();
            licensed = sp.isLicensed();

            companyEditText.setText(company);
            addressTextView.setText(address);
            descriptionTextView.setText(desc);
            phoneEditText.setText(phone);
            licenseSwitch.setChecked(licensed);
            if(licensed) {
                licenseSwitch.setText("Licensed");
            }

        }
        nameHeader.setText(name);
        roleHeader.setText(role);

    }


    public void enterButtonClick() {

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String streetAddress = streetAddressEditText.getText().toString();
                String postalCode = pCodeEditText.getText().toString();
                String city = cityEditText.getText().toString();

                if (!validateAddress(streetAddress)) {
                    return;
                }
                if (!validatePostalCode(postalCode)) {
                    return;
                }
                if (!validateCity(city)) {
                    return;
                }
                addressTextView.setText(streetAddress + ", " + city + ", " + postalCode);
            }
        });
    }

    public void licenseSwitchListener() {

        licenseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    licensed = true;
                    licenseSwitch.setText("Lisenced");
                } else if (!isChecked) {
                    licensed = false;
                    licenseSwitch.setText("No License");
                }
            }
        });
    }

    public void editButtonListener() {

        editDescButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflateDialog();

            }
        });

    }

    private void inflateDialog() {

        LayoutInflater layoutInflater = getLayoutInflater();
        View alertLayout = layoutInflater.inflate(R.layout.custom_dialog_layout1, null);
        final EditText editDescEditText = alertLayout.findViewById(R.id.editDescEditText);
        final Button saveDescButton = alertLayout.findViewById(R.id.saveDescButton);
        final TextView limitTextView = alertLayout.findViewById(R.id.descLimitTextView);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Edit Description");
        alert.setView(alertLayout);

        alert.setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        String holder = "Tell us about yourself... (300 characters or less)";
        if (holder.equals(descriptionTextView.getText().toString()))
            editDescEditText.setText("");
        else
            editDescEditText.setText(descriptionTextView.getText().toString());

        final AlertDialog alertDialog = alert.create();

        editDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String current = s.toString();
                int len = current.length();
                int awayFromLimit = 300 - len;
                limitTextView.setText("(" + Integer.toString(awayFromLimit) + ")");


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveDescButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = editDescEditText.getText().toString();
                descriptionTextView.setText(description);

                alertDialog.dismiss();

            }

        });

        alertDialog.show();

    }

    public void saveButtonListener(){

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = addressTextView.getText().toString();
                String phone = phoneEditText.getText().toString();
                String desc = descriptionTextView.getText().toString();
                String company = companyEditText.getText().toString();

                if(!validatePhone(phone))
                    return;

                if(!validateCompany(company))
                    return;

                sp.setAddress(address);
                sp.setPhone(phone);
                sp.setDesc(desc);
                sp.setLicensed(licensed);
                sp.setCompany(company);

                updateSharedPreferences(sp);

                Toast.makeText(getActivity(), "Saved your personal information", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private ServiceProvider getFromSharedPreferences(){

        Gson gson = new Gson();
        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        String storedSP = prefs.getString(sp.getUsername(), "noWork");
        if(storedSP.equals("noWork")){
            return null;
        }
        java.lang.reflect.Type type = new TypeToken<ServiceProvider>(){}.getType();
        ServiceProvider serviceProvider = gson.fromJson(storedSP, type);
        return serviceProvider;
    }

    private void updateSharedPreferences(ServiceProvider s){

        Gson gson = new Gson();
        String arrayString = gson.toJson(s);

        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        prefs.edit().putString(sp.getUsername(), arrayString).apply();

    }

    //NOTE: Canadian postal codes can't contain the letters D, F, I, O, Q, or U, and cannot start with W or Z:
    private boolean validatePostalCode(String postal) {

        String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(postal);
        boolean matches = matcher.matches();
        boolean empty = postal.trim().equals("");
        if (empty) {
            Toast.makeText(getActivity(), "Postal Code field is blank.", Toast.LENGTH_SHORT).show();
        }
        if (!matches) {
            Toast.makeText(getActivity(), "Postal Code is invalid. (A1A 1A1)", Toast.LENGTH_SHORT).show();
            pCodeEditText.setText("");
        }
        return matches && !empty;
    }

    private boolean validateCity(String city) {
        //assume that we don't have any numbers in a city name
        String regex = "^[ A-Za-z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(city);
        boolean matches = matcher.matches();
        boolean empty = city.trim().equals("");
        if (empty)
            Toast.makeText(getActivity(), "City field is blank.", Toast.LENGTH_SHORT).show();
        else if (!matches) {
            Toast.makeText(getActivity(), "City must contain alphabets and spaces only.", Toast.LENGTH_SHORT).show();
            cityEditText.setText("");
        }
        return matches && !empty;
    }

    private boolean validateAddress(String address) {

        String regex = "^\\d+\\s{1}\\D+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(address);
        boolean matches = matcher.matches();
        boolean empty = address.trim().equals("");

        if (empty)
            Toast.makeText(getActivity(), "Address field is blank.", Toast.LENGTH_SHORT).show();
        else if (!matches) {
            Toast.makeText(getActivity(), "Invalid Address Field (123 Example Street)", Toast.LENGTH_SHORT).show();
            streetAddressEditText.setText("");
        }

        return matches && !empty;

    }

    //true if valid, false if empty
    private boolean validateCompany(String company) {

        if (company.trim().equals("")) {
            Toast.makeText(getActivity(), "Company name is empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private boolean validatePhone(String phone) {

        String regex = "^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        boolean matches = matcher.matches();
        boolean empty = phone.trim().equals("");
        if (empty)
            Toast.makeText(getActivity(), "Phone field is blank.", Toast.LENGTH_SHORT).show();
        else if (!matches) {
            Toast.makeText(getActivity(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
            phoneEditText.setText("");
        }
        return matches && !empty;
    }
}
