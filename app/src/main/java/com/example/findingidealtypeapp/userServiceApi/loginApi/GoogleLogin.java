package com.example.findingidealtypeapp.userServiceApi.loginApi;

import com.example.findingidealtypeapp.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleLogin {
    GoogleSignInClient mGoogleSignInClient;

    // 앱에 필요한 사용자 데이터를 요청하도록 로그인 옵션을 설정한다.
    // DEFAULT_SIGN_IN parameter는 유저의 ID와 기본적인 프로필 정보를 요청하는데 사용된다.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // email addresses도 요청함
            .build();

    // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬

}
