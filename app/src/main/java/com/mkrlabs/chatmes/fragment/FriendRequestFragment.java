package com.mkrlabs.chatmes.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.adapter.FriendRequestAdapter;
import com.mkrlabs.chatmes.model.Request;

import java.util.ArrayList;
import java.util.List;


public class FriendRequestFragment extends Fragment {


    DatabaseReference reference;
    RecyclerView recyclerView;
    FriendRequestAdapter adapter;
    private List<Request> requestList;
    private FirebaseAuth mAuth;
    public FriendRequestFragment() {
        reference = FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
        requestList = new ArrayList<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_request, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        recyclerView =view.findViewById(R.id.friendRequestRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.child("Users").child(mAuth.getCurrentUser().getUid()).child("request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    requestList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Request request = dataSnapshot.getValue(Request.class);
                        requestList.add(request);
                    }
                    adapter = new FriendRequestAdapter(getContext(),requestList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(), "No Friend Request Found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}