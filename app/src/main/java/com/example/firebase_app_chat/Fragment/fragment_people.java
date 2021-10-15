package com.example.firebase_app_chat.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.firebase_app_chat.Adapter.Adapter_fragment_people;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class fragment_people extends Fragment {
    View view;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    Adapter_fragment_people adapter;
    FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_people,container,false);
        tabLayout=(TabLayout) view.findViewById(R.id.tab_layout);
        viewPager=(ViewPager2) view.findViewById(R.id.Viewpager);
        adapter=new Adapter_fragment_people(getActivity());
        viewPager.setAdapter(adapter);
        firestore=FirebaseFirestore.getInstance();
        CollectionReference reference= firestore.collection("user").document(Main_FireChat.uid)
                .collection("friend");
        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty())    {
                    for (DocumentChange change:value.getDocumentChanges()) {
                        DocumentSnapshot snapshot = change.getDocument();
                        String checkSeen = snapshot.getString("seen");
                        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override
                            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                switch (position){
                                    case 0:
                                        tab.setText("Bạn bè");
                                        break;
                                    case 1:
                                        tab.setText("Lời mời kết bạn");
                                        if (checkSeen.equals("false")){
                                            BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                                            badgeDrawable.setVisible(true);
                                        }
                                        else {
                                            BadgeDrawable badgeDrawable=tab.getOrCreateBadge();
                                            badgeDrawable.setVisible(false);
                                        }
                                        break;
                                    case 2:
                                        tab.setText("Đã gửi");
                                        break;
                                }
                            }
                        }).attach();
                        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                BadgeDrawable badgeDrawable=tabLayout.getTabAt(position).getOrCreateBadge();
                                badgeDrawable.setVisible(false);
                                if (checkSeen.equals("false")){
                                    reference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (DocumentChange change1:queryDocumentSnapshots.getDocumentChanges()){
                                                DocumentSnapshot snapshot1=change1.getDocument();
                                                String id=snapshot1.getId();
                                                Map<String,Object> ischeckSeen=new HashMap<>();
                                                ischeckSeen.put("seen","true");
                                                reference.document(id).update(ischeckSeen).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });


        return view;
    }
}
