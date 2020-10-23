package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.kongzue.dialog.v3.WaitDialog;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static boolean isActive=false;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionPagerAdapter mPagerAdapter;
    private TabLayout tabLayout1;
    private CircleImageView imageView;
    private DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isActive=true;
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        } catch (Exception e) {

        }
        KMSChat nesne=new KMSChat();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        String UI=mAuth.getUid();
        System.out.println("deger "+UI);
        if(!TextUtils.isEmpty(UI)){
            mUserRef= FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
            mUserRef.child("lastSeen").onDisconnect().setValue(ServerValue.TIMESTAMP);
            mUserRef.child("online").onDisconnect().setValue("false");
            mUserRef.keepSynced(true);
        }
        mToolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        WaitDialog.show(this,"Please wait");
        imageView=findViewById(R.id.gfs);
        mViewPager=findViewById(R.id.main_pageview);
        mPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout1=findViewById(R.id.main_tabs_layout);
        tabLayout1.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onStart() {
        System.out.println("mainActivity on start calisti");
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user==null){
           startTostart();
        }else{
            mUserRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive=false;
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (UserActıvıty.isActive==true || SetingActivity.isActive==true){
            System.out.println("Active activity");
        }
        else{
            mAuth=FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser()!=null){
            mUserRef.child("online").setValue("false");

            }
        }
    }

    private void startTostart() {
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_app_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menu_allusers){
            Intent setingIntent=new Intent(MainActivity.this,UserActıvıty.class);
            startActivity(setingIntent);
        }
        if (item.getItemId()==R.id.menu_logout){
            FirebaseUser user=mAuth.getCurrentUser();
            FirebaseAuth.getInstance().signOut();
            startTostart();
        }
        if (item.getItemId()==R.id.menu_setings){
            Intent setingIntent=new Intent(MainActivity.this,SetingActivity.class);
            startActivity(setingIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}