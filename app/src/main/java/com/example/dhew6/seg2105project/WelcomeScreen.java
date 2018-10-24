package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        Intent Extra = getIntent();
        Intent Role = getIntent();
        String roleView = Extra.getStringExtra("role");
        String textView = Extra.getStringExtra("loginUsernameEditText");

        TextView RoleInput = (TextView) findViewById(R.id.textView6);
        TextView UserInput = (TextView) findViewById(R.id.textView4);
        RoleInput.setText(roleView);
        UserInput.setText(textView);
    }

    private void launchActivity() {

        Intent intent = new Intent(this, LoginFragment.class);
        startActivity(intent);
    }


}
