package com.kumsal.kmschat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.friendsViewHolder> {
    private List<FriendModdel> moddelList;
    private Context context;
    private  
    @NonNull
    @Override
    public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=View.inflate(context,R.layout.user_single,parent);
        return new friendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendsViewHolder holder, int position) {
        FriendModdel moddel=moddelList.get(position);


    }

    @Override
    public int getItemCount() {
        return moddelList.size();
    }

    class friendsViewHolder extends RecyclerView.ViewHolder{
        TextView date,displayName;
        CircleImageView imageView;
        public friendsViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.users_single_status);
            displayName=itemView.findViewById(R.id.user_single_name);
            imageView=itemView.findViewById(R.id.user_single_imageview);

        }
    }
}
