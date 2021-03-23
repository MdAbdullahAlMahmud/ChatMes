package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.model.Request;
import com.mkrlabs.chatmes.model.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends  RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>{
    private Context context;
    private List<Request> requestList;
    private DatabaseReference reference;

    public FriendRequestAdapter(Context context, List<Request> requestList) {
        this.context = context;
        this.requestList = requestList;
        reference= FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_add_friend_item,parent,false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {

        Request request = requestList.get(position);
        holder.timestamp.setVisibility(View.VISIBLE);
        holder.addButton.setText("Confirm");
        getUserInfo(request.getUid(),holder);


    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    class  FriendRequestViewHolder extends RecyclerView.ViewHolder {
        TextView name,timestamp;
        CircleImageView userImage;
        AppCompatButton addButton,removeButton;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.addFriendUserName);
            userImage= itemView.findViewById(R.id.addFriendUserImage);
            addButton=itemView.findViewById(R.id.addFriendUserAddButton);
            removeButton=itemView.findViewById(R.id.addFriendUserRemoveButton);
            timestamp=itemView.findViewById(R.id.request_timestamp);
        }
    }

    public  void  getUserInfo(String uid, FriendRequestViewHolder holder){
        reference.child("Users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    String name = user.getUserFullName();
                    String imageUrl = user.getImageUrl();
                    Glide.with(context).load(imageUrl).into(holder.userImage);
                    holder.name.setText(name);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
