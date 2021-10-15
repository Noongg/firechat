package com.example.firebase_app_chat.Account;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends AppCompatActivity {
    TextView edtForgotPhone;
    Button buttonForgotLogin;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        buttonForgotLogin=(Button) findViewById(R.id.buttonForgotLogin);
        edtForgotPhone=(TextView) findViewById(R.id.edtForgotPhone);

        firestore=FirebaseFirestore.getInstance();

        buttonForgotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhoneEnter=edtForgotPhone.getText().toString().trim();
                String mphoneEnter=userPhoneEnter.substring(1,userPhoneEnter.length());
                String userEnterPhone1="+84"+mphoneEnter;

                firestore.collection("user")
                        .whereEqualTo("phone",userEnterPhone1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful() && !task.getResult().isEmpty()){
                                    Intent intent=new Intent(ForgotPassword.this,Reg_Otp_Phone.class);
                                    intent.putExtra("mobile",userEnterPhone1);
                                    intent.putExtra("forgotPass","resetpass");
                                    startActivity(intent);
                                }else {
                                    edtForgotPhone.setError("No such phone exist!");
                                    edtForgotPhone.requestFocus();
                                }
                            }
                        });

            }
        });

    }
}