package com.example.dhew6.seg2105project;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
