package com.example.findingidealtypeapp;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginPage extends Fragment {
    
    private EditText inputEmail, inputPassword;
    private Button loginButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.login_page, container, false);
        inputEmail = rootView.findViewById(R.id.inputEmailAddress);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        loginButton = rootView.findViewById(R.id.loginButton);

        //로그인 버튼 눌렀을 때 수행
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = inputEmail.getText().toString();
                String userPassword = inputPassword.getText().toString();

                System.out.println(userEmail);
                System.out.println(userPassword);
                Log.d("test", "test:" + userEmail);
            }
        });
        return rootView;
    }
}
