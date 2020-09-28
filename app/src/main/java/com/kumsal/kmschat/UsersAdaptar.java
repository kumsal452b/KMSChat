package com.kumsal.kmschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdaptar extends RecyclerView.Adapter<UsersAdaptar.UserViewHolder> {

    Context context;
    List<Users> userValue;


    public UsersAdaptar(Context context, List<Users> userValue) {
        this.context = context;
        this.userValue = userValue;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_single,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users users=userValue.get(position);
        holder.status.setText(users.getStatus());
        holder.name.setText(users.getName());
        Picasso.get().load(users.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userValue.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageView;
        public TextView name,status;
       public UserViewHolder(@NonNull View itemView) {
           super(itemView);
           imageView=itemView.findViewById(R.id.user_single_imageview);
           name=itemView.findViewById(R.id.user_single_name);
           status=itemView.findViewById(R.id.users_single_status);
       }
   }
}
