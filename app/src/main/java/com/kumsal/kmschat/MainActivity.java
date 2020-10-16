package com.kumsal.kmschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kongzue.dialog.v3.WaitDialog;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private SectionPagerAdapter mPagerAdapter;
    private TabLayout tabLayout1;
    private CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        mToolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("KMSChat");
        WaitDialog.show(this,"Please wait");
        imageView=findViewById(R.id.gfs);
        mViewPager=findViewById(R.id.main_pageview);
        mPagerAdapter=new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        tabLayout1=findViewById(R.id.main_tabs_layout);
        tabLayout1.setupWithViewPager(mViewPager);
        WaitDialog.dismiss();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if (user==null){
           startTostart();
        }else{

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