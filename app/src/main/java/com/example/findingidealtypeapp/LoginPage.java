package com.example.findingidealtypeapp;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginPage extends Fragment {

    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private ViewGroup rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.login_page, container, false);
        inputEmail = rootView.findViewById(R.id.inputEmailAddress);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        loginButton = rootView.findViewById(R.id.loginButton);

        //로그인 버튼 눌렀을 때 수행
        loginButton.setOnClickListener(new View.OnClickListener() { // 로그인 버튼 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                validateCheck(); // 이메일, 비밀번호 유효성검사
                String userEmail = inputEmail.getText().toString();
                String userPassword = inputPassword.getText().toString();


                System.out.println(userEmail);
                System.out.println(userPassword);
                Log.d("test", "test:" + userEmail);
            }
        });
        return rootView;
    }

   public void validateCheck(){
       if ( inputEmail.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "Email 입력하세요", Toast.LENGTH_SHORT).show();
           inputEmail.requestFocus();
           return;
       }

       if ( inputPassword.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "Password 입력하세요", Toast.LENGTH_SHORT).show();
           inputPassword.requestFocus();
           return;
       }
   }
}
