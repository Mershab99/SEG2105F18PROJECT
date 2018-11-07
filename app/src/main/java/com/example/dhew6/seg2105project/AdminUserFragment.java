package com.example.dhew6.seg2105project;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminUserFragment extends Fragment {

    DatabaseHelper myDB;
    ListView listview;
    ArrayList<User> users;
    EditText searchEditText;
    ArrayAdapter<String> adapter;

    public AdminUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listview = getView().findViewById(R.id.listview);
        searchEditText = getView().findViewById(R.id.searchEditText);

        //gets all the users in the database
        myDB = new DatabaseHelper(getActivity());
        users = myDB.displayAllUsers();

        //displays the relevant information in the listview
        ArrayList<String> nameRoles = new ArrayList<>();
        for(int i = 0; i < users.size(); i++){
            String email = users.get(i).getEmail();
            String username = users.get(i).getUsername();
            String role = users.get(i).getType();
            String combined = username + " - " + email + " - " + role;
            nameRoles.add(combined);
        }

        //updates the listview
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, nameRoles);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //listener
        search();

    }

    public void search(){
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user, container, false);
    }

    public static AdminUserFragment newInstance(){
        AdminUserFragment frag = new AdminUserFragment();
        return frag;
    }

}
