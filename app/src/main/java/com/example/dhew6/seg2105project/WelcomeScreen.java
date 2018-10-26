package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView roleTextView, usernameTextView;
    ArrayList<User> users;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(getApplicationContext());
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOutItem:
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        myDB = new DatabaseHelper(this);
        users = myDB.displayAllUsers();

        String formattedType = "";
        Intent intent = getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");
        boolean isAdmin = username.equals("admin");
        roleTextView = findViewById(R.id.roleTextView);
        usernameTextView = findViewById(R.id.usernameTextView);

        if(isAdmin){
            username = "Admin";
            formattedType = "Admin";
        }else if(!isAdmin){

            User user = myDB.getUser(username);
            String type = user.getClass().getName();
            String[] stringArray = type.split("(?=\\p{Upper})");
            formattedType = String.join(" ", stringArray);
        }

        usernameTextView.setText(username);
        roleTextView.setText(formattedType);
    }


}
