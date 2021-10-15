package com.example.firebase_app_chat.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Adapter.Adapter_chat_lastMessage;
import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.Adapter.Adapter_user;
import com.example.firebase_app_chat.Main_app.Chat_Activity;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.Models.chatMessage;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Search_person;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class fragment_chat extends Fragment implements Adapter_chat_lastMessage.ConversionListener{
    View view;
    LinearLayout search_bar;
    FirebaseFirestore firestore;
    RecyclerView recyclerView,recycler_chat;
    Adapter_user adapter_user;
    ImageView firechat_imgview;
    List<chatMessage> chatMessage;
    Adapter_chat_lastMessage adapter_chat_lastMessage;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chat,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler_people) ;
        recycler_chat=(RecyclerView)view.findViewById(R.id.recycler_chat);
        firechat_imgview = (ImageView) view.findViewById(R.id.firechat_imgview);
        search_bar=(LinearLayout) view.findViewById(R.id.search_bar);

        search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Search_person.class);
                startActivity(intent);
            }
        });
        firestore=FirebaseFirestore.getInstance();
        loadpeople();
        loadHeader();
        loadLastChat();
        listConversation();
        return view;
    }

    private void loadLastChat() {
        chatMessage= new ArrayList<>();
        adapter_chat_lastMessage=new Adapter_chat_lastMessage(getActivity(),chatMessage,this);
        recycler_chat.setAdapter(adapter_chat_lastMessage);
    }
    private void listConversation(){
        firestore.collection("conversations")
                .whereEqualTo("uid_person",Main_FireChat.uid)
                .addSnapshotListener(snapshotEventListener);
        firestore.collection("conversations")
                .whereEqualTo("uid_friend",Main_FireChat.uid)
                .addSnapshotListener(snapshotEventListener);
    }
    private EventListener<QuerySnapshot> snapshotEventListener=new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
            if (!value.isEmpty())  {
                for (DocumentChange change:value.getDocumentChanges()){
                    if (change.getType()==DocumentChange.Type.ADDED){
                        String senderId=change.getDocument().getString("uid_person");
                        String receiverId=change.getDocument().getString("uid_friend");
                        com.example.firebase_app_chat.Models.chatMessage chatMessage1=new chatMessage();
                        chatMessage1.setSenderId(senderId);
                        chatMessage1.setReceiverId(receiverId);
                        if (Main_FireChat.uid.equals(senderId)){
                            chatMessage1.setConversionImg(change.getDocument().getString("avt_friend"));
                            chatMessage1.setConversionName(change.getDocument().getString("name_friend"));
                            chatMessage1.setConversionId(change.getDocument().getString("uid_friend"));
                        }else {
                            chatMessage1.setConversionImg(change.getDocument().getString("avt_person"));
                            chatMessage1.setConversionName(change.getDocument().getString("name_person"));
                            chatMessage1.setConversionId(change.getDocument().getString("uid_person"));
                        }
                        chatMessage1.setMessage(change.getDocument().getString("lastmessage"));
                        chatMessage1.setDate(change.getDocument().getDate("date"));
                        chatMessage.add(chatMessage1);
                    }else if (change.getType()== DocumentChange.Type.MODIFIED){
                        for (int i=0;i<chatMessage.size();i++){
                            String senderId=change.getDocument().getString("uid_person");
                            String receiverId=change.getDocument().getString("uid_friend");
                            if (chatMessage.get(i).getSenderId().equals(senderId) && chatMessage.get(i).getReceiverId().equals(receiverId)){
                                chatMessage.get(i).setMessage(change.getDocument().getString("lastmessage"));
                                chatMessage.get(i).setDate(change.getDocument().getDate("date"));
                                break;
                            }
                        }
                    }
                }
                Collections.sort(chatMessage,(obj1,obj2)->obj2.getDate().compareTo(obj1.getDate()));
                adapter_chat_lastMessage.notifyDataSetChanged();
            }
        }
    };

    private void loadpeople() {
        Query query=firestore.collection("user").document(Main_FireChat.uid)
                .collection("friend")
                .whereEqualTo("ischeck","friend");
        FirestoreRecyclerOptions<UserDb> options=new FirestoreRecyclerOptions.Builder<UserDb>()
                .setQuery(query,UserDb.class)
                .build();

        adapter_user=new Adapter_user(options,getActivity());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter_user);
        recyclerView.setItemAnimator(null);
        adapter_user.setOnItemClick(new Adapter_user.OnItemClickListenner() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(DocumentSnapshot snapshot,int position) {
                String id=snapshot.getId();
                Intent intent=new Intent(getActivity(), Chat_Activity.class);
                intent.putExtra("uid_person",id);
                startActivity(intent);
                adapter_user.notifyDataSetChanged();
            }
        });
    }
    private void loadHeader() {
        try {
            DocumentReference docRef = firestore.collection("user").document(Main_FireChat.uid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        if (getActivity() == null) {
                            return;
                        }
                        String avtHeader = value.getString("avt");
                        Glide.with(getActivity()).load(avtHeader).into(firechat_imgview);
                    }
                }
            });
        }catch (Exception e){}
    }
    @Override
    public void onStart() {
        super.onStart();

            adapter_user.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();

            adapter_user.stopListening();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter_user!=null){
            adapter_user.release();
        }
    }

    @Override
    public void onConversionClick(UserDb userDb) {
        Intent intent=new Intent(getActivity(),Chat_Activity.class);
        intent.putExtra("uid_person", userDb.getId());

        startActivity(intent);
    }
}
