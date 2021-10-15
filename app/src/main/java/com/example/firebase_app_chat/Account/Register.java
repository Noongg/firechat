package com.example.firebase_app_chat.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class Register extends AppCompatActivity{

    EditText edtRegYourName,edtRegPhone,edtRegPass,edtRegBirthday,edtRegGender;
    Button btnRegister;
    TextView txtSignIn;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtSignIn=(TextView) findViewById(R.id.txtSignIn);
        edtRegPass=(EditText) findViewById(R.id.edtRegPassword);
        edtRegPhone=(EditText) findViewById(R.id.edtRegPhone);
        edtRegBirthday=(EditText)findViewById(R.id.edtRegBirthday);
        edtRegYourName=(EditText) findViewById(R.id.edtRegYourName);
        edtRegGender=(EditText) findViewById(R.id.edtRegGender);
        btnRegister=(Button) findViewById(R.id.buttonRegister);

        firestore=FirebaseFirestore.getInstance();



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PHONE=edtRegPhone.getText().toString().trim();
                String username=edtRegYourName.getText().toString().trim();
                String gender=edtRegGender.getText().toString().trim();
                String birthday=edtRegBirthday.getText().toString().trim();
                String pass=edtRegPass.getText().toString().trim();

                if (username.isEmpty()){
                    edtRegYourName.setError("UserName is required!");
                    edtRegYourName.requestFocus();
                    return;

                }
                else if (PHONE.isEmpty()){
                    edtRegPhone.setError("Phone number is required!");
                    edtRegPhone.requestFocus();
                    return;
                }
                else if (edtRegPhone.length()<10||edtRegPhone.length()>10){
                    edtRegPhone.setError("Please provide valid Phone Number!");
                    edtRegPhone.requestFocus();
                    return;
                }
                else if (gender.isEmpty()){
                    edtRegGender.setError("Gender is required!");
                    edtRegGender.requestFocus();
                    return;
                }
                else if (birthday.isEmpty()){
                    edtRegBirthday.setError("Birthday is required!");
                    edtRegBirthday.requestFocus();
                    return;
                }
                else if (pass.isEmpty()){
                    edtRegPass.setError("PassWord is required!");
                    edtRegPass.requestFocus();
                    return;
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setCancelable(false); // if you want user to wait for some process to finish,
                    builder.setView(R.layout.dialog_progress);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    String mphone=PHONE.substring(1,PHONE.length());
                    String phonee="+84"+mphone;

                    CollectionReference collectionReference=firestore.collection("user");
                    Query checkPhone=collectionReference.whereEqualTo("phone",phonee);
                    checkPhone.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()){
                                Toast.makeText(Register.this, "That phone already exists.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Intent intent=new Intent(Register.this, Reg_Otp_Phone.class);
                                intent.putExtra("mobile",phonee);
                                intent.putExtra("username",username);
                                intent.putExtra("birthday",birthday);
                                intent.putExtra("gender",gender);
                                intent.putExtra("pass",pass);
                                intent.putExtra("forgotPass","createNewUser");
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }

            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Register.this,Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                finish();
            }
        });
    }




}