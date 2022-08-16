package com.example.findingidealtypeapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.findingidealtypeapp.apiService.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private TextView joinText;
    private ViewGroup rootView;
    private Retrofit retrofit;
    private ApiService apiService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.login_fragment, container, false);
        inputEmail = rootView.findViewById(R.id.inputEmailAddress);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        loginButton = rootView.findViewById(R.id.loginButton);
        joinText = rootView.findViewById(R.id.JoinText);

        //로그인 버튼 눌렀을 때 수행
        loginButton.setOnClickListener(new View.OnClickListener() { // 로그인 버튼 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                loginValidateCheck(); // 이메일, 비밀번호 유효성검사
                setRetrofit();
                login();
            }
        });

        //회원가입으로 가기 눌렀을 때 수행
        joinText.setOnClickListener(new View.OnClickListener() {// 로그인 버튼 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), JoinActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

   public void loginValidateCheck(){
       if ( inputEmail.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "Email 입력하세요", Toast.LENGTH_SHORT).show();
           inputEmail.requestFocus();
           return;
       }

       if ( inputPassword.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
           inputPassword.requestFocus();
           return;
       }
   }

    private void login() {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        Call<String> call = apiService.login(email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String pass = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.

                if(pass == "200"){ // 여기에 서버에서 받아온 값으로 로그인 판단 --> 로그인
                    System.out.println(pass);
                    Intent intent = new Intent(getContext(), JoinActivity.class);
                    startActivity(intent);
                }else{     // 로그인 실패
                    System.out.println("실패");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("음");
            }
        });
    }

    private void setRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
}
