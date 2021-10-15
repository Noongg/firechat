package com.example.firebase_app_chat.User_Setting;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Main_app.Chat_Activity;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Profile_person extends AppCompatActivity {
    TextView profile_user_search_story,profile_user_search_userName,profile_user_search_txtadd;
    ImageView profile_user_search_back,profile_user_search_img_cover,profile_user_search_avt,profile_user_search_imgadd;
    LinearLayout user_search_bar,linearLayout_add_friend,linearLayout_chat,linearLayout_block;
    FirebaseFirestore firestore;
    String uid_person;
    String uid_user;
    CollectionReference reference,getReference;
    String ischeck="not_friend";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_person);
        uid_person=getIntent().getStringExtra("uid_person");
        firestore=FirebaseFirestore.getInstance();
        uid_user=Main_FireChat.uid;
        reference=firestore.collection("user").document(uid_user)
                .collection("friend");
        getReference=firestore.collection("user").document(uid_person)
                .collection("friend");
        init();
        load_person();
        load_ischeckfriend();
        linearLayout_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_person.this, Chat_Activity.class);
                intent.putExtra("uid_person",uid_person);
                startActivity(intent);
            }
        });
        linearLayout_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ischeck.equals("not_friend")){
                    Map<String,Object> ischeckfriend=new HashMap<>();
                    ischeckfriend.put("ischeck","pending");
                    ischeckfriend.put("seen","true");
                    reference.document(uid_person).set(ischeckfriend)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    ischeck="pending";
                                }
                            });
                    Map<String,Object> ischeck_person=new HashMap<>();
                    ischeck_person.put("ischeck","waiting");
                    ischeck_person.put("seen","false");
                    getReference.document(uid_user).set(ischeck_person)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                }
                if (ischeck.equals("pending")){
                    Map<String,Object> ischeckfriend=new HashMap<>();
                    ischeckfriend.put("ischeck","not_friend");
                    ischeckfriend.put("seen","true");
                    reference.document(uid_person).set(ischeckfriend)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    ischeck="not_friend";
                                    profile_user_search_txtadd.setText("Kết bạn");
                                    profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                }
                            });
                    Map<String,Object> ischeckperson=new HashMap<>();
                    ischeckperson.put("ischeck","not_friend");
                    ischeckperson.put("seen","true");
                    getReference.document(uid_user).set(ischeckperson)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                }if (ischeck.equals("waiting")){
                    View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
                    LinearLayout bottom_add=(LinearLayout) view.findViewById(R.id.bottom_add);
                    LinearLayout bottom_canner=(LinearLayout) view.findViewById(R.id.bottom_canner);

                    final Dialog mBottomSheetDialog = new Dialog(Profile_person.this, R.style.MaterialDialogSheet);
                    mBottomSheetDialog.setContentView(view);
                    mBottomSheetDialog.setCancelable(true);
                    mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    mBottomSheetDialog.show();

                    bottom_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetDialog.dismiss();
                            Map<String,Object> ischeckfriend=new HashMap<>();
                            ischeckfriend.put("ischeck","friend");
                            ischeckfriend.put("seen","true");
                            reference.document(uid_person).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ischeck="friend";
                                            profile_user_search_txtadd.setText("Bạn bè");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                    });

                            getReference.document(uid_user).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    });
                    bottom_canner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetDialog.dismiss();
                            Map<String,Object> ischeckfriend=new HashMap<>();
                            ischeckfriend.put("ischeck","not_friend");
                            ischeckfriend.put("seen","true");
                            reference.document(uid_person).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ischeck="not_friend";
                                            profile_user_search_txtadd.setText("Kết bạn");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                    });

                            getReference.document(uid_user).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    });
                }
                if (ischeck.equals("friend")){
                    View view = getLayoutInflater().inflate(R.layout.bottom_sheet_canner, null);
                    LinearLayout bottom_unfriend=(LinearLayout) view.findViewById(R.id.bottom_unfriend);

                    final Dialog mBottomSheetDialog = new Dialog(Profile_person.this, R.style.MaterialDialogSheet);
                    mBottomSheetDialog.setContentView(view);
                    mBottomSheetDialog.setCancelable(true);
                    mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    mBottomSheetDialog.show();
                    bottom_unfriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomSheetDialog.dismiss();
                            Map<String,Object> ischeckfriend=new HashMap<>();
                            ischeckfriend.put("ischeck","not_friend");
                            ischeckfriend.put("seen","true");
                            reference.document(uid_person).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            ischeck="not_friend";
                                            profile_user_search_txtadd.setText("Kết bạn");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                    });

                            getReference.document(uid_user).set(ischeckfriend)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    });
                }
            }
        });
        user_search_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Search_person.class);
                startActivity(intent);
            }
        });
        profile_user_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Search_person.class);
                startActivity(intent);
            }
        });
    }


    private void load_ischeckfriend() {

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    for (DocumentChange change:value.getDocumentChanges()){
                        DocumentSnapshot snapshot=change.getDocument();
                        String friendId = snapshot.getId();
                        if (friendId.equals(uid_person)){
                            reference.document(uid_person).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (value.exists()){
                                        ischeck=value.getString("ischeck");
                                        if (ischeck.equals("friend")){
                                            profile_user_search_txtadd.setText("Bạn bè");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }else if (ischeck.equals("pending")){
                                            linearLayout_add_friend.setLayoutParams(new LinearLayout.LayoutParams(280,80));
                                            profile_user_search_txtadd.setText("Huỷ yêu cầu");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                        else if (ischeck.equals("waiting")){
                                            profile_user_search_txtadd.setText("Trả lời");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                        else if (ischeck.equals("not_friend")){
                                            profile_user_search_txtadd.setText("Kết bạn");
                                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                                        }
                                    }

                                }
                            });
                        }else {

                            profile_user_search_txtadd.setText("Kết bạn");
                            profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                        }
                    }
                }else {

                    profile_user_search_txtadd.setText("Kết bạn");
                    profile_user_search_imgadd.setImageResource(R.drawable.ic_baseline_person_24);
                }
            }
        });
    }


    private void load_person() {
        firestore.collection("user")
                .document(uid_person)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {
                            try {
                                profile_user_search_userName.setText(value.getString("yourname"));
                                profile_user_search_story.setText(value.getString("story"));
                                String avt=value.getString("avt");
                                Glide.with(Profile_person.this).load(avt).into(profile_user_search_avt);
                                String cover=value.getString("cover");
                                Glide.with(Profile_person.this).load(cover).into(profile_user_search_img_cover);
                            }catch (Exception e){

                            }
                        }
                    }
                });
    }


    private void init(){
        profile_user_search_imgadd=(ImageView)findViewById(R.id.profile_user_search_imgadd);
        profile_user_search_txtadd=(TextView)findViewById(R.id.profile_user_search_txtadd);
        profile_user_search_story=(TextView) findViewById(R.id.profile_user_search_story);
        profile_user_search_userName=(TextView) findViewById(R.id.profile_user_search_userName);
        profile_user_search_back=(ImageView) findViewById(R.id.profile_user_search_back);
        profile_user_search_img_cover=(ImageView) findViewById(R.id.profile_user_search_img_cover);
        profile_user_search_avt=(ImageView) findViewById(R.id.profile_user_search_avt);
        user_search_bar=(LinearLayout) findViewById(R.id.user_search_bar);
        linearLayout_add_friend=(LinearLayout) findViewById(R.id.linearLayout_add_friend);
        linearLayout_chat=(LinearLayout) findViewById(R.id.linearLayout_chat);
        linearLayout_block=(LinearLayout) findViewById(R.id.linearLayout_block);
    }
}