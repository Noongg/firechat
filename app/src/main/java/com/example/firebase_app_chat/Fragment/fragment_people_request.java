package com.example.firebase_app_chat.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.Adapter.Adapter_fragment_people_friend;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Profile_person;
import com.example.firebase_app_chat.User_Setting.Search_person;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class fragment_people_request extends Fragment {
    View view;
    RecyclerView recycler_request;
    FirebaseFirestore firestore;
    LinearLayout search_bar_request;
    Adapter_fragment_people_friend adapter_user;
    TextView txt_people_count_request;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_people_request,container,false);
        recycler_request=(RecyclerView) view.findViewById(R.id.recycler_request);
        txt_people_count_request=(TextView)view.findViewById(R.id.txt_people_count_request);
        search_bar_request=(LinearLayout) view.findViewById(R.id.search_bar_request);

        search_bar_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Search_person.class);
                startActivity(intent);
            }
        });
        firestore= FirebaseFirestore.getInstance();
        loadpeople();

        return view;
    }
    private void loadpeople() {
        Query query=firestore.collection("user").document(Main_FireChat.uid)
                .collection("friend")
                .whereEqualTo("ischeck","waiting");
        FirestoreRecyclerOptions<UserDb> options=new FirestoreRecyclerOptions.Builder<UserDb>()
                .setQuery(query,UserDb.class)
                .build();
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                int count=value.size();
                txt_people_count_request.setText(String.valueOf(count));
            }
        });
        adapter_user=new Adapter_fragment_people_friend(options,getActivity());
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false);
        recycler_request.setLayoutManager(linearLayoutManager);
        recycler_request.setAdapter(adapter_user);
        recycler_request.setItemAnimator(null);
        adapter_user.setOnItemClick(new Adapter_fragment_people_friend.OnItemClickListenner() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(DocumentSnapshot snapshot, int position) {
                String id=snapshot.getId();
                Intent intent=new Intent(getActivity(), Profile_person.class);
                intent.putExtra("uid_person",id);
                startActivity(intent);
                adapter_user.notifyDataSetChanged();
            }
        });
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
}