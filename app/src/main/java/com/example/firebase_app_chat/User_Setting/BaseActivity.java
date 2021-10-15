package com.example.firebase_app_chat.User_Setting;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Map<String,Object> newonline=new HashMap<>();
        newonline.put("online",0);
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("user").document(Main_FireChat.uid).update(newonline).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String,Object> newonline=new HashMap<>();
        newonline.put("online",1);
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("user").document(Main_FireChat.uid).update(newonline).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }
}
