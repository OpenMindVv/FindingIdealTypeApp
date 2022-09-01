package com.example.findingidealtypeapp.chattingroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.utility.Constants;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.BufferedSource;
import okio.ByteString;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;


public class ChatRoomActivity extends AppCompatActivity {

    private Adapter adapter;
    private RecyclerView recyclerView;
    private EditText input;  //message

    private String userId;
    private OkHttpClient client;
    private WebSocket webSocket;
    private Request request;
    private Handler handler;

    private WebSocketListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        adapter = new Adapter();
        handler = new Handler();

        setWebSocket();

        //입력 창
        input = findViewById(R.id.editText_input);

        //대화상자를 보여주는 뷰
        recyclerView = findViewById(R.id.recyclerview_chat_data);
        recyclerView.setAdapter(adapter);

        //답장 보내기 버튼
        ImageButton btnInput = (ImageButton) findViewById(R.id.btn_send);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendText();
            }
        });

        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    sendText();
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

    private void setWebSocket(){
        client = new OkHttpClient();
        request = new Request.Builder()
                .url("ws://192.168.0.7:8080/websocket")
                .build();

        setWebSocketListener();
        webSocket = client.newWebSocket(request, listener);
    }

    private void setWebSocketListener(){

        listener = new WebSocketListener() {

            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosed(webSocket, code, reason);
                Log.d("TLOG","소켓 onClosing: " + reason);
            }

            @Override
            public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d("TLOG","소켓 onClosing");
                webSocket.close(1000, null);
                webSocket.cancel();
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @NotNull Response response) {
                super.onFailure(webSocket, t, response);
                Log.d("TLOG","소켓 onFailure : " + t.toString());
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                super.onMessage(webSocket, text);
                Log.d("TLOG", "상대 : " + text.toString());
                reflectChatContents(text, Constants.LEFT_CONTENT);
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
                Log.d("TLOG", "ByteString 데이터 확인 : " + bytes.toString());
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                super.onOpen(webSocket, response);
                Log.d("TLOG", "전송 데이터 확인 : " + webSocket + " : " + response);
                //webSocket.close(1000, null);
            }

        };
    }

    private void sendToServer(String inputText){
        try {
            webSocket.send(inputText);
            client.dispatcher().executorService().shutdown();
        }
        catch (Exception e){
            Log.e("Exception", "MAIN 소켓 통신 오류 : " + e.toString());
        }
    }

    private void reflectChatContents(String text, int contentType){
        //입력값 화면에 표시
        adapter.setChattingData(new ChattingData(
                userId, text,
                getCurrentTime(), contentType));

        handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemInserted(adapter.getItemCount());
                //입력 후 최하단으로 이동
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private void sendText(){
        String inputText = input.getText().toString();  //메세지 입력값

        if(inputText.trim().equals("")){
            return;     //빈값 or 공백만 입력시 전송 불가
        }

        sendToServer(inputText);

        reflectChatContents(inputText, Constants.RIGHT_CONTENT);

        input.setText(""); //입력창 초기화
    }

    //채팅 대화상자 옆에 현재 시각을 표시하기 위해 데이터 저장
    private String getCurrentTime(){

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd aa hh:mm");

        return dateFormat.format(date);
    }

    private void initData(){

        adapter.setChattingData(new ChattingData(
                "귤귤111", "안녕하세요 반가워요!",
                getCurrentTime(), Constants.LEFT_CONTENT));

        adapter.setChattingData(new ChattingData(
                "귤귤222", "반가워요ㅎㅎ",
                getCurrentTime(), Constants.RIGHT_CONTENT));
    }

}
