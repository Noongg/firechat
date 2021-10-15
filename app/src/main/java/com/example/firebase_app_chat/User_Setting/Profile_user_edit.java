package com.example.firebase_app_chat.User_Setting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebase_app_chat.Account.Upload_Name;
import com.example.firebase_app_chat.Account.Upload_birthday;
import com.example.firebase_app_chat.Account.Upload_gender;
import com.example.firebase_app_chat.Account.Upload_img;
import com.example.firebase_app_chat.Account.Upload_img_cover;
import com.example.firebase_app_chat.Account.Upload_password;
import com.example.firebase_app_chat.Account.Upload_story;
import com.example.firebase_app_chat.R;

public class Profile_user_edit extends AppCompatActivity {
    TextView txt_update_avt,txt_update_name,txt_update_gender,txt_update_birthday,txt_update_story,txt_update_cover,txt_update_password;
    ImageView profile_user_edit_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_edit);
        txt_update_avt=(TextView) findViewById(R.id.txt_update_avt);
        txt_update_name=(TextView)findViewById(R.id.txt_update_name);
        txt_update_gender=(TextView)findViewById(R.id.txt_update_gender);
        txt_update_birthday=(TextView)findViewById(R.id.txt_update_birthday);
        txt_update_story=(TextView)findViewById(R.id.txt_update_story);
        txt_update_cover=(TextView) findViewById(R.id.txt_update_cover);
        txt_update_password=(TextView) findViewById(R.id.txt_update_password);
        profile_user_edit_back=(ImageView) findViewById(R.id.profile_user_edit_back);

        profile_user_edit_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this,Profile_user.class);
                startActivity(intent);
            }
        });
        txt_update_avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_img.class);
                intent.putExtra("update_avt","update_user_avt");
                startActivity(intent);
            }
        });
        txt_update_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_img_cover.class);
                startActivity(intent);
            }
        });
        txt_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_Name.class);
                startActivity(intent);
            }
        });
        txt_update_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_gender.class);
                startActivity(intent);
            }
        });
        txt_update_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_birthday.class);
                startActivity(intent);
            }
        });
        txt_update_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_story.class);
                startActivity(intent);
            }
        });
        txt_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_user_edit.this, Upload_password.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Profile_user_edit.this, Profile_user.class);
        startActivity(intent);
    }
}