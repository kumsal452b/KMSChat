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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.StringTokenizer;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


public class SetingActivity extends AppCompatActivity {


    private DatabaseReference mRefDatabase;
    private FirebaseUser mUser;
    private TextView name,status;
    private CircleImageView imageView;
    private Button changestatus;
    private Button changeimage;
    private StorageReference mStorageRef;
    private ProgressDialog mProgresDialog;
    private ProgressDialog getmProgresDialog;
    private FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    private StorageTask<UploadTask.TaskSnapshot> storageTask;
    private ValueEventListener dbListener;
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
        getmProgresDialog=new ProgressDialog(this);
        getmProgresDialog.setTitle("Please Wait");
        getmProgresDialog.setMessage("Please wait while we data is dowload");
        getmProgresDialog.show();
        changeimage.setVisibility(View.INVISIBLE);
        changestatus.setVisibility(View.INVISIBLE);
       dbListener= mRefDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name1=snapshot.child("name").getValue().toString();
                String image1=snapshot.child("imagesUrl").getValue().toString();
                String status1=snapshot.child("status").getValue().toString();
                String thumball1=snapshot.child("thumbalimage").getValue().toString();
                name.setText(name1);
                status.setText(status1);
                if (image1.equals("")){
                    Picasso.get().load("uujj").placeholder(R.drawable.ic_baseline_supervised_user_circle_24).into(imageView);
                }else{
                    Picasso.get().load(image1).into(imageView);
                }
                changeimage.setVisibility(View.VISIBLE);
                changestatus.setVisibility(View.VISIBLE);
                getmProgresDialog.dismiss();

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
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
                byte[] data2 = baos.toByteArray();

                String filename = "profile_image"+ FirebaseAuth.getInstance().getCurrentUser().getUid();
                String thumbnail_fileN="thumbnails"+FirebaseAuth.getInstance().getCurrentUser().getUid();
                final StorageReference filePath = imageStorage.getReference().child("profile_images")
                        .child(filename+".jpeg");
                final StorageReference thumbRef=imageStorage.getReference().child("profile_images").child("thumbs").child(thumbnail_fileN+".jpeg");
                storageTask = filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                mProgresDialog.dismiss();
                                mRefDatabase.child("imagesUrl").setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mProgresDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SetingActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                    }
                });
                thumbRef.putBytes(data2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            thumbRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mRefDatabase.child("thumbalimage").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mProgresDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SetingActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

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