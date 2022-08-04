package com.example.findingidealtypeapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class profilePage extends AppCompatActivity {

    private ListView setList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setList = (ListView)findViewById(R.id.setList);

        List<String> setData = new ArrayList<>();
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,setData);
        setList.setAdapter(setAdapter);

        setData.add("연습");
        setData.add("맞나");
        setData.add("흠");
        setAdapter.notifyDataSetChanged();// 이거 해줘야 저장이 된다.

    }
}
