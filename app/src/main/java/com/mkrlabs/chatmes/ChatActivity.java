package com.mkrlabs.chatmes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mkrlabs.chatmes.adapter.MessageAdapter;
import com.mkrlabs.chatmes.databinding.ActivityChatBinding;
import com.mkrlabs.chatmes.model.Message;
import com.mkrlabs.chatmes.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String receiverUid;
    String receiverName,receiverImage;
    static DatabaseReference reference;
    static FirebaseAuth mAuth;
    private String senderUid;

    private String senderRoom,receiverRoom;
    private List<Message> messages;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        messages= new ArrayList<>();
        init();

        if (getIntent()!=null){
            receiverUid=getIntent().getStringExtra("RUID");
            receiverName=getIntent().getStringExtra("NAME");
            receiverImage=getIntent().getStringExtra("USER_IMAGE");
            setUserInfo();
        }

        binding.chatBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.this.onBackPressed();
            }
        });

        binding.messageSendFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textMessageSent();
            }
        });
        checkMessageTypingStatus();
        adapter = new MessageAdapter(ChatActivity.this,messages,receiverImage);
        binding.chatRV.setAdapter(adapter);
    }

    private void checkMessageTypingStatus() {
        binding.messageEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    Log.v("Typing","Not Typing");
                   updateTypingStatus(false);
                }else {
                    Log.v("Typing","Typing");
                    updateTypingStatus(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        reference.child("chats").child(receiverRoom).child("isTyping").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    boolean typingStatus = (boolean)snapshot.getValue();
                    if(typingStatus){
                        binding.typingAnimationView.setVisibility(View.VISIBLE);
                    }else {
                        binding.typingAnimationView.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(ChatActivity.this, "No Child", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateTypingStatus(boolean isTyping) {
        HashMap<String,Object> typingMap = new HashMap<>();
        typingMap.put("isTyping",isTyping);
        reference.child("chats").child(senderRoom).updateChildren(typingMap);
    }

    private void init() {
        reference= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();

        binding.chatRV.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRV.setHasFixedSize(true);


    }

    private void setUserInfo() {
        senderUid = mAuth.getCurrentUser().getUid();
        binding.chatUsername.setText(receiverName);
        senderRoom = senderUid+receiverUid;
        receiverRoom=receiverUid+senderUid;
        Glide.with(this).load(receiverImage).into(binding.chatUserImage);
    }






    @Override
    protected void onStart() {
        super.onStart();
        setUserStatus(true);
        changeActiveStatusTime();
        fetchAllMessage();
    }

    private void fetchAllMessage() {

        reference.child("chats").child(senderRoom).child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        if(snapshot.exists()){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Message message = dataSnapshot.getValue(Message.class);
                                messages.add(message);
                            }
                            adapter.notifyDataSetChanged();
                            binding.chatRV.smoothScrollToPosition(binding.chatRV.getAdapter().getItemCount());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void changeActiveStatusTime() {
        reference.child("Users").child(receiverUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot!=null){
                    User user = snapshot.getValue(User.class);
                    if (user.isStatus()){
                        binding.chatLastSeen.setText("Active");
                    }else {
                        binding.chatLastSeen.setText(LastTimeAgo.getTimeAgo(user.getLastSeenTime(),ChatActivity.this));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    private void  textMessageSent(){
        Date date = Calendar.getInstance().getTime();
         String usermessage=null;
         usermessage=binding.messageEdt.getText().toString();
         String pushKey = reference.push().getKey();
         if(!usermessage.isEmpty()){
             binding.messageEdt.setText("");
             //    public Message(String messageId, String message, String senderId, String type, long timestamp) {
             Message message = new Message(pushKey,usermessage,senderUid,"text",date.getTime());

             reference.child("chats").child(senderRoom).child("messages")
                     .child(pushKey)
                     .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {
                     reference.child("chats").child(receiverRoom).child("messages")
                             .child(pushKey)
                             .setValue(message);
                 }
             });

             HashMap<String,Object> lastMsgMap = new HashMap<>();
             lastMsgMap.put("lastMessage",usermessage);
             lastMsgMap.put("timestamp",date.getTime());

             reference.child("chats").child(senderRoom).updateChildren(lastMsgMap);
             reference.child("chats").child(receiverRoom).updateChildren(lastMsgMap);

         }else {
             binding.messageEdt.setError("required");
         }

    }
}