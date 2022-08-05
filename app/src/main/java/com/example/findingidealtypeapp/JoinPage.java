package com.example.findingidealtypeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class JoinPage extends Fragment {

    private EditText inputEmail, inputName, inputPassword, inputPasswordCheck;
    private Button JoinButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.join_page, container, false);

        inputEmail = rootView.findViewById(R.id.inputEmail); //
        inputName = rootView.findViewById(R.id.inputName);
        inputPassword = rootView.findViewById(R.id.inputPasswordOfJoin);
        inputPasswordCheck= rootView.findViewById(R.id.inputPasswordCheck);

        JoinButton.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼 누를 때 이벤트
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
