package com.kumsal.kmschat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages_adapter  extends RecyclerView.Adapter<Messages_adapter.message_holder> {


    private List<Messages_Model> messages_modelList;
    private FirebaseAuth auth;
    public Messages_adapter(List<Messages_Model> messages_modelList) {
        this.messages_modelList = messages_modelList;
    }

    @NonNull
    @Override
    public message_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single,parent,false);
        return new message_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull message_holder holder, int position) {
        Messages_Model persom=messages_modelList.get(position);
        String currentUid=auth.getUid();
        if (persom.getFrom().equals(currentUid)){
            holder.message.setVisibility(View.INVISIBLE);
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.message2.setText(persom.getMessage());
            try {
                Picasso.get().load(persom.getImage()).placeholder(R.drawable.person).into(holder.imageView2);
            } catch (Exception e) {
                Picasso.get().load("emty").placeholder(R.drawable.person).into(holder.imageView2);
            }

        }else{
            holder.message2.setVisibility(View.INVISIBLE);
            holder.imageView2.setVisibility(View.INVISIBLE);
            holder.message.setText(persom.getMessage());
            try {
                Picasso.get().load(persom.getImage()).placeholder(R.drawable.person).into(holder.imageView);
            } catch (Exception e) {
                Picasso.get().load("emty").placeholder(R.drawable.person).into(holder.imageView);
            }
        }

    }

    @Override
    public int getItemCount() {
        return messages_modelList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull message_holder holder) {

        super.onViewDetachedFromWindow(holder);
    }

    public class message_holder extends RecyclerView.ViewHolder{

        TextView message,message2;
        CircleImageView imageView,imageView2;
        public message_holder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message_text_layout);
            imageView=itemView.findViewById(R.id.message_image_layout);
            message2=itemView.findViewById(R.id.message_text_layout2);
            imageView2=itemView.findViewById(R.id.message_image_layout2);
            auth=FirebaseAuth.getInstance();
        }
    }
}
