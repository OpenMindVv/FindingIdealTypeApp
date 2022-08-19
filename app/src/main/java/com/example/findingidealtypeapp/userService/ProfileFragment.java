package com.example.findingidealtypeapp.userService;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.BundleCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.MyPageResponse;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.loginService.LoginRequest;
import com.example.findingidealtypeapp.userServiceApi.loginService.LoginResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private TextView profileName, numberFollow, numberFollowing;
    private ImageButton profileImage;
    private UserService userService;
    private Button profileEditNutton;
    private MainActivity activity;
    private ViewGroup rootView;
    private Retrofit retrofit;
    private String email, name, follow, following;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.profile_page, container, false);
        profileName = rootView.findViewById(R.id.profile_name);
        setRetrofit();
        //createTextView();

        if (getArguments() != null)
        {
            email = getArguments().getString("email"); // 로그인에서 받아온 이메일
            getUserProfile();
        }

        return rootView;
    }

    private void getUserProfile() {

        Call<MyPageResponse> call = userService.getProfile(email);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.

                if(result != null){ // 여기에 서버에서 받아온 값으로 로그인 판단 --> 로그인
                    profileName.setText(result.getName());
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

    private void createTextView(){
        //2. 텍스트뷰에 들어갈 문자설정
        profileName.setText("텍스트 생성");

        //3. 텍스트뷰 글자크기 설정
        //profileName.setTextSize(12);//텍스트 크기
        //4. 텍스트뷰 글자타입 설정
        //profileName.setTypeface(null, Typeface.BOLD);
        //5. 텍스트뷰 ID설정
        //profileName.setId(0);
        //6. 레이아웃 설정
        //LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
        //        ,LinearLayout.LayoutParams.WRAP_CONTENT);
       // param.leftMargin = 30;
        // 7. 설정한 레이아웃 텍스트뷰에 적용
        //profileName.setLayoutParams(param);
        //8. 텍스트뷰 백그라운드색상 설정
        ///profileName.setBackgroundColor(Color.rgb(184,236,184));
    }



}
