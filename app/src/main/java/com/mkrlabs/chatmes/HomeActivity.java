package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.adapter.UserAdapter;
import com.mkrlabs.chatmes.model.FriendModel;
import com.mkrlabs.chatmes.model.User;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ArrayList<User> userList;
    private ArrayList<FriendModel> friendModelsList;
    private UserAdapter adapter;
    private CircleImageView homeUserProfileImage;
    private FirebaseAuth mAuth;
    private ImageView addFriendButtonCV, homeFriendsIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        getCurrentUserFriendList();
        addFriendButtonCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });
        homeFriendsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FriendActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        recyclerView = findViewById(R.id.homeUserRV);
        homeUserProfileImage = findViewById(R.id.homeUserProfileImage);
        homeFriendsIV = findViewById(R.id.homeFriendsIV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userList = new ArrayList<>();
        friendModelsList = new ArrayList<>();
        addFriendButtonCV = findViewById(R.id.addFriendButtonIV);
        //for new git changes
    }

    // new changes
    private void getCurrentUserFriendList() {
        reference.child("friends").child(mAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            friendModelsList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                FriendModel friendModel = dataSnapshot.getValue(FriendModel.class);
                                friendModelsList.add(friendModel);
                            }
                            findOutFriendFromUserList(friendModelsList);
                        } else {
                            Toast.makeText(HomeActivity.this, "No Friends Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
    // new changes
    private void findOutFriendFromUserList(ArrayList<FriendModel> friendModelsList) {
        reference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        for (FriendModel model : friendModelsList) {
                            if (model.getUid().equals(user.getUid())) {
                                userList.add(user);
                            }
                        }
                    }
                    adapter = new UserAdapter(userList, HomeActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setUserStatus(true);
    }

    public  void  setUserStatus(boolean status){
        Date date = Calendar.getInstance().getTime();
        HashMap<String,Object> statusmap = new HashMap<>();
        statusmap.put("status",status);
        statusmap.put("lastSeenTime",date.getTime());
        reference.child("Users").child(mAuth.getCurrentUser().getUid())
                .updateChildren(statusmap);


    }
    @Override
    protected void onPause() {
        super.onPause();
        setUserStatus(false);
    }
}