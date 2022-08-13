package com.example.findingidealtypeapp.chattingroom;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.utility.Constants;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<ChattingData> chattingDataList;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        initData();

        RecyclerView recyclerView = findViewById(R.id.recyclerview_chat_data);

        adapter = new Adapter(chattingDataList);
        recyclerView.setAdapter(adapter);

        ImageButton btnInput = (ImageButton) findViewById(R.id.btn_send);

        btnInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.editText_input);

                chattingDataList.add(new ChattingData(
                        "귤귤222", input.getText().toString(),
                        "오후 6:58", Constants.RIGHT_CONTENT));

                adapter.notifyDataSetChanged();
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

    private void initData(){

        chattingDataList = new ArrayList<>();

        chattingDataList.add(new ChattingData(
                "귤귤111", "안녕하세요 반가워요!",
                "오후 6:57", Constants.LEFT_CONTENT));

        chattingDataList.add(new ChattingData(
                "귤귤222", "반가워요ㅎㅎ",
                "오후 6:58", Constants.RIGHT_CONTENT));
    }

}
