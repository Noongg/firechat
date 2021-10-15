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

public class Upload_Name extends AppCompatActivity {
    Button upload_btn_nameUser;
    EditText upload_nameUser;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_name);
        upload_btn_nameUser =(Button) findViewById(R.id.upload_btn_nameUser);
        upload_nameUser=(EditText) findViewById(R.id.upload_nameUser);

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        userId= Main_FireChat.uid;
        upload_btn_nameUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upload_nameUser.length()<1){
                    Toast.makeText(Upload_Name.this, "Mời bạn nhập tên", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Upload_Name.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.dialog_progress);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    String name=upload_nameUser.getText().toString().trim();
                    DocumentReference documentReference=firestore.collection("user").document(userId);
                    Map<String,Object> newName=new HashMap<>();
                    newName.put("yourname",name);
                    documentReference.update(newName)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(Upload_Name.this, Profile_user_edit.class);
                                    startActivity(intent);
                                    Toast.makeText(Upload_Name.this, "Change Name Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });
    }
}