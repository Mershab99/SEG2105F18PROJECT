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

public class WelcomeScreen extends AppCompatActivity {

    DatabaseHelper myDB;

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

    // ArrayList<User> = myDB.displayAllUsers();

}
