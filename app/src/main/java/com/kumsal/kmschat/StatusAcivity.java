package com.kumsal.kmschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

public class StatusAcivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout inputLayout;
    private Button change;
    private DatabaseReference mrefDatabse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_acivity);
        toolbar=findViewById(R.id.status_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inputLayout=findViewById(R.id.status_input);
        change=findViewById(R.id.status_changes);
        
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status=inputLayout.getEditText().getText().toString();

            }
        });
    }
}