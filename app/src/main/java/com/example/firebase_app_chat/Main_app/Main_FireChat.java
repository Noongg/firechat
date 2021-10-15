package com.example.firebase_app_chat.Main_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Fragment.fragment_chat;
import com.example.firebase_app_chat.Fragment.fragment_people;
import com.example.firebase_app_chat.Fragment.fragment_user;
import com.example.firebase_app_chat.R;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Main_FireChat extends AppCompatActivity {

    public static String uid;
    FirebaseFirestore firestore;
    BottomNavigationView firechat_bottom_Nav;
    FrameLayout firechat_framelayout;
    RelativeLayout firechat_top;

    public static final int FRAGMENT_CHAT=0;
    public static final int FRAGMENT_PEOPLE=1;
    public static final int FRAGMENT_USER=2;
    private int currentFragment=FRAGMENT_CHAT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_firechat);

        firechat_bottom_Nav = (BottomNavigationView) findViewById(R.id.firechat_bottom_Nav);
        firechat_framelayout=(FrameLayout) findViewById(R.id.firechat_framelayout);
        firechat_top=(RelativeLayout) findViewById(R.id.firechat_top);

        firestore = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("phone_number", MODE_PRIVATE);
        uid = sharedPreferences.getString("key_uid", "");

        replaceFragment(new fragment_chat());
        try {
            firestore.collection("user").document(uid)
                    .collection("friend")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (!value.isEmpty()) {
                                for (DocumentChange change : value.getDocumentChanges()) {
                                    DocumentSnapshot snapshot = change.getDocument();
                                    String checkSeen = snapshot.getString("seen");
                                    if (checkSeen.equals("false")){
                                        BadgeDrawable badgeDrawable=firechat_bottom_Nav.getOrCreateBadge(R.id.bottom_people);
                                        badgeDrawable.setVisible(true);
                                    }else {
                                        BadgeDrawable badgeDrawable=firechat_bottom_Nav.getOrCreateBadge(R.id.bottom_people);
                                        badgeDrawable.setVisible(false);
                                    }
                                }
                            }
                        }
                    });
        }catch (Exception e){}
        firechat_bottom_Nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.bottom_chat) {
                    openFragmentChat();
                }
                else if (id == R.id.bottom_people) {
                    openFragmentPeople();
                    BadgeDrawable badgeDrawable=firechat_bottom_Nav.getOrCreateBadge(R.id.bottom_people);
                    badgeDrawable.setVisible(false);

                }
                else if (id == R.id.bottom_user) {
                    openFragmentUser();
                }
                return true;
            }
        });

    }
    private void openFragmentChat(){
        if (currentFragment!=FRAGMENT_CHAT){
            replaceFragment(new fragment_chat());
            currentFragment=FRAGMENT_CHAT;
        }
    }
    private void openFragmentPeople(){
        if (currentFragment!=FRAGMENT_PEOPLE){
            replaceFragment(new fragment_people());
            currentFragment=FRAGMENT_PEOPLE;
        }
    }
    private void openFragmentUser(){
        if (currentFragment!=FRAGMENT_USER){
            replaceFragment(new fragment_user());
            currentFragment=FRAGMENT_USER;
        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.firechat_framelayout,fragment);
        fragmentTransaction.commit();
    }


}