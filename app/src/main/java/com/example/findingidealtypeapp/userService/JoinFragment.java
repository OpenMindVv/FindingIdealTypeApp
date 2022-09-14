package com.example.findingidealtypeapp.userService;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.utility.Constants;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinFragment extends Fragment {

    private EditText inputEmailOfJoin, inputName, inputPasswordOfJoin, inputPasswordCheck;
    private Button JoinButton;
    private ViewGroup rootView;
    private TextView loginText;
    private UserService userService;
    private MainActivity activity;
    private Retrofit retrofit;
    private boolean isPassJoin;
    private String follow="0", following="0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.join_fragment, container, false);
        inputEmailOfJoin = rootView.findViewById(R.id.inputEmailOfJoin); //
        inputName = rootView.findViewById(R.id.inputName);
        inputPasswordOfJoin = rootView.findViewById(R.id.inputPasswordOfJoin);
        inputPasswordCheck= rootView.findViewById(R.id.inputPasswordCheck);
        JoinButton = rootView.findViewById(R.id.joinButton);
        loginText = rootView.findViewById(R.id.loginText);


        JoinButton.setOnClickListener(new View.OnClickListener() { // 회원가입 버튼 누를 때 이벤트
            @Override
            public void onClick(View v) {
                if(!emailValidateCheck()) return;
                //if(!nameValidateCheck()) return;
                if(!passwordValidateCheck()) return;
                setRetrofit();
                join();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() { // 로그인 버튼 누를 때 이벤트
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.onFragmentChange(1); // 로그인 fragment
            }
        });

        return rootView;
    }

    private void join() {

        String email = inputEmailOfJoin.getText().toString();
        String name = inputName.getText().toString();
        String password = inputPasswordOfJoin.getText().toString();

        MyPageResponse myPageResponse = new MyPageResponse("0", email, password, name, "0", "0", "0");

        Call<String> call = userService.createProfile(myPageResponse.getImage(), myPageResponse.getEmail(), myPageResponse.getPassword(), myPageResponse.getName(), myPageResponse.getFollowing(), myPageResponse.getFollowing(), myPageResponse.getAnimalFace());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                if(result==null) result = "0";// 이미 존재하는 아이디로 로그인 에러 막아줌
                if(result.equals("1")){ // 여기에 서버에서 받아온 값으로 로그인 판단 --> 회원가입
                    // 액티비티에 플래그먼트를 변경하는 메소드 구현하여 호출
                    Toast.makeText(rootView.getContext(), "회원가입이 완료되었습니다. 로그인 해주세요", Toast.LENGTH_SHORT).show();
                    activity = (MainActivity) getActivity();
                    activity.onFragmentChange(Constants.LOGIN_PAGE);
                }else{     // 회원가입 실패
                    Toast.makeText(rootView.getContext(), "이미 존재하는 이메일입니다.", Toast.LENGTH_SHORT).show();
                    System.out.println("fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    private void setRetrofit() {
        retrofit = new Retrofit.Builder()
                //.baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
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
