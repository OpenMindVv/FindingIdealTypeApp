package com.example.findingidealtypeapp.chatting;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chattingroom.ChatModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;

public class ChatListFragment extends Fragment {

    private ViewGroup rootView;
    private Adapter adapter;
    private String myId = "you";
    private FirebaseDatabase firebaseDatabase;
    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_chat_list, container, false);

        //채팅 목록을 보여주는 view
        RecyclerView recyclerView = rootView.findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), RecyclerView.VERTICAL, false)) ;

        //채팅목록이 없을 경우 '채팅 방 없음' 문구를 보여줌
        TextView txNoChattingList = rootView.findViewById(R.id.no_chatting_list);

        adapter = new Adapter(getActivity());
        recyclerView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseDatabase.getReference().child("chatrooms")
                .orderByChild("users/" + myId)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String chatRoomId;
                        String receiverId;
                        ChatRoom chatRoom;

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                            Iterator<String> keys = chatModel.users.keySet().iterator();

                            receiverId = keys.next();
                            receiverId = receiverId.equals(myId) ? keys.next() : receiverId;

                            if(chatModel.users.containsKey(receiverId)) {
                                chatRoomId = dataSnapshot.getKey();
                                chatRoom = new ChatRoom(chatRoomId, myId, receiverId,
                                        "", "");
                                adapter.addChatRoom(chatRoom);
                            }
                        }

                        for(int index = 0; index < adapter.getItemCount(); index++){
                            setLastMessageComment(adapter.chatRoomList.get(index));
                        }

                        setChattingList(recyclerView, txNoChattingList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return rootView;
    }

    private void setLastMessageComment(ChatRoom chatRoom){
        firebaseDatabase.getReference().child("chatrooms")
                .child(chatRoom.getChatRoomId()).child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ChatModel.Comment comment = new ChatModel.Comment();

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            comment = dataSnapshot.getValue(ChatModel.Comment.class);
                        }

                        chatRoom.setLastMessage(comment.getMessage());
                        chatRoom.setDate(comment.getDate());

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setChattingList(RecyclerView recyclerView, TextView txNoChattingList){
        recyclerView.setAdapter(adapter);

        if(adapter.getItemCount() == 0){
            recyclerView.setVisibility(View.GONE);
            txNoChattingList.setVisibility(View.VISIBLE);
        }
    }

    private String getFormattedDate(String formate){

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat(formate);//"yyyy-MM-dd aa hh:mm");

        return dateFormat.format(date);
    }
}