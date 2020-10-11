package com.kumsal.kmschat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FrendsFragment extends Fragment {
    private View mMainView;
    private String mCurrent_user_id;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase;
    private RecyclerView mFriendList;
    public FrendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView=inflater.inflate(R.layout.fragment_frends, container, false);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        mCurrent_user_id=user.getUid();
        mFriendsDatabase= FirebaseDatabase.getInstance().getReference().child("friendList").child(mCurrent_user_id);
        mFriendList=mMainView.findViewById(R.id.friendFragmentsRecycler);

        return inflater.inflate(R.layout.fragment_frends, container, false);
    }
}