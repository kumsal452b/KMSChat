package com.kumsal.kmschat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongzue.dialog.v3.WaitDialog;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class RequestFragment extends Fragment implements RequestFriendFragmentAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    public  static List<Users> personValue;
    private static RequestFriendFragmentAdapter mAdapter;
    private static DatabaseReference mFriendDatabase;
    private static DatabaseReference mUsers;
    private static String UID;
    private static FirebaseAuth mAuth;
    private static Users mUsers1;
    private static DatabaseReference maddFriendsDatabase;
    private static DatabaseReference mFriendRequestbeta;
    private static Context context;
    public RequestFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();



        mFriendDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                WaitDialog.show((AppCompatActivity) getContext(),"Please Wait");
//                personValue.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    String requestType=data.child("request_type").getValue()+"";
                    final String userKey=data.getKey();
                    if (requestType.equals("receive")){

                        mUsers.child(data.getKey()).addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String iUrl=snapshot.child("thumbalimage").getValue().toString();
                                String name=snapshot.child("name").getValue().toString();
                                String status=snapshot.child("status").getValue().toString();
                                mUsers1=new Users(name,status,iUrl,userKey);
                                personValue.add(mUsers1);
                                mAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(mAdapter);
                                WaitDialog.dismiss();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                WaitDialog.dismiss();
                            }
                        });
                    }else{
                        WaitDialog.dismiss();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                WaitDialog.dismiss();
            }
        });
        WaitDialog.dismiss(5000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_request,container,false);
        recyclerView=view.findViewById(R.id.fragment_request_recyler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        context=getActivity();
        personValue=new ArrayList<>();

        mAuth=FirebaseAuth.getInstance();
        UID=mAuth.getUid();
        mFriendDatabase= FirebaseDatabase.getInstance().getReference("Friends_req").child(UID);
        mUsers=FirebaseDatabase.getInstance().getReference("Users");
        mAdapter=new RequestFriendFragmentAdapter(getContext(),personValue);
        mAdapter.setOnItemClickListener(new RequestFragment());
        maddFriendsDatabase=FirebaseDatabase.getInstance().getReference("friendList");
        mFriendRequestbeta=FirebaseDatabase.getInstance().getReference("Friends_req");

        return view;
    }

    public static List<Users> denek;
    @Override
    public void ButtonClick(final int position){
        final String currentDate= DateFormat.getDateInstance().format(new Date());
        final Users person=personValue.get(position);
        denek=personValue;
        WaitDialog.show((AppCompatActivity) context,"Please Wait");
        UID=mAuth.getUid();
        maddFriendsDatabase.child(UID).child(person.getUserID()).child("date").setValue(currentDate).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                maddFriendsDatabase.child(person.getUserID()).child(UID).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("request_type","accept");
                        mFriendRequestbeta.child(UID).child(person.getUserID()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    HashMap<String,Object> hashMap=new HashMap<>();
                                    hashMap.put("request_type","accept");
                                    mFriendRequestbeta.child(person.getUserID()).child(UID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            personValue.remove(position);
                                            mAdapter.notifyDataSetChanged();
                                            recyclerView.setAdapter(mAdapter);
                                            Toast.makeText(context, "Succes", Toast.LENGTH_LONG).show();
                                            WaitDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        WaitDialog.dismiss(5000);
    }

    private void changeNotify(int position) {
        denek.remove(position);
        personValue=denek;
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
    }
}