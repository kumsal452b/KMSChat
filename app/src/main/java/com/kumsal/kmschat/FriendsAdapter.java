package com.kumsal.kmschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.friendsViewHolder> {
    private List<FriendModdel> moddelList;
    private Context context;

    public FriendsAdapter(List<FriendModdel> moddelList, Context context) {
        this.moddelList = moddelList;
        this.context = context;
    }

    @NonNull
    @Override
    public friendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_single,parent,false);
        return new friendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final friendsViewHolder holder, int position) {
        final FriendModdel moddel=moddelList.get(position);
        holder.displayName.setText(moddel.getmDisplayName());
        holder.date.setText(moddel.getDate());
        Picasso.get().load(moddel.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return moddelList.size();
    }

    class friendsViewHolder extends RecyclerView.ViewHolder{
        public TextView date,displayName;
        public CircleImageView imageView;
        public DatabaseReference mRef;
        public friendsViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.users_single_status);
            displayName=itemView.findViewById(R.id.user_single_name);
            imageView=itemView.findViewById(R.id.user_single_imageview);



        }
    }
}
