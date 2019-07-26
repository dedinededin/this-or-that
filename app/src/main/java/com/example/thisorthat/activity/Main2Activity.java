package com.example.thisorthat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thisorthat.R;

public class Main2Activity extends AppCompatActivity {
    String username;
    TextView helloUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        helloUser = findViewById(R.id.helloUser);

        helloUser.setText("Hello " + username);
    }
}
