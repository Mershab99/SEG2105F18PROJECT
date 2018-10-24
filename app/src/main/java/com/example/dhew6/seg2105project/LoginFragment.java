package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    EditText usernameEditText, passwordEditText;
    TextView createNowTextView;
    Button loginButton;
    public final static String EXTRA_MESSAGE="com.example.myHelloAndroid.MESSAGE";

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //retrieving all the views
        usernameEditText = getView().findViewById(R.id.loginUsernameEditText);
        passwordEditText = getView().findViewById(R.id.loginPasswordEditText);
        createNowTextView = getView().findViewById(R.id.createNowTextView);
        loginButton = getView().findViewById(R.id.loginButton);

        //listeners
        onLoginButtonPress();
        onCreateNowPress();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * when you login. YOU MUST:
     * Keith: switch intent to a different activity, store user information.
     * Mershab: retrieve all user data using the username as a key including what type of user.
     * Mershab: check if the user is in the database, and check if the user has the same password as in the database.
     */
    public void onLoginButtonPress(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //string stored in the username edit text
                String usernameText = usernameEditText.getText().toString();
                //string stored in the password edit text
                String passwordText = passwordEditText.getText().toString();
                //if they are blank. create a toast (pop-up bottom of screen) to say it is blank.
                if(usernameText.equals("") || passwordText.equals("")){
                    Toast.makeText(getActivity(), "Fill in all required fields", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent sendMessage = new Intent(getActivity(), WelcomeScreen.class);
                    sendMessage.putExtra("loginUsernameEditText", usernameText);
                    startActivity(sendMessage);
                }


                //FOR MERSHAB: check if user is in database, if not display the toast.
                //FOR MERSHAB: check if user has the correct password in the database, if doesn't match display toast.
                //FOR KEITH: if it passes all checkts go to the welcom page, with the user info from the firebase

            }
        });
    }

    /**
     * Switches to the create new account fragment
     */
    public void onCreateNowPress(){
        createNowTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateFragment createFragment = new CreateFragment();
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.container, createFragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

    }





}
