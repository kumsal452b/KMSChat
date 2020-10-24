package com.kumsal.kmschat;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Messages_adapter  extends RecyclerView.Adapter<Messages_adapter.message_holder> {


    @NonNull
    @Override
    public message_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull message_holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class message_holder extends RecyclerView.ViewHolder{

        public message_holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
