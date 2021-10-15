package com.example.firebase_app_chat.Main_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Adapter.Adapter_Chat;
import com.example.firebase_app_chat.Models.chatMessage;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.BaseActivity;
import com.example.firebase_app_chat.User_Setting.Profile_person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Chat_Activity extends BaseActivity {
    ImageView img_back_chat,img_friend_chat,img_sent_chat;
    TextView txt_nameFriend_chat,txt_chat_Availability;
    FirebaseFirestore firestore;
    String uid_friend;
    List<chatMessage> chatMessages;
    Adapter_Chat adapter_chat;
    RecyclerView recylerview_sent_chat;
    EditText edt_sent_chat;
    String conversionId=null;
    boolean isReceiverAvailable=false;

    String name_person,avt_person,name_friend,avt_friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        firestore=FirebaseFirestore.getInstance();
        img_back_chat=(ImageView) findViewById(R.id.img_back_chat);
        img_friend_chat=(ImageView)findViewById(R.id.img_friend_chat);
        txt_nameFriend_chat=(TextView)findViewById(R.id.txt_nameFriend_chat);
        txt_chat_Availability=(TextView) findViewById(R.id.txt_chat_Availability);
        recylerview_sent_chat=(RecyclerView) findViewById(R.id.recylerview_sent_chat);
        edt_sent_chat=(EditText)findViewById(R.id.edt_sent_chat);
        img_sent_chat=(ImageView) findViewById(R.id.img_sent_chat);

        uid_friend= getIntent().getStringExtra("uid_person");
        chatMessages=new ArrayList<>();
        adapter_chat=new Adapter_Chat(chatMessages,Main_FireChat.uid,uid_friend,getApplicationContext());
        recylerview_sent_chat.setAdapter(adapter_chat);
        adapter_chat.setOnItemClick(new Adapter_Chat.OnItemClickListenner() {
            @Override
            public void onItemClick(String uid_friend) {
                String uid_person=uid_friend;
                Intent intent=new Intent(Chat_Activity.this, Profile_person.class);
                intent.putExtra("uid_person",uid_person);
                startActivity(intent);
            }
        });

        img_back_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Chat_Activity.this,Main_FireChat.class);
                startActivity(intent);
            }
        });
        loadHeader();
        img_sent_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        listMessage();
    }
    private void listMessage(){
        firestore.collection("chat")
                .whereEqualTo("uid_person",Main_FireChat.uid)
                .whereEqualTo("uid_friend",uid_friend)
                .addSnapshotListener(snapshotEventListener);
        firestore.collection("chat")
                .whereEqualTo("uid_person",uid_friend)
                .whereEqualTo("uid_friend",Main_FireChat.uid)
                .addSnapshotListener(snapshotEventListener);

    }

    private void sendMessage() {
        HashMap<String,Object> mesage=new HashMap<>();
        mesage.put("uid_person",Main_FireChat.uid);
        mesage.put("uid_friend",uid_friend);
        mesage.put("person_sent",edt_sent_chat.getText().toString().trim());
        mesage.put("time",new Date());
        firestore.collection("chat").add(mesage);
        if (conversionId!=null){
            updateconversations(edt_sent_chat.getText().toString().trim());
            firestore.collection("user").document(Main_FireChat.uid)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()){
                                name_person=value.getString("yourname");
                                avt_person=value.getString("avt");
                                HashMap<String,Object> conversion1=new HashMap<>();
                                conversion1.put("avt_person",avt_person);
                                conversion1.put("name_person",name_person);
                                firestore.collection("conversations")
                                        .document(conversionId)
                                        .update(conversion1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });

                            }
                        }
                    });
            firestore.collection("user").document(uid_friend)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()){
                                name_friend=value.getString("yourname");
                                avt_friend=value.getString("avt");
                                HashMap<String,Object> conversion2=new HashMap<>();
                                conversion2.put("avt_friend",avt_friend);
                                conversion2.put("name_friend",name_friend);
                                firestore.collection("conversations")
                                        .document(conversionId)
                                        .update(conversion2)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        }
                    });
        }else {

            HashMap<String,Object> conversion=new HashMap<>();
            conversion.put("uid_person",Main_FireChat.uid);

            conversion.put("uid_friend",uid_friend);

            conversion.put("lastmessage",edt_sent_chat.getText().toString().trim());
            conversion.put("date",new Date());
            addconversion(conversion);
        }
        edt_sent_chat.setText(null);
    }
    private void listenerAvailability(){
        firestore.collection("user").document(uid_friend)
                .addSnapshotListener(Chat_Activity.this,(value, error) -> {
                   if (error!=null){
                       return;
                   }if (value!=null){
                       if (value.getLong("online")!=null){
                           int availability= Objects.requireNonNull(value.getLong("online")).intValue();
                           isReceiverAvailable=availability==1;
                       }
                    }
                   if (isReceiverAvailable){
                       txt_chat_Availability.setVisibility(View.VISIBLE);
                   }else {
                       txt_chat_Availability.setVisibility(View.GONE);
                   }
                });
    }
    private String formatDate(Date date){
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date);
    }
    private EventListener<QuerySnapshot> snapshotEventListener=new EventListener<QuerySnapshot>() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (!value.isEmpty()){
                int count=chatMessages.size();
                for (DocumentChange change:value.getDocumentChanges()){
                    if (change.getType()==DocumentChange.Type.ADDED){
                        chatMessage chatMessage=new chatMessage();
                        chatMessage.setSenderId(change.getDocument().getString("uid_person"));
                        chatMessage.setReceiverId(change.getDocument().getString("uid_friend"));
                        chatMessage.setMessage(change.getDocument().getString("person_sent"));
                        chatMessage.setDateTime(formatDate(change.getDocument().getDate("time")));
                        chatMessage.setDate(change.getDocument().getDate("time"));
                        chatMessages.add(chatMessage);
                    }
                }Collections.sort(chatMessages,(obj1,obj2)->obj1.getDate().compareTo(obj2.getDate()));
                if (count==0){
                    adapter_chat.notifyDataSetChanged();
                }else {
                    adapter_chat.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
                    recylerview_sent_chat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            recylerview_sent_chat.scrollToPosition(recylerview_sent_chat.getAdapter().getItemCount()-1);
                        }
                    });
                }

            }
            if (conversionId==null){
                checkForconversion();
            }
        }

    };
    private void addconversion(HashMap<String,Object> conversion){
        firestore.collection("conversations")
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId=documentReference.getId());
    }
    private void updateconversations(String message){
        DocumentReference documentReference=
                firestore.collection("conversations")
                .document(conversionId);
        documentReference.update("lastmessage",message,
                "date",new Date());
    }
    private void checkForconversion(){
        if (chatMessages.size()!=0){
            checkForconversionRemotely(Main_FireChat.uid,uid_friend);
            checkForconversionRemotely(uid_friend,Main_FireChat.uid);
        }
    }

    private void checkForconversionRemotely(String senderId,String receiverId){
        firestore.collection("conversations")
                .whereEqualTo("uid_person",senderId)
                .whereEqualTo("uid_friend",receiverId)
                .get()
                .addOnCompleteListener(snapshot);
    }
    private final OnCompleteListener<QuerySnapshot> snapshot=new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size()>0){
                DocumentSnapshot snapshot=task.getResult().getDocuments().get(0);
                conversionId=snapshot.getId();
            }
        }
    };


    private void loadHeader() {
        try {
            DocumentReference docRef = firestore.collection("user").document(uid_friend);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        try {
                            String avtHeader = value.getString("avt");
                            Glide.with(Chat_Activity.this).load(avtHeader).into(img_friend_chat);
                            txt_nameFriend_chat.setText(value.getString("yourname"));
                        }catch (Exception e){}
                    }
                }
            });
        }catch (Exception e){}
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter_chat!=null){
            adapter_chat.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenerAvailability();
    }
}