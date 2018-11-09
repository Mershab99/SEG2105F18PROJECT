package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView roleTextView, usernameTextView;

    /**
     * inflates the menu for a log out option
     *
     * @param menu
     * @return
     */
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
                //if you select the log out option in the menu then you should go back to log in screen
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

        myDB = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");
        String role = "";
        String name = "";
        roleTextView = findViewById(R.id.roleTextView);
        usernameTextView = findViewById(R.id.usernameTextView);

        User user = myDB.getUser(username);
        if (user != null) {
            name = user.getName();
            role = user.getType();
        }

        usernameTextView.setText(name);
        roleTextView.setText(role);

    }


}
