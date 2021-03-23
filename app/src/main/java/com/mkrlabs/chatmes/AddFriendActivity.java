package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.adapter.AddFriendAdapter;
import com.mkrlabs.chatmes.adapter.UserAdapter;
import com.mkrlabs.chatmes.model.User;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    DatabaseReference reference;
    private List<User> userList;
    private FirebaseAuth mAuth;
    private AddFriendAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();
    }

    private void init() {
        reference= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        userList= new ArrayList<>();
        recyclerView=findViewById(R.id.addFriendRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserData();
    }

    private void getUserData() {
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    userList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        if(!mAuth.getCurrentUser().getUid().equals(user.getUid())){
                            userList.add(user);
                        }
                    }
                    adapter = new AddFriendAdapter(AddFriendActivity.this,userList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}