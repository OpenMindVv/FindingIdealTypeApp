package com.example.findingidealtypeapp.chattingroom;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ChatRoomActivity extends AppCompatActivity {

    private Adapter adapter;
    private RecyclerView recyclerView;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        adapter = new Adapter();
        initData();

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

    private void sendText(){
        String inputText = input.getText().toString();

        if(inputText.trim().equals("")){
            return;     //빈값 or 공백만 입력시 전송 불가
        }

        //입력값 화면에 표시
        adapter.setChattingData(new ChattingData(
                "귤귤222", inputText,
                getCurrentTime(), Constants.RIGHT_CONTENT));

        adapter.notifyItemInserted(adapter.getItemCount());

        input.setText(""); //입력창 초기화

        //입력 후 최하단으로 이동
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
