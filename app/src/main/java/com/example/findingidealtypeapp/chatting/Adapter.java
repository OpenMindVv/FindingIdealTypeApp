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
import com.example.findingidealtypeapp.utility.DataProcessing;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<ChatRoom> chatRoomList;
    private Context context;
    private DataProcessing processing = new DataProcessing();

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

        if(!chatRoom.getProfileImage().equals("0")) {
            byte[] Image = processing.binaryStringToByteArray(chatRoom.getProfileImage());
            viewHolder.profileImage.setImageBitmap(processing.byteArrayToBitmap(Image)); // 프로필 이미지 비트맵으로 가져와서 저장
        }
        else viewHolder.profileImage.setImageResource(R.drawable.profile_image);

        viewHolder.userId.setText(chatRoom.getReceiverName());
        viewHolder.content.setText(chatRoom.getLastMessage());
        viewHolder.date.setText(chatRoom.getDate());

        viewHolder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
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
