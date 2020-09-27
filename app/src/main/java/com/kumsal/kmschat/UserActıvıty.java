package com.kumsal.kmschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class UserActıvıty extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar=findViewById(R.id.user_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Deneme");
        recyclerView=findViewById(R.id.user_recycler);
        recyclerView.setHasFixedSize(true);
    }
}