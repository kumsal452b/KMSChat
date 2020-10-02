package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView profile_display;
    private TextView status,friendCounts;
    private ImageView imageView;
    private Button senreq;
    private DatabaseReference mRefDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mRefDatabase= FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("ui"));

        status=findViewById(R.id.profile_status);
        friendCounts=findViewById(R.id.profile_totalfriend);
        imageView=findViewById(R.id.profile_imageview);
        profile_display=findViewById(R.id.profile_displayname);

        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String Display_name=snapshot.child("name").getValue().toString();
                    String status1=snapshot.child("status").getValue()+"";
                    String image=snapshot.child("imagesUrl").getValue()+"";
                    status.setText(status1);
                    profile_display.setText(Display_name);
                    Picasso.get().load(image).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}