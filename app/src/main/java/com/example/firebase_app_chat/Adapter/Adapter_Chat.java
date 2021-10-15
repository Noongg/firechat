package com.example.firebase_app_chat.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Models.chatMessage;
import com.example.firebase_app_chat.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class Adapter_Chat extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<chatMessage> chatMessages;
    private String senderId;
    private String receivedId;
    private Context context;
    public static final int VIEW_TYPE_SENT=1;
    public static final int VIEW_TYPE_RECEIVED=2;

    public Adapter_Chat(List<chatMessage> chatMessages, String senderId, String receivedId, Context context) {
        this.chatMessages = chatMessages;
        this.senderId = senderId;
        this.receivedId = receivedId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_chat_sent,parent,false)
            );
        }else {
            return new ReceivedMessageViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_chat_received,parent,false)
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==VIEW_TYPE_SENT){
            ((SentMessageViewHolder)holder).setdata(chatMessages.get(position));
        }else {
            ((ReceivedMessageViewHolder)holder).setdata(chatMessages.get(position),receivedId);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderId().equals(senderId)){
            return VIEW_TYPE_SENT;
        }else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder{
        TextView txt_sent_message,txt_dateTime_sent;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_sent_message=(TextView) itemView.findViewById(R.id.txt_sent_message);
            txt_dateTime_sent=(TextView) itemView.findViewById(R.id.txt_dateTime_sent);
        }
        public void setdata(chatMessage chatMessage){
            txt_sent_message.setText(chatMessage.getMessage());
            txt_dateTime_sent.setText(chatMessage.getDateTime());
        }
    }
    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        ImageView img_friend_received;
        TextView txt_friend_received,txt_dateTime_received;
        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_friend_received=(TextView) itemView.findViewById(R.id.txt_friend_received);
            txt_dateTime_received=(TextView) itemView.findViewById(R.id.txt_dateTime_received);
            img_friend_received=(ImageView) itemView.findViewById(R.id.img_friend_received);
        }
        public void setdata(chatMessage chatMessage, String receivedId){
            txt_friend_received.setText(chatMessage.getMessage());
            txt_dateTime_received.setText(chatMessage.getDateTime());
            FirebaseFirestore firestore= FirebaseFirestore.getInstance();
            firestore.collection("user").document(receivedId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()){
                                String avt=value.getString("avt");
                                Glide.with(context).load(avt).into(img_friend_received);
                            }
                        }
                    });
        }
    }
    public void release(){
        context=null;
    }
}
