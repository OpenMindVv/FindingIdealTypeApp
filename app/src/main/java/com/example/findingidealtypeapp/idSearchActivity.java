package com.example.findingidealtypeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class idSearchActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button searchPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_search);
        inputEmail = findViewById(R.id.inputEmail);
        searchPasswordButton = findViewById(R.id.searchPasswordButton);


    searchPasswordButton.setOnClickListener(new View.OnClickListener() { // 비밀번호 찾기 버튼 누를 때 이벤트
        @Override
        public void onClick(View v) {
            loginValidateCheck();
            System.out.println("비밀번호 찾기 로직 여기에 넣어야함"); // 로직 만들면 될듯
        }
    });
}

    public void loginValidateCheck(){
        if ( inputEmail.getText().toString().length() == 0 ) {
            Toast.makeText(getApplicationContext(), "Email을 입력하세요", Toast.LENGTH_SHORT).show();
            inputEmail.requestFocus();
            return;
        }
    }
}