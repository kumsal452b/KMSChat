package com.kumsal.kmschat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView profile_display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_display=findViewById(R.id.profile_displayname);
        profile_display.setText(getIntent().getStringExtra("ui"));
    }
}