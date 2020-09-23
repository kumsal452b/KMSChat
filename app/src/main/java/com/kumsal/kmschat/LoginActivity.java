package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout email, password;
    private Button btn_login;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.login_page_toolbar);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        btn_login = findViewById(R.id.login_login);
        progressDialog = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memail = email.getEditText().getText().toString();
                String mpassword = password.getEditText().getText().toString();
                if (!TextUtils.isEmpty(memail) || !TextUtils.isEmpty(mpassword)) {
                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Please wait while we chech credintials...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUsers(memail, mpassword);
                }
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loginUsers(String memail, String mpassword) {
        mAuth.signInWithEmailAndPassword(memail, mpassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.hide();
                        Intent main_Intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(main_Intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}