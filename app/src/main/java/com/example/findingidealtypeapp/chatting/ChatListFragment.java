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
import android.widget.Toast;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chattingroom.ChatModel;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatListFragment extends Fragment {

    private ViewGroup rootView;
    private Adapter adapter;
    private String myId;
    private String receiverName;
    private FirebaseDatabase firebaseDatabase;
    private Handler handler = new Handler();
    private Retrofit retrofit;
    private UserService userService;

    private RecyclerView recyclerView;
    private TextView txNoChattingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_chat_list, container, false);

        setRetrofit();

        //?????? ????????? ???????????? view
        recyclerView = rootView.findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), RecyclerView.VERTICAL, false)) ;

        //??????????????? ?????? ?????? '?????? ??? ??????' ????????? ?????????
        txNoChattingList = rootView.findViewById(R.id.no_chatting_list);

        adapter = new Adapter(getActivity());
        recyclerView.setAdapter(adapter);

        setMyId();

        return rootView;
    }

    private void setChattingListView(RecyclerView recyclerView, TextView txNoChattingList){
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
                            receiverId = changeIdToFirebaseFormat(receiverId);

                            if(chatModel.users.containsKey(receiverId)) {
                                chatRoomId = dataSnapshot.getKey();
                                chatRoom = new ChatRoom("", chatRoomId, myId, receiverId,
                                        "","", "");
                                adapter.addChatRoom(chatRoom);

                                int index = adapter.getItemCount() - 1;
                                setReceiverName(receiverId, adapter.chatRoomList.get(index));
                                System.out.println(chatRoom.getProfileImage());
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
    }

    private void setRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }

    private String changeIdToFirebaseFormat(String id){
        id = id.replace("@", "-");
        id = id.replace(".", "-");

        return id;
    }

    private void setMyId(){
        System.out.println(TokenDTO.Token);
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                if(result != null){
                    myId = changeIdToFirebaseFormat(result.getEmail());

                    setChattingListView(recyclerView, txNoChattingList);
                }
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println("????????????");
            }
        });
    }

    private String getEmail(String email){
        StringBuilder builder = new StringBuilder(email);
        int index;

        index = email.indexOf("-");
        builder.setCharAt(index, '@');

        index = email.lastIndexOf("-");
        builder.setCharAt(index, '.');

        return builder.toString();
    }

    private void setReceiverName(String email, ChatRoom chatRoom){
        Call<MyPageResponse> call = userService.getName(getEmail(email));

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                if(result != null){
                    receiverName = result.getName();
                    chatRoom.setReceiverName(receiverName);
                    chatRoom.setProfileImage(result.getImage());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }else{     // ????????? ??????
                    System.out.println("????????? ????????? ??????????????? ????????? ??????????????????.");
                    System.out.println(result);
                }
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println("????????????");
                System.out.println(t);
            }
        });
    }

    private String getDate(String date){
        date = date.replace("PM", "??????");
        date = date.replace("AM", "??????");

        return date;
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
                        chatRoom.setDate(getDate(comment.getDate()));

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

}