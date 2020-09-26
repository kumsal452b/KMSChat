package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetingActivity extends AppCompatActivity {


    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;
    private TextView name,status;
    private CircleImageView imageView;
    private Button changestatus;
    private Button changeimage;
    private StorageReference mStorageRef;
    private ProgressDialog mProgresDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mStorageRef= FirebaseStorage.getInstance().getReference("profile_images");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        String curentID=mUser.getUid();
        name=findViewById(R.id.seting_displayname);
        status=findViewById(R.id.seting_hi);
        imageView=findViewById(R.id.setings_circleimage);
        changeimage=findViewById(R.id.seting_changeimage);
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
        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(SetingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions(SetingActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    fileChooser();
                }
            }
        });
        changestatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),StatusAcivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults.length>0){
                if (permissions[0]==Manifest.permission.READ_EXTERNAL_STORAGE){
                    fileChooser();
                }
            }
        }
    }
    public void fileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    Uri imageUri=data.getData();
//                    imageView.setImageURI(imageUri);
                    CropImage.activity(imageUri)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(SetingActivity.this);
                }
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgresDialog=new ProgressDialog(SetingActivity.this);
                mProgresDialog.setTitle("Uploading Image");
                mProgresDialog.setMessage("Please wait while we upload and procces the image");
                mProgresDialog.show();
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
                UUID uniqkey=UUID.randomUUID();

                mStorageRef.child(uniqkey.toString()).putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            mProgresDialog.hide();
                           mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   System.out.println(uri.toString());
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Toast.makeText(SetingActivity.this, e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
                               }
                           });


                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}