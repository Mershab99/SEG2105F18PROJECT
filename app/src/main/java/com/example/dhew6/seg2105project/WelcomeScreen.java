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

    //init commit of branch

    DatabaseHelper myDB;
    TextView roleTextView, usernameTextView;
    ArrayList<User> users;
    ListView listview;

    /**
     * inflates the menu for a log out option
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

        //database
        myDB = new DatabaseHelper(this);
        users = myDB.displayAllUsers();

        Intent intent = getIntent();
        String username = intent.getStringExtra("loginUsernameEditText");
        String role = "";

        boolean isAdmin = username.equals("admin");
        roleTextView = findViewById(R.id.roleTextView);
        usernameTextView = findViewById(R.id.usernameTextView);

        //if the user is admin then display all users in a listview
        if (isAdmin) {
            username = "Admin";
            role = "Admin";
            User admin = new Admin("admin", "admin", "admin", "admin");
            listview = findViewById(R.id.listview);
            ArrayList<String> usernames = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                usernames.add(users.get(i).getUsername());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (!isAdmin) {
            //if they aren't an admin then get the user then display the type.
            User user = myDB.getUser(username);
            if (user != null) {
                username = user.getName();
                role = user.getType();
            }
        }

        usernameTextView.setText(username);
        roleTextView.setText(role);

    }


}
