package com.example.firebase_app_chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.Main_app.Main_FireChat;
import com.example.firebase_app_chat.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class Adapter_user_search extends FirestoreRecyclerAdapter<UserDb, Adapter_user_search.UserviewHolder> {
    Context context;
    OnItemClickListenner listenner;

    public Adapter_user_search(@NonNull FirestoreRecyclerOptions<UserDb> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserviewHolder holder, int position, @NonNull UserDb model) {
        if (!getSnapshots().getSnapshot(position).getId().equals(Main_FireChat.uid)){
            Glide.with(context).load(model.getAvt()).into(holder.img_user_search);
            holder.txt_user_search.setText(model.getYourname());
        }else {
            holder.img_user_search.setVisibility(View.GONE);
            holder.img_user_search.setLayoutParams(new Constraints.LayoutParams(0,0));
            holder.txt_user_search.setVisibility(View.GONE);
            holder.txt_user_search.setLayoutParams(new Constraints.LayoutParams(0,0));
        }
    }

    @NonNull
    @Override
    public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user,parent,false);
        return new UserviewHolder(view);
    }

    public class UserviewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_search;
        TextView txt_user_search;
        public UserviewHolder(@NonNull View itemView) {
            super(itemView);
            txt_user_search=(TextView) itemView.findViewById(R.id.txt_user_search);
            img_user_search=(ImageView) itemView.findViewById(R.id.img_user_search);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getBindingAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listenner!=null){
                        listenner.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListenner{
        void onItemClick(DocumentSnapshot snapshot, int position);
    }
    public void setOnItemClick(OnItemClickListenner listenner){
        this.listenner=listenner;
    }

    public void release(){
        context=null;
    }
}
