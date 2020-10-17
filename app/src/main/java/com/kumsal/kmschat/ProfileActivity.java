package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.util.view.ProgressView;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private TextView profile_display;
    private TextView status, friendCounts;
    private ImageView imageView;
    private Button senreq;
    private DatabaseReference mRefDatabase;
    private String current_friends;
    private DatabaseReference mFriendRequest;
    private FirebaseUser user;
    private String clikedUserId;
    private DatabaseReference maddFriendsDatabase,mFriendRequestbeta;
    private ChildEventListener mListenerRquestFriend;
    private ValueEventListener mValueListener;
    private Karar mKarar;
    private DatabaseReference chechFriend;
    private ProgressView progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mKarar=new Karar(false);
        clikedUserId = getIntent().getStringExtra("ui");
        mRefDatabase = FirebaseDatabase.getInstance().getReference("Users").child(getIntent().getStringExtra("ui"));
        mFriendRequest = FirebaseDatabase.getInstance().getReference().child("Friends_req");
        mFriendRequestbeta=FirebaseDatabase.getInstance().getReference().child("Friends_req");
        maddFriendsDatabase=FirebaseDatabase.getInstance().getReference().child("friendList");
        progressView=new ProgressView(this);
        WaitDialog.show(this,"Loading");

        user = FirebaseAuth.getInstance().getCurrentUser();
        chechIsFr(user.getUid(),clikedUserId);
        status = findViewById(R.id.profile_status);
        friendCounts = findViewById(R.id.profile_totalfriend);
        imageView = findViewById(R.id.profile_imageview);
        profile_display = findViewById(R.id.profile_displayname);
        current_friends = "not_friends";
        senreq = findViewById(R.id.profile_sendrequestButton);

         mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String Display_name = snapshot.child("name").getValue().toString();
                String status1 = snapshot.child("status").getValue() + "";
                String image = snapshot.child("imagesUrl").getValue() + "";

                if (image.equals("") || image.equals(null)){
                    image="empty";
                }
                status.setText(status1);
                profile_display.setText(Display_name);
                Picasso.get().load(image).into(imageView);
                mFriendRequest.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(clikedUserId)){
                            String reques_type=snapshot.child(clikedUserId).child("request_type").getValue().toString();
                            if (reques_type.equals("send")){
                                senreq.setBackgroundResource(R.drawable.button_back2);
                                senreq.setText("Cancel Friend Request");
                                current_friends="send";
                            }
                            else if (reques_type.equals("receive")){
                                senreq.setBackgroundResource(R.drawable.button_back3);
                                senreq.setText("Accept Friend Request");
                                current_friends="receive";
                            }
                            else if (reques_type.equals("accept")){
                                senreq.setBackgroundResource(R.drawable.button_back3);
                                senreq.setText("Your Friend");
                                current_friends="accept";
                            }
                            else if (reques_type.equals("denial")){
                                senreq.setBackgroundResource(R.drawable.button_back);
                                senreq.setText("Send Request Friend");
                                current_friends="not_friends";

                            }
                        }
                        WaitDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                mListenerRquestFriend=mFriendRequest.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                    }
