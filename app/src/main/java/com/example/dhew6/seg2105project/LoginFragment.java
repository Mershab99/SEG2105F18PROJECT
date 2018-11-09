package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    DatabaseHelper myDB;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myDB = new DatabaseHelper(getContext());

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
    public void onLoginButtonPress() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //string stored in the username edit text
                String usernameText = usernameEditText.getText().toString();
                //string stored in the password edit text
                String passwordText = passwordEditText.getText().toString();
                //if they are blank. create a toast (pop-up bottom of screen) to say it is blank.
                if (usernameText.equals("") || passwordText.equals("")) {
                    Toast.makeText(getActivity(), "Fill in all required fields", Toast.LENGTH_SHORT).show();
                } else if (usernameText.equals("admin") && passwordText.equals("admin")) {
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                    return;
                } else {
                    int validUser = myDB.validateLogin(usernameText, passwordText);
                    if (validUser == 1) {
                        Toast.makeText(getActivity(), "Welcome", Toast.LENGTH_LONG).show();
                        Intent sendMessage = new Intent(getActivity(), WelcomeScreen.class);
                        sendMessage.putExtra("loginUsernameEditText", usernameText);
                        getActivity().finish();
                        startActivity(sendMessage);
                    } else if (validUser == -1) {
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        Toast.makeText(getActivity(), "User does not exist", Toast.LENGTH_LONG).show();
                    } else if (validUser == 0) {
                        passwordEditText.setText("");
                        Toast.makeText(getActivity(), "Incorrect Password", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * Switches to the create new account fragment
     */
    public void onCreateNowPress() {
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
