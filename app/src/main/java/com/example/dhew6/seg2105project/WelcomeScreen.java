package com.example.dhew6.seg2105project;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

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
    private static CustomAdapter customAdapter;

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

        //set constraints
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        set.connect(R.id.listview, ConstraintSet.BOTTOM, R.id.layout, ConstraintSet.BOTTOM, 24);
        set.applyTo(layout);

        switchClicked();
        addServiceButtonClicked();

        //if the user is admin then display all users in a listview
        if (isAdmin) {

            User admin =  Admin.getInstance();
            username = admin.getUsername();
            role = admin.getType();

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
                    titleListView.setText("Available Services");
                    addServiceButton.setVisibility(View.VISIBLE);

                    //change the constraints
                    ConstraintSet set = new ConstraintSet();
                    set.clone(layout);
                    set.connect(R.id.listview, ConstraintSet.BOTTOM, R.id.addServiceButton, ConstraintSet.TOP, 16);
                    set.applyTo(layout);

                    Admin admin = Admin.getInstance();
                    ArrayList<Service> serviceNames = admin.getServiceNames();
                    customAdapter = new CustomAdapter(getApplicationContext(), serviceNames);
                    listview.setAdapter(customAdapter);
                    customAdapter.notifyDataSetChanged();

                }else{
                    //now in users listview
                    listviewSwitch.setText("Users");
                    titleListView.setText("Users in the Database");
                    addServiceButton.setVisibility(View.INVISIBLE);

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(WelcomeScreen.this, android.R.layout.simple_list_item_1, usernames);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    //change the constraints
                    ConstraintSet set = new ConstraintSet();
                    set.clone(layout);
                    set.connect(R.id.listview, ConstraintSet.BOTTOM, R.id.layout, ConstraintSet.BOTTOM, 24);
                    set.applyTo(layout);
                }
            }
        });
    }

    public void addServiceButtonClicked(){
        addServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.custom_dialog_layout, null);
                final EditText serviceEditText = alertLayout.findViewById(R.id.serviceNameEditText);
                final EditText rateEditText = alertLayout.findViewById(R.id.rateEditText);
                Button addButton = alertLayout.findViewById(R.id.addButton);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String service = serviceEditText.getText().toString();
                        String rate = rateEditText.getText().toString();

                        boolean valid = validateAdding(service, rate, serviceEditText, rateEditText);
                        System.out.println(valid);

                        if(valid){

                            Admin admin = Admin.getInstance();
                            Service s = new Service(service, Double.parseDouble(rate));
                            admin.updateService(service, s);
                            admin.addServiceNames(s);

                            ArrayList<Service> serviceNames = admin.getServiceNames();
                            customAdapter = new CustomAdapter(getApplicationContext(), serviceNames);
                            listview.setAdapter(customAdapter);
                            customAdapter.notifyDataSetChanged();

                        }

                    }
                });

                AlertDialog.Builder alert = new AlertDialog.Builder(WelcomeScreen.this);
                alert.setTitle("Add a Service");
                alert.setView(alertLayout);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });
    }

    public boolean validRate(String rate){

        if(!isNumeric(rate)){
            return false;
        }else if(rate.contains("-")){
            return false;
        }else if(isNumeric(rate)){
            double rateDouble = Double.parseDouble(rate);
            if(rate.contains("."))
            return (BigDecimal.valueOf(rateDouble).scale() <= 2);
        }
        return true;
    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public boolean validateAdding(String service, String rate, EditText serviceET, EditText rateET){

        final Admin admin = Admin.getInstance();
        final HashMap<String, Service> map = admin.getServices();

        if(rate.trim().equals("") || service.trim().equals("")) {
            Toast.makeText(WelcomeScreen.this, "You have left empty field(s)", Toast.LENGTH_LONG).show();
            return false;
        }else if(map.containsKey(service)){
            Toast.makeText(WelcomeScreen.this, "This service already exists. You can edit service by clicking the edit button in the list"
                    , Toast.LENGTH_LONG).show();
            serviceET.setText("");
            return false;
        }else if(!validRate(rate)) {
            Toast.makeText(WelcomeScreen.this, "The rate you entered isn't valid", Toast.LENGTH_LONG).show();
            rateET.setText("");
            return false;
        }

        return true;

    }

}