//
//                    @Override
//                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                            String checkId=snapshot.getKey();
//                                if (!checkId.equals("") && !checkId.equals(null)){
//                                    if (user.getUid().equals(checkId)){
//                                        senreq.setBackgroundResource(R.drawable.button_back);
//                                        senreq.setText("Send Friend Request");
//                                        current_friends="no_friends";
//                                    }
//                                }
//                    }
//
//                    @Override
//                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (user.getUid().equals(clikedUserId)){
            senreq.setEnabled(false);

        }


        senreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!mKarar.getisCheck()){
                    unregisterForContextMenu(senreq);
                    if (current_friends.equals("not_friends")){
                        sendRequest(user.getUid(),clikedUserId);
                    }
                    else if (current_friends.equals("send")){
                        deleteRequest(user.getUid(),clikedUserId);
                    }
                    else if (current_friends.equals("receive")){
                        addFriends(user.getUid(),clikedUserId);
                    }

                }else{
                    Toast.makeText(ProfileActivity.this, "Each other's friend ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Your Option");
        menu.add(0,v.getId(),0,"Delete Friendship");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("Delete Friendship")){
            chechFriend.child(user.getUid()).child(clikedUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    chechFriend.child(clikedUserId).child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("request_type","denial");
                            mFriendRequestbeta.child(user.getUid()).child(clikedUserId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        HashMap<String,Object> hashMap=new HashMap<>();
                                        hashMap.put("request_type","denial");
                                        mFriendRequestbeta.child(clikedUserId).child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                senreq.setBackgroundResource(R.drawable.button_back);
                                                senreq.setText("Send Request Friend");
                                                current_friends="not_friends";
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }
        return super.onContextItemSelected(item);
    }

    private void chechIsFr(final String userId, final String clikedUserId){
        chechFriend = FirebaseDatabase.getInstance().getReference("friendList");
        mValueListener=chechFriend.child(userId).child(clikedUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(ProfileActivity.this, userId, Toast.LENGTH_SHORT).show();
                    mKarar.setCheck(true);
                    senreq.setBackgroundResource(R.drawable.button_back3);
                    senreq.setText("Your Friend");
                    current_friends="accept";
                    registerForContextMenu(senreq);
                    chechFriend.removeEventListener(mValueListener);
                }else{
                    mKarar.setCheck(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chechFriend.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String checkId=snapshot.getKey();
                if (!checkId.equals("") && !checkId.equals(null)){
                    if (user.getUid().equals(checkId)){
                        Toast.makeText(ProfileActivity.this, userId +" Silinme islemi gerceklesti", Toast.LENGTH_SHORT).show();
                        senreq.setBackgroundResource(R.drawable.button_back);
                        senreq.setText("Send Friend Request");
                        current_friends="no_friends";
                        unregisterForContextMenu(senreq);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendRequest(final String senderUid , final String getterUid){
        mFriendRequest.child(senderUid).child(getterUid).child("request_type").setValue("send")
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mFriendRequest.child(getterUid).child(senderUid).child("request_type").setValue("receive")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileActivity.this, "Succes friend request", Toast.LENGTH_SHORT).show();
                            senreq.setBackgroundResource(R.drawable.button_back2);
                            senreq.setText("Cancel Friend Request");
                            current_friends="send";
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }
    private void deleteRequest(final String senderUid , final String getterUid){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("request_type","denial");
        mFriendRequest.child(senderUid).child(getterUid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("request_type","denial");
                    mFriendRequest.child(getterUid).child(senderUid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileActivity.this, "Success delete", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

       

//        mFriendRequest.child(senderUid).removeValue().addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                mFriendRequest.child(getterUid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ProfileActivity.this, "Succes request deleted", Toast.LENGTH_LONG).show();
//                        senreq.setBackgroundResource(R.drawable.button_back);
//                        senreq.setText("Send Friend Request");
//                        current_friends="no_friends";
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                });
//            }
//        });
    }

    private void addFriends(final String sendFrendId, final String recFriendId){
        final String currentDate= DateFormat.getDateInstance().format(new Date());
        maddFriendsDatabase.child(sendFrendId).child(recFriendId).child("date").setValue(currentDate).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                maddFriendsDatabase.child(recFriendId).child(sendFrendId).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("request_type","accept");
                        mFriendRequestbeta.child(sendFrendId).child(recFriendId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    HashMap<String,Object> hashMap=new HashMap<>();
                                    hashMap.put("request_type","accept");
                                    mFriendRequestbeta.child(recFriendId).child(sendFrendId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            senreq.setBackgroundResource(R.drawable.button_back3);
                                            senreq.setText("Your Friend");
                                            current_friends="accept";
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    }

