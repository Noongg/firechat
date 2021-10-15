package com.example.firebase_app_chat.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.firebase_app_chat.Fragment.fragment_people_friend;
import com.example.firebase_app_chat.Fragment.fragment_people_request;
import com.example.firebase_app_chat.Fragment.fragment_people_sent;

public class Adapter_fragment_people extends FragmentStateAdapter {
    public Adapter_fragment_people(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new fragment_people_friend();
            case 1:
                return new fragment_people_request();
            case 2:
                return new fragment_people_sent();
            default:
                return new fragment_people_friend();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
