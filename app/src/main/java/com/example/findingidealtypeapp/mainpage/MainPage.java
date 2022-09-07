package com.example.findingidealtypeapp.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.findingidealtypeapp.R;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<User>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        setUpData();

        setUpList();

        //setUpOnClickListener();

    }

    private void  setUpData() {
        User jisoo = new User("김지수", R.drawable.jisoo);
        userList.add(jisoo);

        User jennie = new User("김제니", R.drawable.jennie);
        userList.add(jennie);

        User rose = new User("박채영", R.drawable.rose);
        userList.add(rose);

        User lisa = new User("라리사", R.drawable.lisa);
        userList.add(lisa);

        User kiyong = new User("장기용", R.drawable.kiyong);
        userList.add(kiyong);
    }

    private void setUpList() {
        listView = findViewById(R.id.user_listView);

        UserAdapter adapter = new UserAdapter(getApplicationContext(),0, userList);
        listView.setAdapter(adapter);
    }

    /** 왜 주석 처리 했냐면 모르겠음
    private void setUpOnClickListener() {
        listView.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                User selectedUser = (User) listView.getItemAtPosition(position);
                Intent showDetail = new Intent(getApplicationContext(), DetailImageActivity.class);
                showDetail.putExtra("name", selectedUser.getName());
                startActivity(showDetail);
            }
        });
    }**/
}