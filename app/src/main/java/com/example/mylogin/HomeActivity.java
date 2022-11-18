package com.example.mylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtWelcome = (TextView) findViewById(R.id.txtWelcome);

        txtWelcome.setText("Hello "+getIntent().getStringExtra("Username")+"!");
    }
}