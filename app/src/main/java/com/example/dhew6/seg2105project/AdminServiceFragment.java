package com.example.dhew6.seg2105project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import static android.widget.Toast.LENGTH_SHORT;

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

        if (serviceMap == null) {
            serviceMap = new HashMap<>();
        }

        addServiceButtonClicked();
        assignListviewListener();

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

    public void inflateDialog() {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
        final EditText serviceEditText = alertLayout.findViewById(R.id.serviceNameEditText);
        final EditText rateEditText = alertLayout.findViewById(R.id.rateEditText);
        Button addButton = alertLayout.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String service = serviceEditText.getText().toString();
                String rate = rateEditText.getText().toString();

                boolean valid = validateAdding(service, rate, serviceEditText, rateEditText);
                System.out.println(valid);

                if (valid) {

                    Service s = new Service(service, Double.parseDouble(rate));
                    serviceMap.put(service, s);

                    ArrayList<Service> list = new ArrayList<>(serviceMap.values());
                    customAdapter = new CustomAdapter(getActivity(), list);
                    servicesListView.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();

                }

                closeKeyboard();

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Add a Service");
        alert.setView(alertLayout);

        alert.setNegativeButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                closeKeyboard();
            }
        });

        AlertDialog alertDialog = alert.create();
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
                inflateDialog();
            }
        });
    }

    public boolean validateAdding(String service, String rate, EditText serviceET, EditText rateET) {

        if (rate.trim().equals("") || service.trim().equals("")) {
            Toast.makeText(getActivity(), "You have left empty field(s)", Toast.LENGTH_LONG).show();
            return false;
        } else if (serviceMap.containsKey(service)) {
            Toast.makeText(getActivity(), "This service already exists. You can edit service by clicking the edit button in the list"
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

    public void assignListviewListener() {

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(servicesListView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                Toast.makeText(getActivity(), "Dismiss swipe", Toast.LENGTH_LONG).show();
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter recyclerView, int position) {
                                ArrayList<Service> list = new ArrayList<>(serviceMap.values());
                                Service s = list.get(position);
                                customAdapter.remove(position);
                                serviceMap.remove(s.getName());
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
