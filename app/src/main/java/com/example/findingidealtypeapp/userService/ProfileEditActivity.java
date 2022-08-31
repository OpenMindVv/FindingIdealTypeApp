package com.example.findingidealtypeapp.userService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileEditActivity extends AppCompatActivity {

    private TextView proFileEditText, cancelText, finishText;
    private EditText emailEditText, nameEditText, passwordEditText;
    private Retrofit retrofit;
    private UserService userService;
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        proFileEditText = findViewById(R.id.editProfileText);
        emailEditText = findViewById(R.id.profile_edit_email);
        nameEditText = findViewById(R.id.profile_edit_name);
        passwordEditText = findViewById(R.id.profile_edit_password);
        cancelText = findViewById(R.id.cancelText);
        finishText = findViewById(R.id.finishText);


        setRetrofit();
        getUserProfile();// 유저 정보 가져오기

        finishText.setOnClickListener(new View.OnClickListener() { // 완료텍스트 누를 때 이벤트
            @Override
            public void onClick(View v) {
                setRetrofit();
                editFinishProfile();
                finish();
                mainActivity.onFragmentChange(Constants.PROFILE_PAGE); // 수정완료하면 마이페이지로 가는데
                // 현재 수정된 화면이 안되어서 나중에 수정해야할듯 다른 페이지를 보여주던지 해서

            }
        });
        cancelText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    System.out.println("취소");
                    finish();
                }
        });
    }

    private void editFinishProfile() {

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Call<String> call = userService.editProrfile(name, email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){ //
                    System.out.println("성공");
                    System.out.println(result);
                    Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                }else{     // 로그인 실패
                    System.out.println("실패");
                    System.out.println(result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
    }

    private void getUserProfile() {
        System.out.println(TokenDTO.Token);
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){
                    nameEditText.setText(result.getName());
                    emailEditText.setText(result.getEmail());
                    passwordEditText.setText(result.getPassword());
                }
                System.out.println("call= "+call);
                System.out.println("result= "+result);
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
                System.out.println(call);
            }
        });
    }

    private void setRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                //.client(okHttpClient)
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }
}