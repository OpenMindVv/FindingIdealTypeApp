package com.example.findingidealtypeapp.chattingroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chatting.ChatRoom;
import com.example.findingidealtypeapp.utility.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatRoomActivity extends AppCompatActivity {

    private Adapter adapter;
    private RecyclerView recyclerView;
    private EditText input;  //message

    private ChatRoom chatRoom;
    private String chatRoomId; //채팅방 id
    private String myId;       //나의 id
    private String receiverId; //상대방 id
    private String receiverName; //상대방 이름
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //입력 창
        input = findViewById(R.id.editText_input);
        //대화상자를 보여주는 뷰
        recyclerView = findViewById(R.id.recyclerview_chat_data);

        initUserInformation();

        adapter = new Adapter(firebaseDatabase, chatRoom,
                recyclerView);
        recyclerView.setAdapter(adapter);

        //답장 보내기 버튼
        ImageButton btnInput = (ImageButton) findViewById(R.id.btn_send);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    sendMessage();
                    return true;
                }

                return false;
            }
        });

        //뒤로가기 버튼 -> 채팅목록으로 돌아감
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initUserInformation(){
        Intent intent = getIntent();
        chatRoom = (ChatRoom) intent.getSerializableExtra("chatRoom");

        myId = chatRoom.getMyId();
        receiverId = chatRoom.getReceiverId();
        chatRoomId = chatRoom.getChatRoomId();
        receiverName = chatRoom.getReceiverName();

        firebaseDatabase = FirebaseDatabase.getInstance();

        checkChatRoom();
    }

    private void sendMessage(){
        ChatModel chatModel = new ChatModel();
        chatModel.users.put(myId, true);
        chatModel.users.put(receiverId, true);

        if(chatRoomId == null){
            Log.v("채팅방 생성 안내", "채팅방 생성");
            firebaseDatabase.getReference()
                    .child("chatrooms")
                    .push()
                    .setValue(chatModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            checkChatRoom();
                        }
                    });
        }
        else{
            sendMessageToDatabase();
        }
    }

    private void checkChatRoom(){
        firebaseDatabase.getReference().child("chatrooms")
                .orderByChild("users/" + myId)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                            //상대방 id가 포함되어있는 채팅방 key를 가져옴
                            if(chatModel.users.containsKey(receiverId)){
                                chatRoomId = dataSnapshot.getKey();

                                sendMessageToDatabase();
                                adapter.getMessageList(chatRoomId);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    //채팅 대화상자 옆에 현재 시각을 표시하기 위해 데이터 저장
    private String getCurrentTime(){

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd aa hh:mm");

        return dateFormat.format(date);
    }

    private void sendMessageToDatabase(){
        String inputText = input.getText().toString();  //메세지 입력값

        if(inputText.trim().equals("")){
            return;     //빈값 or 공백만 입력시 전송 불가
        }

        ChatModel.Comment comment = new ChatModel.Comment();
        comment.uid = myId;
        comment.message = inputText;     //메세지 입력값
        comment.date = getCurrentTime(); //날짜

        firebaseDatabase.getReference()
                .child("chatrooms").child(chatRoomId)
                .child("comments").push().setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        input.setText(""); //입력창 초기화
                    }
                });
    }
}
