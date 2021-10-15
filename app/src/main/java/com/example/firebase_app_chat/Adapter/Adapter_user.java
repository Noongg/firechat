package com.example.firebase_app_chat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebase_app_chat.Models.UserDb;
import com.example.firebase_app_chat.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Adapter_user extends FirestoreRecyclerAdapter<UserDb, Adapter_user.UserviewHolder> {
    Context context;
    OnItemClickListenner listenner;

    public Adapter_user(@NonNull FirestoreRecyclerOptions<UserDb> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserviewHolder holder, int position, @NonNull UserDb model) {
        String uidFriend=getSnapshots().getSnapshot(position).getId();
        holder.setList(uidFriend);
    }

    @NonNull
    @Override
    public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend,parent,false);
        return new UserviewHolder(view);
    }

    public class UserviewHolder extends RecyclerView.ViewHolder {
        ImageView item_avt_friend;
        TextView item_name_friend;
        public UserviewHolder(@NonNull View itemView) {
            super(itemView);
            item_avt_friend=(ImageView) itemView.findViewById(R.id.item_avt_friend);
            item_name_friend=(TextView) itemView.findViewById(R.id.item_name_friend);
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
        public void setList(String uidFriend){
            FirebaseFirestore firestore= FirebaseFirestore.getInstance();
            firestore.collection("user").document(uidFriend)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value.exists()){
                                String avt=value.getString("avt");
                                Glide.with(context).load(avt).into(item_avt_friend);

                                String name=value.getString("yourname");
                                item_name_friend.setText(name);
                            }
                        }
                    });
        }
    }
    public interface OnItemClickListenner{
        void onItemClick(DocumentSnapshot snapshot,int position);
    }
    public void setOnItemClick(OnItemClickListenner listenner){
        this.listenner=listenner;
    }

    public void release(){
        context=null;
    }
}
