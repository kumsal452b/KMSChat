package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserActıvıty extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mrefDatabase;
    private UsersAdaptar adapter;
    private List<Users> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar=findViewById(R.id.user_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All users");
        recyclerView=findViewById(R.id.user_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mrefDatabase= FirebaseDatabase.getInstance().getReference("Users");
        usersList=new ArrayList<>();

        mrefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String,String> values=new HashMap<>();
                Users user;
                for (DataSnapshot data:snapshot.getChildren()) {
                    values=(HashMap<String,String>)data.getValue();
                    if (TextUtils.isEmpty(values.get("thumbalimage"))){
                        user=new Users(values.get("name"),values.get("status"),"empty");
                    }else{
                        user=new Users(values.get("name"),values.get("status"),values.get("thumbalimage"));
                    }
                    usersList.add(user);
                }
//                adapter.notifyDataSetChanged();
                adapter=new UsersAdaptar(getApplicationContext(),usersList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}