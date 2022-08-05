package com.example.findingidealtypeapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinPage extends Fragment {

    private EditText inputEmailOfJoin, inputName, inputPasswordOfJoin, inputPasswordCheck;
    private Button JoinButton;
    private ViewGroup rootView;
    private boolean isPassJoin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.join_page, container, false);
        inputEmailOfJoin = rootView.findViewById(R.id.inputEmailOfJoin); //
        inputName = rootView.findViewById(R.id.inputName);
        inputPasswordOfJoin = rootView.findViewById(R.id.inputPasswordOfJoin);
        inputPasswordCheck= rootView.findViewById(R.id.inputPasswordCheck);
        JoinButton = rootView.findViewById(R.id.joinButton);


        JoinButton.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼 누를 때 이벤트
            @Override
            public void onClick(View v) {
                if(!emailValidateCheck()) return;
                if(!nameValidateCheck()) return;
                if(!passwordValidateCheck()) return;
                System.out.println("가입완료"); // 로직 만들면 될듯
            }
        });
        return rootView;
    }


        private boolean emailValidateCheck(){
        if ( inputEmailOfJoin.getText().toString().length() == 0 ) { // 아무 입력없이 회원가입 버튼 눌렀을 때
            Toast.makeText(rootView.getContext(), "Email 입력하세요", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        else if(!IsEmailPass()){ // 이메일 형식이 맞지 않을 때
            Toast.makeText(rootView.getContext(), "이메일 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        else if(inputEmailOfJoin.getText().toString().equals("iopp3423@gmail.com")){ // 여기에 이메일 중복체크
            Toast.makeText(rootView.getContext(), "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        return true;
    }

    private boolean nameValidateCheck(){
        if (inputName.getText().toString().length() == 0 ) {
            Toast.makeText(rootView.getContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            inputName.requestFocus();
            return false;
        }
        else if(!IsNamePass()){
            Toast.makeText(rootView.getContext(), "한글만 입력해주세요", Toast.LENGTH_SHORT).show();
            inputName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean passwordValidateCheck(){
        if ( inputPasswordOfJoin.getText().toString().length() == 0 ) {
            Toast.makeText(rootView.getContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            inputPasswordOfJoin.requestFocus();
            return false;
        }
        else if(!IsPasswordPass()){
            Toast.makeText(rootView.getContext(), "비밀번호 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show();
            inputPasswordOfJoin.requestFocus();
            return false;
        }
        if (inputPasswordCheck.getText().toString().length() == 0 ) {
            Toast.makeText(rootView.getContext(), "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            inputPasswordCheck.requestFocus();
            return false;
        }
        else if(!inputPasswordOfJoin.getText().toString().equals(inputPasswordCheck.getText().toString())){
            Toast.makeText(rootView.getContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            System.out.println(inputPasswordOfJoin.getText().toString());
            System.out.println(inputPasswordCheck.getText().toString());
            inputPasswordCheck.requestFocus();
            return false;
        }
        return true;
    }

    private boolean IsEmailPass(){
        String regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        if(Pattern.matches(regex, inputEmailOfJoin.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean IsNamePass(){
        String regex = "^[가-힣]*$";
        if(Pattern.matches(regex, inputName.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean IsPasswordPass(){
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,15}$";

        if(Pattern.matches(regex, inputPasswordOfJoin.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }


}
