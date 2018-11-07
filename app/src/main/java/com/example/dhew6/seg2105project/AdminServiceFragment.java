package com.example.dhew6.seg2105project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class AdminServiceFragment extends Fragment {

    Button addServiceButton;
    ListView servicesListView;
    EditText searchServicesET;
    CustomAdapter customAdapter;

    HashMap<String, Service> serviceMap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addServiceButton = getView().findViewById(R.id.addServiceButton);
        servicesListView = getView().findViewById(R.id.servicesListView);
        searchServicesET = getView().findViewById(R.id.searchServicesEditText);

        addServiceButtonClicked();
        assignListviewListener();
        editListView();
        search();

        serviceMap = getFromShreadPrefs();

        if(serviceMap == null){
            serviceMap = new HashMap<>();
        }

        ArrayList<Service> list = new ArrayList<>(serviceMap.values());
        customAdapter = new CustomAdapter(getActivity(), list);
        servicesListView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }

    public boolean validRate(String rate) {

        if (!isNumeric(rate)) {
            return false;
        } else if (rate.contains("-")) {
            return false;
        } else if (isNumeric(rate)) {
            double rateDouble = Double.parseDouble(rate);
            if (rate.contains("."))
                return (BigDecimal.valueOf(rateDouble).scale() <= 2);
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param editMode true = edit mode. false = add.
     */
    public void inflateDialog(final boolean editMode, final int pos) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
        final EditText serviceEditText = alertLayout.findViewById(R.id.serviceNameEditText);
        final EditText rateEditText = alertLayout.findViewById(R.id.rateEditText);
        Button addButton = alertLayout.findViewById(R.id.addButton);

        if(pos != -1) {
            ArrayList<Service> list = new ArrayList<>(serviceMap.values());
            String s = list.get(pos).getName();
            serviceEditText.setText(s);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Add a Service");
        alert.setView(alertLayout);

        alert.setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard();
            }
        });

        final AlertDialog alertDialog = alert.create();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String service = serviceEditText.getText().toString();
                String rate = rateEditText.getText().toString();

                boolean valid = validateAdding(service, rate, serviceEditText, rateEditText, editMode);

                if (valid) {

                    Service s = new Service(service, Double.parseDouble(rate));
                    serviceMap.put(service, s);

                    ArrayList<Service> list = new ArrayList<>(serviceMap.values());
                    customAdapter = new CustomAdapter(getActivity(), list);
                    servicesListView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();

                    updateSharedPrefs(serviceMap);

                }

                alertDialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void addServiceButtonClicked() {
        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDialog(false, -1);
            }
        });
    }

    public boolean validateAdding(String service, String rate, EditText serviceET, EditText rateET, boolean editMode) {

        if (rate.trim().equals("") || service.trim().equals("")) {
            Toast.makeText(getActivity(), "You have left empty field(s)", Toast.LENGTH_LONG).show();
            return false;
        } else if (serviceMap.containsKey(service) && !editMode) {
            Toast.makeText(getActivity(), "This service already exists. You can edit service by long-clicking the item in the list"
                    , Toast.LENGTH_LONG).show();
            serviceET.setText("");
            return false;
        } else if (!validRate(rate)) {
            Toast.makeText(getActivity(), "The rate you entered isn't valid", Toast.LENGTH_LONG).show();
            rateET.setText("");
            return false;
        }

        return true;

    }

    public AdminServiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_service, container, false);
    }

    public static AdminServiceFragment newInstance() {
        AdminServiceFragment frag = new AdminServiceFragment();
        return frag;
    }

    public void search(){
        searchServicesET.addTextChangedListener(new TextWatcher() {
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

    public void editListView(){

        servicesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                inflateDialog(true, position);
                return false;
            }
        });
    }

    public void updateSharedPrefs(HashMap<String, Service> map){

        Gson gson = new Gson();
        String hashMapString = gson.toJson(map);

        SharedPreferences prefs = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
        prefs.edit().putString("hashString", hashMapString).apply();

    }

    public HashMap<String, Service> getFromShreadPrefs(){

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

    public void assignListviewListener() {

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(servicesListView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter recyclerView, int position) {
                                ArrayList<Service> list = new ArrayList<>(serviceMap.values());
                                Service s = list.get(position);
                                customAdapter.remove(position);
                                serviceMap.remove(s.getName());
                                updateSharedPrefs(serviceMap);
                            }
                        }
                );
        servicesListView.setOnTouchListener(touchListener);
        servicesListView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                }
            }
        });
    }

}
