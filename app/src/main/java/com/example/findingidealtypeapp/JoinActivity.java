package com.example.findingidealtypeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    private EditText inputEmailOfJoin, inputName, inputPasswordOfJoin, inputPasswordCheck;
    private Button JoinButton;
    private boolean isPassJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        
        inputEmailOfJoin = findViewById(R.id.inputEmailOfJoin); //
        inputName = findViewById(R.id.inputName);
        inputPasswordOfJoin = findViewById(R.id.inputPasswordOfJoin);
        inputPasswordCheck= findViewById(R.id.inputPasswordCheck);
        JoinButton = findViewById(R.id.joinButton);


        JoinButton.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼 누를 때 이벤트
            @Override
            public void onClick(View v) {
                if(!emailValidateCheck()) return;
                if(!nameValidateCheck()) return;
                if(!passwordValidateCheck()) return;
                System.out.println("가입완료"); // 로직 만들면 될듯
            }
        });
    }

    private boolean emailValidateCheck(){
        if ( inputEmailOfJoin.getText().toString().length() == 0 ) { // 아무 입력없이 회원가입 버튼 눌렀을 때
            Toast.makeText(getApplicationContext(), "Email 입력하세요", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        else if(!IsEmailPass()){ // 이메일 형식이 맞지 않을 때
            Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        else if(inputEmailOfJoin.getText().toString().equals("iopp3423@gmail.com")){ // 여기에 이메일 중복체크
            Toast.makeText(getApplicationContext(), "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
            inputEmailOfJoin.requestFocus();
            return false;
        }
        return true;
    }

    private boolean nameValidateCheck(){
        if (inputName.getText().toString().length() == 0 ) {
            Toast.makeText(getApplicationContext(), "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            inputName.requestFocus();
            return false;
        }
        else if(!IsNamePass()){
            Toast.makeText(getApplicationContext(), "한글만 입력해주세요", Toast.LENGTH_SHORT).show();
            inputName.requestFocus();
            return false;
        }
        return true;
    }

    private boolean passwordValidateCheck(){
        if ( inputPasswordOfJoin.getText().toString().length() == 0 ) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            inputPasswordOfJoin.requestFocus();
            return false;
        }
        else if(!IsPasswordPass()){
            Toast.makeText(getApplicationContext(), "비밀번호 형식이 맞지 않습니다", Toast.LENGTH_SHORT).show();
            inputPasswordOfJoin.requestFocus();
            return false;
        }
        if (inputPasswordCheck.getText().toString().length() == 0 ) {
            Toast.makeText(getApplicationContext(), "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show();
            inputPasswordCheck.requestFocus();
            return false;
        }
        else if(!inputPasswordOfJoin.getText().toString().equals(inputPasswordCheck.getText().toString())){
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
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