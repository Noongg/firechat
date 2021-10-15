package com.example.firebase_app_chat.Account;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Profile_user_edit;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Upload_password extends AppCompatActivity {

    TextView upload_pass_old,upload_pass_new,upload_pass_new2;
    Button upload_btn_pass_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_password);
        upload_pass_old=(TextView) findViewById(R.id.upload_pass_old);
        upload_pass_new=(TextView) findViewById(R.id.upload_pass_new);
        upload_pass_new2=(TextView) findViewById(R.id.upload_pass_new2);
        upload_btn_pass_user=(Button) findViewById(R.id.upload_btn_pass_user);

        upload_btn_pass_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass_old=upload_pass_old.getText().toString().trim();
                String pass_new=upload_pass_new.getText().toString().trim();
                String pass_confirm=upload_pass_new2.getText().toString().trim();
                if (pass_old.isEmpty()){
                    upload_pass_old.setError("Pass is required!");
                    upload_pass_old.requestFocus();
                    return;
                }
                else if (pass_new.isEmpty()){
                    upload_pass_new.setError("Pass is required!");
                    upload_pass_new.requestFocus();
                    return;
                }
                else if (pass_confirm.isEmpty()){
                    upload_pass_new2.setError("Pass is required!");
                    upload_pass_new2.requestFocus();
                    return;
                }
                else if (pass_new.equals(pass_old)){
                    Toast.makeText(Upload_password.this, "Mật khẩu cũ và mật khẩu mới không được trùng nhau", Toast.LENGTH_SHORT).show();
                }
                else if (!pass_new.equals(pass_confirm)){
                    Toast.makeText(Upload_password.this, "Mật khẩu mới và mật khẩu nhập lại không trùng nhau", Toast.LENGTH_SHORT).show();
                }else {
                    String uid= Main_FireChat.uid;
                    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                    Map<String,Object> newPassword=new HashMap<>();
                    newPassword.put("password",pass_new);
                    firestore.collection("user")
                            .document(uid)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()){
                                        String pass_user=documentSnapshot.getString("password");
                                        if (pass_old.equals(pass_user)){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Upload_password.this);
                                            builder.setCancelable(false); // if you want user to wait for some process to finish,
                                            builder.setView(R.layout.dialog_progress);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();
                                            firestore.collection("user")
                                                    .document(uid)
                                                    .update(newPassword)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            dialog.dismiss();
                                                            Intent intent=new Intent(Upload_password.this, Profile_user_edit.class);
                                                            startActivity(intent);
                                                            Toast.makeText(Upload_password.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(Upload_password.this, "Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });
                }
            }
        });
    }
}