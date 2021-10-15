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

public class Upload_birthday extends AppCompatActivity {
    Button upload_btn_birthday_user;
    EditText upload_birthday_user;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_birthday);
        upload_btn_birthday_user=(Button) findViewById(R.id.upload_btn_birthday_user);
        upload_birthday_user=(EditText) findViewById(R.id.upload_birthday_user);
        userId= Main_FireChat.uid;
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();

        upload_btn_birthday_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upload_birthday_user.length()<1){
                    Toast.makeText(Upload_birthday.this, "Mời bạn nhập ngày sinh", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Upload_birthday.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.dialog_progress);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    String birthday=upload_birthday_user.getText().toString().trim();
                    DocumentReference documentReference=firestore.collection("user").document(userId);
                    Map<String,Object> newBirthday=new HashMap<>();
                    newBirthday.put("birthday",birthday);

                    documentReference.update(newBirthday)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    dialog.dismiss();
                                    Intent intent=new Intent(Upload_birthday.this, Profile_user_edit.class);
                                    startActivity(intent);
                                    Toast.makeText(Upload_birthday.this, "Change BirthDay Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            }
        });
    }
}