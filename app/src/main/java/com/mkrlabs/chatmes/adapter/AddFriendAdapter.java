package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.model.User;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.AddFriendViewHolder>{


    private Context context;
    private List<User> userList;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    public AddFriendAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public AddFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_add_friend_item,parent,false);
        return new AddFriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFriendViewHolder holder, int position) {

        User user = userList.get(position);
        holder.name.setText(user.getUserFullName());
        Glide.with(context).load(user.getImageUrl())
                .into(holder.userImage);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_uid = user.getUid();
                Date date = Calendar.getInstance().getTime();
                String key = reference.push().getKey();

                HashMap<String,Object> usermap = new HashMap<>();
                usermap.put("uid",mAuth.getCurrentUser().getUid());
                usermap.put("name",mAuth.getCurrentUser().getDisplayName());
                usermap.put("timestamp",date.getTime());

                reference.child("Users").child(user_uid).child("request").child(key)
                        .updateChildren(usermap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Friend Request Sent", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class  AddFriendViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CircleImageView userImage;
        AppCompatButton addButton,removeButton;

        public AddFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.addFriendUserName);
            userImage= itemView.findViewById(R.id.addFriendUserImage);
            addButton=itemView.findViewById(R.id.addFriendUserAddButton);
            removeButton=itemView.findViewById(R.id.addFriendUserRemoveButton);
        }

    }
}
