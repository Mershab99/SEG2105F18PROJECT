package com.example.dhew6.seg2105project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testing gitignore
        TextView test = findViewById(R.id.testText);
        test.setText("Testing GitIgnore");

    }
}
