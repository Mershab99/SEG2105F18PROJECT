package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CreateFragment extends Fragment {

    EditText confirmEditText, passwordEditText, emailEditText, nameEditText, usernameEditText;
    Button createAccountButton;
    Spinner userTypeSpinner;
    TextView loginTextView;

    public CreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //retrieving view objects
        confirmEditText = getView().findViewById(R.id.confirmEditText);
        passwordEditText = getView().findViewById(R.id.createPasswordEditText);
        emailEditText = getView().findViewById(R.id.createEmailEditText);
        nameEditText = getView().findViewById(R.id.fullNameEditText);
        usernameEditText = getView().findViewById(R.id.createUsernameEditText);
        createAccountButton = getView().findViewById(R.id.createButton);
        loginTextView = getView().findViewById(R.id.loginScreenTextView);
        userTypeSpinner = getView().findViewById(R.id.userSelectSpinner);

        //all my listeners
        populateSpinner();
        onCreateButtonClick();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    /**
     * populates the spinner with the user types
     */
    public void populateSpinner(){
        String[] items = new String[]{
                "Home Owner", "Service Provider"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        userTypeSpinner.setAdapter(adapter);

    }

    /**
     * runs checks, if passes all checks goes to the welcome page.
     * KEITH: you must do the passing to welcome page. create a new user when you pass into the page in the oncreate.
     * MERSHAB: to check for validity for if email is already there and if the username is already there
     * NOTE: admin account should already be created, with username:admin, password admin.
     */
    public void onCreateButtonClick(){
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //retrieve values from the edittexts.
                String fullname = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmEditText.getText().toString();
                String type = userTypeSpinner.getSelectedItem().toString();

                //if the edittexts are empty
                if(fullname.equals("") || email.equals("") || username.equals("") || password.equals("") || confirmPassword.equals("")){
                    Toast.makeText(getActivity(), "Fill in all required fields", Toast.LENGTH_SHORT).show();
                    //if it is not a valid email
                }else if(!isValidEmail(email)){
                    Toast.makeText(getActivity(), "Enter a valid email", Toast.LENGTH_SHORT).show();
                    emailEditText.setText("");
                    //if the passwords don't match
                }else if(!password.equals(confirmPassword)){
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    passwordEditText.setText("");
                    confirmEditText.setText("");
                }
                else{
                    Intent sendMessage = new Intent(getActivity(), WelcomeScreen.class);
                    Intent sendRole = new Intent(getActivity(), WelcomeScreen.class);
                    sendMessage.putExtra("role", type);
                    sendMessage.putExtra("loginUsernameEditText", fullname);
                    getActivity().finish();
                    startActivity(sendMessage);
                }
                //FOR MERSHAB: CHECK IF USERNAME IS IN DB AND IF EMAIL IS IN DB.
                //FOR KEITH: IF PASSES ALL CHECKS, SWITCH TO MAIN INTENT, WITH ALL STRINGS and CREATE THE RIGHT USER ACCORDING TO THE USERTYPE.
            }
        });
    }

    /**
     *
     * @param target target you want to check if valid
     * @return true if valid, else false.
     */
    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


}
