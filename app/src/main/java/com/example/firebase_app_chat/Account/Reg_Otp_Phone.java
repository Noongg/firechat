package com.example.firebase_app_chat.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Reg_Otp_Phone extends AppCompatActivity {
    EditText edtRegOtpPhone;
    TextView txtOTPPhone,txtcodefail,txtResendOTP;
    Button btnRegVerify;
    FirebaseAuth mAuth;
    String verificationCodeBySystem;
    String phoneNumber,yourName,birthday,PassWord,phoneNumberForgot,userId,gender;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_otp_phone);
        edtRegOtpPhone=(EditText)findViewById(R.id.edtRegOtpPhone);
        txtOTPPhone=(TextView) findViewById(R.id.txtOTPPhone);
        btnRegVerify=(Button) findViewById(R.id.btnRegVerify);
        txtcodefail=(TextView) findViewById(R.id.txtcodefail);
        txtResendOTP=(TextView) findViewById(R.id.txtResendOTP);

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        phoneNumber=getIntent().getStringExtra("mobile");
        yourName=getIntent().getStringExtra("username");
        birthday=getIntent().getStringExtra("birthday");
        PassWord=getIntent().getStringExtra("pass");
        gender=getIntent().getStringExtra("gender");
        phoneNumberForgot=getIntent().getStringExtra("forgotPass");
        txtOTPPhone.setText(phoneNumber);

        startPhoneNumberVerification(phoneNumber);

        btnRegVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getCode=edtRegOtpPhone.getText().toString().trim();
                if (getCode.isEmpty()||getCode.length()<6){
                    Toast.makeText(Reg_Otp_Phone.this, "Bạn hãy nhập đúng code", Toast.LENGTH_SHORT).show();
                }
                else {
                    verifyCode(getCode);
                }
            }
        });
        txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(phoneNumber,token);
            }
        });


    }

    private void resendOTP(String phoneNumber,PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(token)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        PhoneAuthOptions options=PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null){
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Reg_Otp_Phone.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
            token=forceResendingToken;
        }
    };
    private void verifyCode(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInTheUserByCredentials(credential);

    }
    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if ( phoneNumberForgot.equals("resetpass")){
                                Intent intent1=new Intent(Reg_Otp_Phone.this,ForgotPassword_setNewPass.class);
                                intent1.putExtra("mobile",phoneNumber);
                                startActivity(intent1);
                            }
                            if(phoneNumberForgot.equals("createNewUser")){
                                Toast.makeText(getApplicationContext(), "Your Account has been created!", Toast.LENGTH_SHORT).show();
                                userId=mAuth.getCurrentUser().getUid();
                                DocumentReference documentReference=firestore.collection("user").document(userId);
                                Map<String, Object> user=new HashMap<>();
                                user.put("yourname",yourName);
                                user.put("phone",phoneNumber);
                                user.put("birthday",birthday);
                                user.put("password",PassWord);
                                user.put("gender",gender);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent(Reg_Otp_Phone.this, Upload_img.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("userId",userId);
                                        intent.putExtra("update_avt","update_avt_create");
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Sai mã code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}