package com.example.firebase_app_chat.User_Setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Profile_user extends AppCompatActivity {
    ImageView profile_user_avt,profile_user_img_cover,profile_user_back;
    TextView profile_user_userName,profile_user_Gender,profile_user_birthday,profile_user_phone,profile_user_story;
    LinearLayout linearLayout;
    String uid=Main_FireChat.uid;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        firestore=FirebaseFirestore.getInstance();
        initUi();
        showinfoUser();

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user.this,Profile_user_edit.class);
                startActivity(intent);
            }
        });
        profile_user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user.this,Main_FireChat.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile_user.this,Main_FireChat.class);
        startActivity(intent);
    }

    private void initUi() {
        profile_user_userName=(TextView) findViewById(R.id.profile_user_userName);
        profile_user_birthday=(TextView) findViewById(R.id.profile_user_birthday);
        profile_user_phone=(TextView) findViewById(R.id.profile_user_phone);
        profile_user_Gender=(TextView) findViewById(R.id.profile_user_Gender);
        profile_user_story=(TextView) findViewById(R.id.profile_user_story);
        linearLayout=(LinearLayout) findViewById(R.id.linearLayout);
        profile_user_avt=(ImageView) findViewById(R.id.profile_user_avt);
        profile_user_img_cover=(ImageView) findViewById(R.id.profile_user_img_cover);
        profile_user_back=(ImageView) findViewById(R.id.profile_user_back);
    }

    private void showinfoUser(){


        try {
            DocumentReference docRef = firestore.collection("user").document(uid);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        try {
                            profile_user_userName.setText(value.getString("yourname"));
                            profile_user_birthday.setText(value.getString("birthday"));
                            profile_user_phone.setText(value.getString("phone"));
                            profile_user_Gender.setText(value.getString("gender"));
                            profile_user_story.setText(value.getString("story"));
                            String avt=value.getString("avt");
                            Glide.with(Profile_user.this).load(avt).into(profile_user_avt);
                            String cover=value.getString("cover");
                            Glide.with(Profile_user.this).load(cover).into(profile_user_img_cover);
                        }catch (Exception e){

                        }
                    }
                }
            });
        }
        catch (Exception e){

        }

    }
}