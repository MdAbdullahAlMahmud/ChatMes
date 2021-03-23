package com.mkrlabs.chatmes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.mkrlabs.chatmes.R;
import com.mkrlabs.chatmes.databinding.MessageReceiveBinding;
import com.mkrlabs.chatmes.databinding.MessageSendBinding;
import com.mkrlabs.chatmes.model.Message;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter{
    final  int ITEM_SENT=1;
    final  int ITEM_RECEIVE=2;
    private Context context;
    private List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_SENT){
            View view = LayoutInflater.from(context).inflate(R.layout.message_send,parent,false);
            return  new SenderViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.message_receive,parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        Message message = messageList.get(position);
        if (holder.getClass() == SenderViewHolder.class){
            SenderViewHolder senderViewHolder = (SenderViewHolder)holder;
            senderViewHolder.messageSendBinding.senderMessageItem.setText(message.getMessage());

        }else {
            ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder)holder;
            receiverViewHolder.messageReceiveBinding.receiveMessageItem.setText(message.getMessage());
        }



    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSenderId())){
            return ITEM_SENT;
        }else {
            return ITEM_RECEIVE;
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public  class SenderViewHolder extends RecyclerView.ViewHolder {

        MessageSendBinding messageSendBinding;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            messageSendBinding = MessageSendBinding.bind(itemView);
        }
    }
    public  class  ReceiverViewHolder extends  RecyclerView.ViewHolder {
        MessageReceiveBinding messageReceiveBinding;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            messageReceiveBinding= MessageReceiveBinding.bind(itemView);
        }
    }
}
