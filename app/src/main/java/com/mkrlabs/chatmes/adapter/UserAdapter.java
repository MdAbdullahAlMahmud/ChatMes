package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.ChatActivity;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.model.User;

import java.text.SimpleDateFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> userList;
    private Context context;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    public UserAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
        reference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
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
        String friendUID = user.getUid();
        String myUID = mAuth.getCurrentUser().getUid();
        String room = myUID+friendUID;
        getLastMessage(room,holder);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public  class UserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName, userLastMessage,lastMessageTime;
        TextView user_item_status;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.userItemImage);
            userName=itemView.findViewById(R.id.userItemName);
            userLastMessage=itemView.findViewById(R.id.userItemLastMessage);
            lastMessageTime=itemView.findViewById(R.id.userItemLastMessageTime);
            user_item_status=itemView.findViewById(R.id.user_item_status);
        }
    }

    public void getLastMessage(String room,UserViewHolder holder){
        reference.child("chats").child(room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("lastMessage").exists()){
                    String lastMessage = snapshot.child("lastMessage").getValue(String.class);
                    long timestamp = snapshot.child("timestamp").getValue(Long.class);
                    holder.userLastMessage.setText(lastMessage);
                    holder.lastMessageTime.setText(getFormattedTime(timestamp));
                }else {
                    holder.userLastMessage.setText("Tap to chat");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v("Tag",error.getMessage());
            }
        });

    }

    public String getFormattedTime(long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");

        String time = simpleDateFormat.format(timestamp);
        return time;
    }
}
