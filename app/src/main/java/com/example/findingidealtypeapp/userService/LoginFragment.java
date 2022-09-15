package com.example.findingidealtypeapp.userService;
import static android.provider.Settings.System.getString;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.loginService.LoginRequest;
import com.example.findingidealtypeapp.userServiceApi.loginService.LoginResponse;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private EditText inputEmail, inputPassword;
    private Button loginButton;
    private TextView joinText, textSearchPassowrd;
    private ViewGroup rootView;
    private Retrofit retrofit;
    private UserService userService;
    private MainActivity activity;
    private ImageView googleLogin;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.login_fragment, container, false);
        inputEmail = rootView.findViewById(R.id.inputEmailAddress);
        inputPassword = rootView.findViewById(R.id.inputPassword);
        loginButton = rootView.findViewById(R.id.loginButton);
        joinText = rootView.findViewById(R.id.JoinText);
        textSearchPassowrd = rootView.findViewById(R.id.textSearchPassowrd);
        googleLogin = rootView.findViewById(R.id.googleLogin);


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
                // 자신(플래그먼트)를 호출할 상위 액티비티
                activity = (MainActivity) getActivity();
                // 액티비티에 플래그먼트를 변경하는 메소드 구현하여 호출
                activity.onFragmentChange(0);
            }
        });

        textSearchPassowrd.setOnClickListener(new View.OnClickListener() {// 비밀번호 찾기 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), idSearchActivity.class);
                startActivity(intent);
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {// 구글로그인 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                //googleSignIn();
            }
        });

        return rootView;
    }

   public void loginValidateCheck(){
       if (inputEmail.getText().toString().length() == 0 ) {
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

        //LoginRequest loginRequest = new LoginRequest(email, password);

        Call<String> call = userService.login(email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){ // 여기에 서버에서 받아온 값으로 로그인 판단 --> 로그인
                    // 프래그먼트에 데이터 전달
                    transData();
                    inputEmail.setText("");
                    inputPassword.setText("");

                    //토큰 저장
                    System.out.println("sucess");
                    TokenDTO.Token = response.body();

                }else{     // 로그인 실패
                    Toast.makeText(rootView.getContext(), "회원정보가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println(t);
            }
        });
    }

    private void transData(){
        Bundle bundle = new Bundle(); // 번들을 통해 값 전달
        bundle.putString("email",inputEmail.getText().toString());//번들에 넘길 값 저장
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();//프래그먼트2 선언
        profileFragment.setArguments(bundle);//번들을 프래그먼트2로 보낼 준비
        transaction.replace(R.id.menu_frame_layout, profileFragment);
        transaction.commit();
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
