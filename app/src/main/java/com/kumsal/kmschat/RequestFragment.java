package com.kumsal.kmschat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class RequestFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Users> personValue;
    private RequestFriendFragmentAdapter mAdapter;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mUsers;
    private String UID;
    private FirebaseAuth mAuth;
    private Users mUsers1;
    public RequestFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();
        mFriendDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                personValue.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    String requestType=data.child("request_type").getValue()+"";
                    if (requestType.equals("receive")){
                        mUsers.child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String iUrl=snapshot.child("thumbalimage").getValue().toString();
                                String name=snapshot.child("name").getValue().toString();
                                String status=snapshot.child("status").getValue().toString();
                                mUsers1=new Users(name,status,iUrl,"");
                                personValue.add(mUsers1);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_request,container,false);
        recyclerView=view.findViewById(R.id.fragment_request_recyler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        personValue=new ArrayList<>();
        mAdapter=new RequestFriendFragmentAdapter(getContext(),personValue);
        mAuth=FirebaseAuth.getInstance();
        UID=mAuth.getUid();
        mFriendDatabase= FirebaseDatabase.getInstance().getReference("Friends_req");
        mUsers=FirebaseDatabase.getInstance().getReference("Users");

        return view;
    }
}