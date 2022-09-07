package com.example.findingidealtypeapp.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findingidealtypeapp.R;

public class DetailImageActivity extends AppCompatActivity {

    User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        getSelectedUser();

        setOriginalImage();
    }

    private void setOriginalImage() {
        TextView textView = findViewById(R.id.user_detail_name);
        ImageView imageView = findViewById(R.id.user_detail_image);

        textView.setText(selectedUser.getName());
        imageView.setImageResource(selectedUser.getImage());
    }

    private void getSelectedUser() {

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        selectedUser = MainPage.userList.get(Integer.valueOf(name));
    }
}