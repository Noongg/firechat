package com.example.firebase_app_chat.Account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Profile_user;
import com.example.firebase_app_chat.User_Setting.Profile_user_edit;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Upload_img extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 123;
    String userId;
    ImageView upload_avt;
    Uri mUri;
    Button upload_btn;
    String change_avt;
    ActivityResultLauncher<Intent> resultLauncher=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()==RESULT_OK){
                        Intent intent=result.getData();
                        if (intent==null){
                            return;
                        }else {
                            Uri uri=intent.getData();
                            mUri=uri;
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);

                            try {
                                Drawable drawable=ImageDecoder.decodeDrawable(source);
//                                Bitmap bitmap = ImageDecoder.decodeBitmap(source);
//                                upload_avt.setImageBitmap(bitmap);
                                upload_avt.setImageDrawable(drawable);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_img);
        upload_avt=(ImageView) findViewById(R.id.upload_avt);
        upload_btn=(Button) findViewById(R.id.upload_btn);
        userId=getIntent().getStringExtra("userId");
        change_avt=getIntent().getStringExtra("update_avt");

        uploadImg();
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId==null){
                    userId= Main_FireChat.uid;
                }
                if (mUri==null){
                    Toast.makeText(Upload_img.this, "bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFileimg();
                }
            }
        });

    }

    private void uploadImg() {
        upload_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickRequestPermission();
            }
        });
    }

    private void onclickRequestPermission() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String [] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_REQUEST_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery();
            }else {
                Toast.makeText(Upload_img.this, "bạn từ chối quyền mở thư mục ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(Intent.createChooser(intent,"Select Picture"));
    }
    private void uploadFileimg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Upload_img.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.dialog_progress);
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        StorageReference storageReference=firebaseStorage.getReference();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date date=new Date();
        String filename=dateFormat.format(date);
        StorageReference storageImg=storageReference.child("images/"+userId+"/"+filename);
        storageImg.putFile(mUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                Map<String,Object> newAvt=new HashMap<>();
                                newAvt.put("avt",url);
                                FirebaseFirestore firestore;
                                firestore=FirebaseFirestore.getInstance();
                                firestore.collection("user")
                                        .document(userId)
                                        .update(newAvt)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (change_avt.equals("update_avt_create")){
                                                    dialog.dismiss();
                                                    Intent intent=new Intent(Upload_img.this,Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(Upload_img.this, "Upload Avatar Success", Toast.LENGTH_SHORT).show();
                                                }if (change_avt.equals("update_user_avt")){
                                                    dialog.dismiss();
                                                    Intent intent=new Intent(Upload_img.this, Profile_user_edit.class);
                                                    startActivity(intent);
                                                    Toast.makeText(Upload_img.this, "Change Avatar Success", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }

                                            }
                                        });
                            }
                        });
                    }
                });
    }
}