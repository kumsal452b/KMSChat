package com.kumsal.kmschat;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class KMSChat extends Application implements RequestFriendFragmentAdapter.OnItemClickListener {
    private DatabaseReference mUserDb;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate() {

        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder=new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built=builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        RequestFriendFragmentAdapter deneme=null;
        deneme.setOnItemClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        mUserDb=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
        mUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot!=null){
                            mUserDb.child("online").onDisconnect().setValue("false");
                            mUserDb.child("online").setValue("true");
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void ButtonClick(int position) {

    }
}
