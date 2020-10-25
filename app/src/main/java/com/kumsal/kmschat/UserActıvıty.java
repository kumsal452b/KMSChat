package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActıvıty extends AppCompatActivity implements UsersAdaptar.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mrefDatabase;
    private UsersAdaptar adapter;
    private List<Users> usersList;
    private CircleImageView imageView;
    public static boolean isActive=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isActive=true;
        imageView=findViewById(R.id.user_single_onlineimage);
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
                usersList.clear();
                for (DataSnapshot data:snapshot.getChildren()) {
                    values=(HashMap<String,String>)data.getValue();
                    if (TextUtils.isEmpty(values.get("thumbalimage"))){
                        user=new Users(values.get("name"),values.get("status"),"empty",data.getKey(),values.get("online"));
                    }else{
                        user=new Users(values.get("name"),values.get("status"),values.get("thumbalimage"),data.getKey(),values.get("online"));
                    }
                    usersList.add(user);
                }
//                adapter.notifyDataSetChanged();
                adapter=new UsersAdaptar(getApplicationContext(),usersList);
                recyclerView.setAdapter(adapter);
                adapter.setOnClicklistener(UserActıvıty.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserActıvıty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClikView(int position) {

        Users users=usersList.get(position);
        Intent profile_intent=new Intent(this,ProfileActivity.class);
        profile_intent.putExtra("ui",users.getUserID());
        startActivity(profile_intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive=false;
    }
}