package com.example.findingidealtypeapp.chatting;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chattingroom.ChatRoomActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<ChatRoom> chatRoomList;
    private Context context;

    public Adapter(Context context){

        chatRoomList = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_chat_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder)holder;

        ChatRoom chatRoom = chatRoomList.get(position);

        viewHolder.userId.setText(chatRoom.getReceiverId());
        viewHolder.content.setText(chatRoom.getLastMessage());
        viewHolder.date.setText(chatRoom.getDate());

        viewHolder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("chatRoom", chatRoom);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();
    }

    public void addChatRoom(ChatRoom chatRoom){
        chatRoomList.add(chatRoom);
    }
}
