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


        //????????? ?????? ????????? ??? ??????
        loginButton.setOnClickListener(new View.OnClickListener() { // ????????? ?????? ????????? ??? ?????????
            @Override
            public void onClick(View v) {
                loginValidateCheck(); // ?????????, ???????????? ???????????????
                setRetrofit();
                login();
            }
        });

        //?????????????????? ?????? ????????? ??? ??????
        joinText.setOnClickListener(new View.OnClickListener() {// ????????? ?????? ????????? ??? ?????????
            @Override
            public void onClick(View v) {
                // ??????(???????????????)??? ????????? ?????? ????????????
                activity = (MainActivity) getActivity();
                // ??????????????? ?????????????????? ???????????? ????????? ???????????? ??????
                activity.onFragmentChange(0);
            }
        });

        textSearchPassowrd.setOnClickListener(new View.OnClickListener() {// ???????????? ?????? ????????? ??? ?????????
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), idSearchActivity.class);
                startActivity(intent);
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {// ??????????????? ????????? ??? ?????????
            @Override
            public void onClick(View v) {
                //googleSignIn();
            }
        });

        return rootView;
    }

   public void loginValidateCheck(){
       if (inputEmail.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "Email ???????????????", Toast.LENGTH_SHORT).show();
           inputEmail.requestFocus();
           return;
       }

       if ( inputPassword.getText().toString().length() == 0 ) {
           Toast.makeText(rootView.getContext(), "??????????????? ???????????????", Toast.LENGTH_SHORT).show();
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
                String result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                System.out.println(result);
                if(result != null){ // ????????? ???????????? ????????? ????????? ????????? ?????? --> ?????????
                    // ?????????????????? ????????? ??????
                    transData();
                    inputEmail.setText("");
                    inputPassword.setText("");

                    //?????? ??????
                    System.out.println("sucess");
                    TokenDTO.Token = response.body();

                }else{     // ????????? ??????
                    Toast.makeText(rootView.getContext(), "??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    System.out.println("fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println(t);
            }
        });
    }

    private void transData(){
        Bundle bundle = new Bundle(); // ????????? ?????? ??? ??????
        bundle.putString("email",inputEmail.getText().toString());//????????? ?????? ??? ??????
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();//???????????????2 ??????
        profileFragment.setArguments(bundle);//????????? ???????????????2??? ?????? ??????
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
                .baseUrl(Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }
}
