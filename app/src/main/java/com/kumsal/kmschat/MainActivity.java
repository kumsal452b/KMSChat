package com.kumsal.kmschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jfoenix.controls.JFXButton;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    JFXButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.main_app_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("KMSChat");

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user==null){
            Intent intent=new Intent(this,StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}