package com.kumsal.kmschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestFriendFragmentAdapter extends RecyclerView.Adapter<RequestFriendFragmentAdapter.FriendRequestHolder> {

    private Context context;
    private List<Users> userValue;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
         void ButtonClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener mListener){
        this.mListener=mListener;
    }
    public RequestFriendFragmentAdapter(Context context, List<Users> userValue) {
        this.context = context;
        this.userValue = userValue;
    }
    @NonNull
    @Override
    public FriendRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.request_friend,parent,false);
        return new FriendRequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestHolder holder, int position) {
        Users person=userValue.get(position);
        holder.status.setText(person.getStatus());
        holder.dispayName.setText(person.getName());
        if (person.getImage().equals("") || person.getImage().equals(null)){
            person.setImage("empty");
        }
        Picasso.get().load(person.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return userValue.size();
    }

    public class FriendRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView imageView;
        public TextView dispayName,status;
        public Button btnAccept;
        public FriendRequestHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.request_friend_image);
            dispayName=itemView.findViewById(R.id.request_friend_displayName);
            status=itemView.findViewById(R.id.request_friend_status);
            btnAccept=itemView.findViewById(R.id.request_friend_accept);
            btnAccept.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    mListener.ButtonClick(position);
                }
            }
        }
    }
}
