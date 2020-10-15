package com.kumsal.kmschat;

import android.content.Context;

import android.view.View;

import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFriendFragmentAdapter extends RecyclerView.Adapter<RequestFriendFragmentAdapter.FriendRequestHolder> {

    private Context context;
    private List<Users> userValue;

    public RequestFriendFragmentAdapter(Context context, List<Users> userValue) {
        this.context = context;
        this.userValue = userValue;
    }

    @NonNull
    @Override
    public FriendRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class FriendRequestHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageView;
        public TextView dispayName,status;
        public FriendRequestHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.request_friend_image);
            dispayName=itemView.findViewById(R.id.request_friend_displayName);
            status=itemView.findViewById(R.id.request_friend_status);
        }
    }
}
