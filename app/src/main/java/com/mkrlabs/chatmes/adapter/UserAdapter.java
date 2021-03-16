package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mkrlabs.chatmes.ChatActivity;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> userList;
    private Context context;

    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = userList.get(position);

        holder.userName.setText(user.getUserFullName());
        holder.userLastMessage.setText("last Message");
        holder.lastMessageTime.setText("00.00 AM ");
        if(user.isStatus()){
            holder.user_item_status.setVisibility(View.VISIBLE);
        }else {
            holder.user_item_status.setVisibility(View.GONE);
        }
        Glide.with(context).load(user.getImageUrl())
                .into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("RUID",user.getUid());
                intent.putExtra("NAME",user.getUserFullName());
                intent.putExtra("USER_IMAGE",user.getImageUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public  class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName, userLastMessage,lastMessageTime;
        ImageView user_item_status;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userItemImage);
            userName=itemView.findViewById(R.id.userItemName);
            userLastMessage=itemView.findViewById(R.id.userItemLastMessage);
            lastMessageTime=itemView.findViewById(R.id.userItemLastMessageTime);
            user_item_status=itemView.findViewById(R.id.user_item_status);
        }
    }
}
