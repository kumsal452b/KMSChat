package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private String current_friends;
    private DatabaseReference mFriendRequest;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mRefDatabase= FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("ui"));
        mFriendRequest=FirebaseDatabase.getInstance().getReference().child("Friends_req");
        user= FirebaseAuth.getInstance().getCurrentUser();
        status=findViewById(R.id.profile_status);
        friendCounts=findViewById(R.id.profile_totalfriend);
        imageView=findViewById(R.id.profile_imageview);
        profile_display=findViewById(R.id.profile_displayname);
        current_friends="not_friends";
        senreq=findViewById(R.id.profile_sendrequestButton);
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

        senreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senreq.setEnabled(false);
                if (user.getUid().equals(getIntent().getStringExtra("ui"))){
                    Toast.makeText(ProfileActivity.this,
                            "you shouldn't send yourself a friend request", Toast.LENGTH_LONG).show();
                    return;
                }
                if (current_friends.equals("not_friends")){

                    mFriendRequest.child(user.getUid()).child(getIntent().getStringExtra("ui")).child("request_type").setValue("send")
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequest.child(getIntent().getStringExtra("ui")).child(user.getUid()).child("request_type").setValue("received");
                            senreq.setEnabled(true);
                            current_friends="req_send";
                            senreq.setText("Cancel Request");
                            senreq.setBackgroundColor(Color.GREEN);
                            Toast.makeText(ProfileActivity.this, "Succes", Toast.LENGTH_LONG).show();
                        }
                    });


                }
                if (current_friends.equals("req_send")){
                    mFriendRequest.child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequest.child(getIntent().getStringExtra("ui")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    senreq.setBackgroundColor(R.drawable.button_back);
                                    current_friends="not_friends";
                                    Toast.makeText(ProfileActivity.this, "Request deleting succes", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this,"Received "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this,"Send "+ e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    
                }
            }
        });
    }
}