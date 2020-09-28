package com.kumsal.kmschat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View view=Lay
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

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
