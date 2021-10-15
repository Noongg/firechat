package com.example.firebase_app_chat.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Account.Login;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.example.firebase_app_chat.User_Setting.Dark_mode;
import com.example.firebase_app_chat.User_Setting.Profile_search_friend;
import com.example.firebase_app_chat.User_Setting.Profile_user;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class fragment_user extends Fragment {
    View view;
    LinearLayout linearlayout_Profile,linearlayout_security,linearlayout_privacy,linearlayout_logout;
    LinearLayout linearlayout_display,linearlayout_notification,linearlayout_info;
    ImageView fragment_user_avt;
    TextView fragment_user_Username;
    FirebaseFirestore firestore;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user,container,false);
        linearlayout_Profile=(LinearLayout) view.findViewById(R.id.linearlayout_Profile);
        linearlayout_security=(LinearLayout) view.findViewById(R.id.linearlayout_security);
        linearlayout_privacy=(LinearLayout) view.findViewById(R.id.linearlayout_privacy);
        linearlayout_display=(LinearLayout) view.findViewById(R.id.linearlayout_display);
        linearlayout_notification=(LinearLayout) view.findViewById(R.id.linearlayout_notification);
        linearlayout_info=(LinearLayout) view.findViewById(R.id.linearlayout_info);
        linearlayout_logout=(LinearLayout) view.findViewById(R.id.linearlayout_logout);
        fragment_user_avt=(ImageView) view.findViewById(R.id.fragment_user_avt);
        fragment_user_Username=(TextView) view.findViewById(R.id.fragment_user_Username);
        firestore=FirebaseFirestore.getInstance();
        load_user();
        linearlayout_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(), Profile_user.class);
                startActivity(intent);
            }
        });

        linearlayout_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "chưa làm", Toast.LENGTH_SHORT).show();
            }
        });
        linearlayout_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "chưa làm", Toast.LENGTH_SHORT).show();
            }
        });
        linearlayout_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Dark_mode.class);
                startActivity(intent);
            }
        });
        linearlayout_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "chưa làm", Toast.LENGTH_SHORT).show();
            }
        });
        linearlayout_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "chưa làm", Toast.LENGTH_SHORT).show();
            }
        });
        linearlayout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearlayout_Profile.setBackgroundColor(Color.parseColor("#F1F1F1"));
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    private void load_user() {
        DocumentReference documentReference= firestore.collection("user").document(Main_FireChat.uid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()){
                    String avt_user=value.getString("avt");
                    String name_user=value.getString("yourname");
                    Glide.with(getActivity()).load(avt_user).into(fragment_user_avt);
                    fragment_user_Username.setText(name_user);
                }
            }
        });
    }
}
