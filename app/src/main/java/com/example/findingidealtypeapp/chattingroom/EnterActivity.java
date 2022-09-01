package com.example.findingidealtypeapp.chattingroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.findingidealtypeapp.R;

import okhttp3.OkHttpClient;

public class EnterActivity extends AppCompatActivity {
    EditText editText;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        enterButton = (Button)findViewById(R.id.enterButton);
        editText = (EditText)findViewById(R.id.editText);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatRoomActivity.class);
                String username = editText.getText().toString();
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

    }
}
