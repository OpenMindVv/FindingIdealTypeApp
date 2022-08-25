package com.example.findingidealtypeapp.userService;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class idSearchActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button searchPasswordButton;
    private UserService userService;
    private String email;
    private Retrofit retrofit;

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
            email = inputEmail.getText().toString();
            setRetrofit();
            getUserPassword();

            System.out.println("비밀번호 찾기 로직 여기에 넣어야함"); // 로직 만들면 될듯
        }
    });
}
    private void getUserPassword() {

        Call<MyPageResponse> call = userService.getProfile(email);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.

                if(result != null){
                    System.out.println(result.getPassword());
                    dialog(result.getPassword());
                }
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
    }
    private void setRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                //.client(okHttpClient)
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    public void loginValidateCheck(){
        if ( inputEmail.getText().toString().length() == 0 ) {
            Toast.makeText(getApplicationContext(), "Email을 입력하세요", Toast.LENGTH_SHORT).show();
            inputEmail.requestFocus();
            return;
        }
    }

    public void dialog(String password){
        AlertDialog.Builder menu = new AlertDialog.Builder(idSearchActivity.this);
        menu.setIcon(R.mipmap.ic_launcher);
        menu.setTitle("비밀번호 안내"); // 제목
        menu.setMessage("비밀번호는 "+ "'"+password+"'" + " 입니다"); // 문구

        menu.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog 제거
                dialog.dismiss();
            }
        });
        menu.show();
    }

}