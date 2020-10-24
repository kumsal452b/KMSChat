package com.kumsal.kmschat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Messages_adapter  extends RecyclerView.Adapter<Messages_adapter.message_holder> {


    private List<Messages_Model> messages_modelList;

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
        holder.message.setText(persom.getMessage());
    }

    @Override
    public int getItemCount() {
        return messages_modelList.size();
    }

    public class message_holder extends RecyclerView.ViewHolder{

        TextView message;
        CircleImageView imageView;
        public message_holder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.message_text_layout);
            imageView=itemView.findViewById(R.id.message_image_layout);
        }
    }
}
