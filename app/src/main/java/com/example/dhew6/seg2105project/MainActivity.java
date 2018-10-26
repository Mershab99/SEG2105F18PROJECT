package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myDB = new DatabaseHelper(this);

        setContentView(R.layout.activity_main);

        //replace the blank fragment with the login fragment
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.replace(R.id.container, loginFragment);
        trans.commit();
        //removes the blank fragment from backstack
        //so when you click back you close the application.
        manager.popBackStack();

    }
}
