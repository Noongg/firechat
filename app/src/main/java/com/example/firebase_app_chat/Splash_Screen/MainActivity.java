package com.example.firebase_app_chat.Splash_Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.firebase_app_chat.Account.Login;
import com.example.firebase_app_chat.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
    }

    private void loadData() {
        if (AppUtil.checkNetwork(this)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
        }else {
            Toast.makeText(MainActivity.this, "Network disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}