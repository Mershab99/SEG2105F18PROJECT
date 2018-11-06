package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    DatabaseHelper myDB;
    TextView roleTextView, usernameTextView, titleListView;
    ArrayList<User> users;
    ArrayList<String> usernames;
    ListView listview;
    Switch listviewSwitch;
    Button addServiceButton;
    ConstraintLayout layout;
    boolean userType;

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
        listviewSwitch = findViewById(R.id.listviewSwitch);
        addServiceButton = findViewById(R.id.addServiceButton);
        layout = findViewById(R.id.layout);
        titleListView = findViewById(R.id.listviewTitle);

        listviewSwitch.setVisibility(View.INVISIBLE);
        addServiceButton.setVisibility(View.INVISIBLE);

        //true = users. false = services
        userType = true;

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.connect(R.id.listview, ConstraintSet.BOTTOM, R.id.layout, ConstraintSet.BOTTOM, 24);
        set.applyTo(layout);

        switchClicked();

        //if the user is admin then display all users in a listview
        if (isAdmin) {
            username = "Admin";
            role = "Admin";
            User admin =   Admin.getInstance();

            listview = findViewById(R.id.listview);
            listviewSwitch.setVisibility(View.VISIBLE);

            //populate listview with usernames
            usernames = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                usernames.add(users.get(i).getUsername());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usernames);
            listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            //populates listview with services

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

    public void switchClicked(){

        listviewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //now in the services listview
                    listviewSwitch.setText("Services");
                    titleListView.setText("Services Available");
                }else{
                    //now in users listview
                    listviewSwitch.setText("Users");
                    titleListView.setText("Users in the Database");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(WelcomeScreen.this, android.R.layout.simple_list_item_1, usernames);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
