package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.WaitDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private String UI;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DatabaseReference mRefRoot;
    private CircleImageView imageView;
    private TextView textView,onlineText;

    private FirebaseAuth mAuth;
    private String clickUserId;
    private String ownUserId;

    private ImageButton addmessage, sendmessage;
    private TextView message;
    private ValueEventListener eventRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        ownUserId=mAuth.getUid();
        clickUserId=getIntent().getStringExtra("ui");
        message=findViewById(R.id.chat_message);
        addmessage=findViewById(R.id.chat_addmessage);
        sendmessage=findViewById(R.id.char_sendButton);

        setContentView(R.layout.activity_chat);
        UI=getIntent().getStringExtra("ui");
        mRefRoot= FirebaseDatabase.getInstance().getReference();
        toolbar=findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar=getSupportActionBar();
        getSupportActionBar().setTitle("");
        WaitDialog.show(this,"Please Wait");
        imageView=findViewById(R.id.chat_custom_imageview);
        textView=findViewById(R.id.chat_custom_name);
        onlineText=findViewById(R.id.chat_custom_online);
        String name=getIntent().getStringExtra("un");
        String image=getIntent().getStringExtra("iu");
        if (image.equals("") || image.equals(null)){
            image="emty";
        }
        textView.setText(name);
        mRefRoot.child("Users").child(getIntent().getStringExtra("ui")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String online=snapshot.child("online").getValue()+"";
                String lastSeen=snapshot.child("lastSeen").getValue()+"";
                if (online.equals("true")){
                    onlineText.setText("Online");
                }
                else{
                    getTimeAgo timeAgo=new getTimeAgo();
                    Long lastTime=Long.parseLong(lastSeen);
                    String lastSeenTime=getTimeAgo.getTimeAgo2(lastTime,getApplicationContext());
                    onlineText.setText(lastSeenTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       eventRoot=mRefRoot.child("Chat").child(ownUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map chatAddMap=new HashMap();
                chatAddMap.put("seen",false);
                chatAddMap.put("timestap", ServerValue.TIMESTAMP);

                Map chatUserMap=new HashMap();
                chatUserMap.put("Chat/"+ownUserId+"/"+clickUserId,chatAddMap);
                chatUserMap.put("Chat/"+clickUserId+"/"+ownUserId,chatAddMap);

                mRefRoot.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Picasso.get().load(image).into(imageView);
        WaitDialog.dismiss();

        LayoutInflater inflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View ation_bar_view=inflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(ation_bar_view);

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String message2=message.getText().toString();

        if (!TextUtils.isEmpty(message2)){
            String current_user_id="messages/"+ownUserId+"/"+clickUserId;
            String chat_user_id="messages/"+clickUserId+"/"+ownUserId;
            DatabaseReference user_message_pushId=mRefRoot.child("message").child(ownUserId).child(clickUserId).push();

            String push_id=user_message_pushId.getKey();

            Map messagingMap=new HashMap();

            messagingMap.put("message",message2);
            messagingMap.put("seen","false");
            messagingMap.put("type","text");
            messagingMap.put("time",ServerValue.TIMESTAMP);

            Map messageUserMap=new HashMap();

            messageUserMap.put(ownUserId,messagingMap);
            messageUserMap.put(clickUserId,messagingMap);

            mRefRoot.child("messages").updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error!=null){
                        System.out.println(error.getMessage());
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRefRoot.removeEventListener(eventRoot);

    }

}