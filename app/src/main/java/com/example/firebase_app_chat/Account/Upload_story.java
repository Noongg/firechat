package com.example.firebase_app_chat.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Profile_user;
import com.example.firebase_app_chat.User_Setting.Profile_user_edit;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Upload_story extends AppCompatActivity {
    Button upload_btn_story_user;
    EditText upload_story_user;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_story);
        upload_btn_story_user=(Button) findViewById(R.id.upload_btn_story_user);
        upload_story_user=(EditText) findViewById(R.id.upload_story_user);
        userId= Main_FireChat.uid;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        upload_btn_story_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Upload_story.this);
                builder.setCancelable(false); // if you want user to wait for some process to finish,
                builder.setView(R.layout.dialog_progress);
                AlertDialog dialog = builder.create();
                dialog.show();

                String story=upload_story_user.getText().toString();
                DocumentReference documentReference=firestore.collection("user").document(userId);
                Map<String,Object> newstory=new HashMap<>();
                newstory.put("story",story);
                documentReference.update(newstory)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                dialog.dismiss();
                                Intent intent=new Intent(Upload_story.this, Profile_user_edit.class);
                                startActivity(intent);
                                Toast.makeText(Upload_story.this, "Change Story Success", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

            }
        });

    }
}