package com.kumsal.kmschat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.util.view.ProgressView;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FrendsFragment extends Fragment{
    private View mMainView;
    private String mCurrent_user_id;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase;
    private RecyclerView mFriendList;
    private List<FriendModdel> friendModdels;
    private FriendsAdapter adapter;
    private FriendModdel object;
    private HashMap<String,String> values;
    private DatabaseReference mRef;
    private HashMap<String, String> values2;

    public FrendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mFriendsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendModdels.clear();
                for (DataSnapshot get: snapshot.getChildren()){
                    values=( HashMap<String,String>)get.getValue();
                    mRef.child(get.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            values2=( HashMap<String,String>)snapshot.getValue();
                            object=new FriendModdel(values.get("date"),values2.get("name"),values2.get("thumbalimage"),values2.get("online"));
                            friendModdels.add(object);
                            adapter=new FriendsAdapter(friendModdels,getContext());
                            mFriendList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView=inflater.inflate(R.layout.fragment_frends, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        mCurrent_user_id=user.getUid();
        friendModdels=new ArrayList<>();
        mFriendsDatabase= FirebaseDatabase.getInstance().getReference().child("friendList").child(mCurrent_user_id);
        mFriendList=mMainView.findViewById(R.id.friendFragmentsRecycler);
        mFriendList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFriendList.setHasFixedSize(true);

        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        WaitDialog.dismiss();
        return mMainView;
    }

}