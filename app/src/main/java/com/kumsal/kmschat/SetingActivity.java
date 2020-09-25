package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetingActivity extends AppCompatActivity {


    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;
    private TextView name,status;
    private CircleImageView imageView;
    private Button changestatus;
    private Button changeimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        String curentID=mUser.getUid();
        name=findViewById(R.id.seting_displayname);
        status=findViewById(R.id.seting_hi);
        imageView=findViewById(R.id.setings_circleimage);
        changeimage=findViewById(R.id.seting_statusimage);
        changestatus=findViewById(R.id.seting_statuschanges);

        mRefDatabase= FirebaseDatabase.getInstance().getReference("Users").child(curentID);

        mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1=snapshot.child("name").getValue().toString();
                String image1=snapshot.child("imagesUrl").getValue().toString();
                String status1=snapshot.child("status").getValue().toString();
                String thumball1=snapshot.child("thumbalimage").getValue().toString();
                name.setText(name1);
                status.setText(status1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetingActivity.this, "hata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}