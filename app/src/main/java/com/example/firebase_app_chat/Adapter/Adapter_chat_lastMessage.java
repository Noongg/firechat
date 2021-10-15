package com.example.firebase_app_chat.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.Models.chatMessage;
import com.example.firebase_app_chat.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class Adapter_chat_lastMessage extends RecyclerView.Adapter<Adapter_chat_lastMessage.lassMessageViewholder>{
    Context context;
    List<chatMessage> chatMessages;
    ConversionListener listener;

    public Adapter_chat_lastMessage(Context context, List<chatMessage> chatMessages,ConversionListener listener) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.listener=listener;
    }

    @NonNull
    @Override
    public lassMessageViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new lassMessageViewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_lastmessage,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull lassMessageViewholder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class lassMessageViewholder extends RecyclerView.ViewHolder{
        ImageView img_friend_lastMessage;
        TextView txt_name_lastMessage,txt_chat_lastMessage;

        public lassMessageViewholder(@NonNull View itemView) {

            super(itemView);
            img_friend_lastMessage=(ImageView)itemView.findViewById(R.id.img_friend_lastMessage);
            txt_name_lastMessage=(TextView) itemView.findViewById(R.id.txt_name_lastMessage);
            txt_chat_lastMessage=(TextView)itemView.findViewById(R.id.txt_chat_lastMessage);

        }
        public void setData(chatMessage chatMessage){
            txt_name_lastMessage.setText(chatMessage.getConversionName());
            txt_chat_lastMessage.setText(chatMessage.getMessage());
            Glide.with(context).load(chatMessage.getConversionImg()).into(img_friend_lastMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDb userDb=new UserDb();
                    userDb.setYourname(chatMessage.getConversionName());
                    userDb.setAvt(chatMessage.getConversionName());
                    userDb.setId(chatMessage.getConversionId());
                    listener.onConversionClick(userDb);
                }
            });
        }
    }
    public interface ConversionListener{
        void onConversionClick(UserDb userDb);
    }
    public void release(){
        context=null;
    }
}
