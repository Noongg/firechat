package com.example.firebase_app_chat.User_Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.firebase_app_chat.R;

public class Dark_mode extends AppCompatActivity {
    CheckBox checkbox_dark,checkbox_light;
    boolean uncheck=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode);
        checkbox_dark=(CheckBox) findViewById(R.id.checkbox_dark);
        checkbox_light=(CheckBox) findViewById(R.id.checkbox_light);


        SharedPreferences sharedPreferences=getSharedPreferences("darkmode",0);
        checkbox_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox_light.isChecked()){
                    checkbox_dark.setChecked(uncheck);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else {

                }
            }
        });
        checkbox_dark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox_dark.isChecked()){
                    checkbox_light.setChecked(uncheck);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {

                }

            }
        });

    }
}