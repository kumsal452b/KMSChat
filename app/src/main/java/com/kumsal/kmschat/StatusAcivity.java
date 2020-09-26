package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusAcivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout inputLayout;
    private Button change;
    private DatabaseReference mrefDatabse;
    private FirebaseUser mCurremtUser;
    private ProgressDialog progressDialog;
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
        mCurremtUser= FirebaseAuth.getInstance().getCurrentUser();
        String userKey=mCurremtUser.getUid();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Saving");
        progressDialog.setMessage("Please Wait, while we saving your status");
        mrefDatabse= FirebaseDatabase.getInstance().getReference("Users").child(userKey);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status=inputLayout.getEditText().getText().toString();
                progressDialog.show();
                mrefDatabse.child("status").setValue(status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toast.makeText(StatusAcivity.this, "There was some error is saving changes", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}