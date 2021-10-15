package com.example.firebase_app_chat.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword_setNewPass extends AppCompatActivity {
    EditText edtForgotPassword,edtForgotconfirmPassword;
    Button buttonForgotUpdate;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_set_new_pass);
        edtForgotPassword=(EditText) findViewById(R.id.edtForgotPassword);
        edtForgotconfirmPassword=(EditText) findViewById(R.id.edtForgotconfirmPassword);
        buttonForgotUpdate=(Button) findViewById(R.id.buttonForgotUpdate);

        String phoneNumber=getIntent().getStringExtra("mobile");
        firestore=FirebaseFirestore.getInstance();

        buttonForgotUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ForgotPassword=edtForgotPassword.getText().toString().trim();
                String ForgotconfirmPassword=edtForgotconfirmPassword.getText().toString().trim();

                if (ForgotPassword.isEmpty()){
                    edtForgotPassword.setError("PassWord is required!");
                    edtForgotPassword.requestFocus();
                    return;
                }
                else if (ForgotconfirmPassword.isEmpty()){
                    edtForgotconfirmPassword.setError("PassWord is required!");
                    edtForgotconfirmPassword.requestFocus();
                    return;
                }
                else if (!ForgotPassword.equals(ForgotconfirmPassword)){
                    Toast.makeText(ForgotPassword_setNewPass.this, "Mật khẩu mới và mật khẩu nhập lại không trùng nhau", Toast.LENGTH_SHORT).show();
                }
                else if (ForgotPassword.equals(ForgotconfirmPassword)){
                    Map<String,Object> newPass=new HashMap<>();
                    newPass.put("password",ForgotPassword);
                    firestore.collection("user")
                            .whereEqualTo("phone",phoneNumber)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()){
                                DocumentSnapshot snapshot=task.getResult().getDocuments().get(0);
                                String uid=snapshot.getId();
                                firestore.collection("user")
                                        .document(uid)
                                        .update(newPass)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent=new Intent(ForgotPassword_setNewPass.this,Login.class);
                                                startActivity(intent);
                                                Toast.makeText(ForgotPassword_setNewPass.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });


                }
                else {
                    Toast.makeText(ForgotPassword_setNewPass.this, "Nhập lại mật khẩu", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}