package com.example.findingidealtypeapp.chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.findingidealtypeapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (this, RecyclerView.VERTICAL, false)) ;

        Adapter adapter = new Adapter(getApplicationContext());

        for (int number = 1; number <= 5; number++) {
            ChatInformation example = new ChatInformation("밍귤" + number, "안녕하세요!", "오후 6:57");
            adapter.setChatInformations(example);
        }

        recyclerView.setAdapter(adapter);
    }
}