package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplay,mEmail,password;
    private Button regBtn;
    private FirebaseAuth mAuth;
    private Toolbar  toolbar;
    private ActionBarDrawerToggle togle;
    private ProgressDialog mProgresDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDisplay=findViewById(R.id.reg_display);
        mEmail=findViewById(R.id.reg_email);
        password=findViewById(R.id.reg_password);
        regBtn=findViewById(R.id.reg_create_account);
        toolbar=findViewById(R.id.register_toolbar);
        mProgresDialog=new ProgressDialog(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth=FirebaseAuth.getInstance();
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display=mDisplay.getEditText().getText().toString();
                String email=mEmail.getEditText().getText().toString();
                String password1=password.getEditText().getText().toString();
                if (!TextUtils.isEmpty(display) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password1)){
                    mProgresDialog.setTitle("Registering User");
                    mProgresDialog.setMessage("Please wait while we create your account");
                    mProgresDialog.setCanceledOnTouchOutside(false);
                    mProgresDialog.show();
                    register_user(display,email,password1);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    private void register_user(String display, String email, String password1) {

        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                mProgresDialog.dismiss();
                                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(myIntent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgresDialog.hide();
                                Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });




    }
}