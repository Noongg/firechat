package com.example.firebase_app_chat.User_Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.Adapter.Adapter_user_search;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Search_person extends AppCompatActivity {
    SearchView search_User;
    ImageView img_search_back;
    RecyclerView recycler_search;
    FirebaseFirestore firestore;
    Adapter_user_search adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_person);
        search_User=(SearchView)findViewById(R.id.search_User);
        recycler_search=(RecyclerView) findViewById(R.id.recycler_search);
        img_search_back=(ImageView) findViewById(R.id.img_search_back);

        firestore= FirebaseFirestore.getInstance();
        loaduser();

        img_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Search_person.this,Main_FireChat.class);
                startActivity(intent);
            }
        });

        search_User.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Search_person.this,Main_FireChat.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void processsearch(String query) {
        Query query1=firestore.collection("user").orderBy("yourname").startAt(query).endAt(query+"\uf8ff");
        FirestoreRecyclerOptions<UserDb> recyclerOptions=new FirestoreRecyclerOptions.Builder<UserDb>()
                .setQuery(query1,UserDb.class)
                .build();
        adapter=new Adapter_user_search(recyclerOptions,getApplicationContext());
        recycler_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_search.setAdapter(adapter);
        adapter.startListening();
        recycler_search.setItemAnimator(null);
        adapter.setOnItemClick(new Adapter_user_search.OnItemClickListenner() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                String id=snapshot.getId();
                Intent intent=new Intent(getApplicationContext(), Profile_person.class);
                intent.putExtra("uid_person",id);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void loaduser() {
        Query query=firestore.collection("user");
        FirestoreRecyclerOptions<UserDb> options=new FirestoreRecyclerOptions.Builder<UserDb>()
                .setQuery(query,UserDb.class)
                .build();
        adapter=new Adapter_user_search(options,getApplicationContext());
        recycler_search.setLayoutManager(new LinearLayoutManager(this));
        recycler_search.setAdapter(adapter);
        recycler_search.setItemAnimator(null);
        adapter.setOnItemClick(new Adapter_user_search.OnItemClickListenner() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                String id=snapshot.getId();
                Intent intent=new Intent(getApplicationContext(), Profile_person.class);
                intent.putExtra("uid_person",id);
                startActivity(intent);
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter.release();
        }
    }
}