package com.example.firebase_app_chat.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.prefs.Preferences;

public class Login extends AppCompatActivity {

    TextView txtSignup,txtForgotPassword;
    EditText edtPhone,edtPass;
    Button buttonLogin;
    CheckBox checkboxRemeber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtSignup=(TextView) findViewById(R.id.txtSignup);
        txtForgotPassword=(TextView) findViewById(R.id.txtForgotPassword);
        edtPass=(EditText) findViewById(R.id.edtPass);
        edtPhone=(EditText) findViewById(R.id.edtPhone);
        buttonLogin=(Button) findViewById(R.id.buttonLogin);
        checkboxRemeber=(CheckBox) findViewById(R.id.checkboxRemeber);



        SharedPreferences sharedPreferences1=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=sharedPreferences1.getString("remember","");
        if (checkbox.equals("true")){
            Intent intent=new Intent(Login.this, Main_FireChat.class);
            startActivity(intent);
            finish();
        }
        else {

        }

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
        checkboxRemeber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences sharedPreferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else if(!buttonView.isChecked()){
                    SharedPreferences sharedPreferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userPhone=edtPhone.getText().toString().trim();
                String userEnterPass=edtPass.getText().toString().trim();
                if (userPhone.isEmpty()){
                    edtPhone.setError("Phone number is required!");
                    edtPhone.requestFocus();
                    return;
                }
                else if (edtPhone.length()<10||edtPhone.length()>10){
                    edtPhone.setError("Please provide valid Phone Number!");
                    edtPhone.requestFocus();
                    return;
                }
                else if (userEnterPass.isEmpty()){
                    edtPass.setError("Password is required!");
                    edtPass.requestFocus();
                    return;
                }
                else {

                    isUser();
                }
            }
        });
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);

            }
        });
    }

    private void isUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.dialog_progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        String userPhone=edtPhone.getText().toString().trim();
        String mphone=userPhone.substring(1,userPhone.length());
        String userEnterPhone="+84"+mphone;
        String userEnterPass=edtPass.getText().toString().trim();

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("user")
                .whereEqualTo("phone",userEnterPhone)
                .whereEqualTo("password",userEnterPass)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult()!=null &&task.getResult().getDocuments().size()>0){
                            DocumentSnapshot snapshot=task.getResult().getDocuments().get(0);
                            Intent intent=new Intent(Login.this, Main_FireChat.class);
                            SharedPreferences sharedPreferences= getSharedPreferences("phone_number",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("key_uid",snapshot.getId());
                            editor.apply();
                            startActivity(intent);
                            finish();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(Login.this, "Wrong phone number or password", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("loi",e.getMessage() );
            }
        });
    }


}