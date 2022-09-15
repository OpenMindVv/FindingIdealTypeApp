package com.example.findingidealtypeapp.mainpage;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.android.gms.common.util.concurrent.HandlerExecutor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomListFragment extends Fragment {


    private ArrayList<MyPageResponse> userList;
    private ListView customListView;
    private ConstraintLayout todayLayout;
    private TextView todayId;
    private CircleImageView todayImage;

    private static CustomAdapter customAdapter;

    private Retrofit retrofit;
    private UserService userService;
    private Handler handler;
    private Button rabbitButton, dogButton, catButton, dinoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_user_list, container, false);

        rabbitButton =  rootView.findViewById(R.id.rabbit_button);
        dogButton =  rootView.findViewById(R.id.dog_button);
        catButton =  rootView.findViewById(R.id.cat_button);
        dinoButton =  rootView.findViewById(R.id.dinosaur_button);

        userList = new ArrayList<>();
        handler = new Handler();

        //data를 가져와서 어답터와 연결해 준다. 서버에서 가져오는게 대부분 이다.
        customListView = (ListView) rootView.findViewById(R.id.listView_custom);
        customAdapter = new CustomAdapter(getContext(),userList);

        customListView.setAdapter(customAdapter);

        todayLayout = (ConstraintLayout) rootView.findViewById(R.id.today_layout);
        todayId = (TextView) rootView.findViewById(R.id.today_id);
        todayImage = (CircleImageView) rootView.findViewById(R.id.today_image);

        todayId.setText("JENNIERUBYJANE");
        todayImage.setImageResource(R.drawable.jennie);

        todayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickShowAlert(rootView);
            }
        });

        getListOfMembersExceptMe();    //회원목록을 불러옴

        return rootView;
    }

    public void OnClickShowAlert(View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle("오늘의 이상형");
        builder.setMessage("JENNIERUBYJANE" +" 님은 " + "토끼상" + "입니다.");
        builder.setPositiveButton("채팅하기", null);
        builder.setNeutralButton("닫기", null);

        builder.show();
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
    private void getListOfMembersExceptMe(){
        setRetrofit();

        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터
                String myId;

                if(result != null){
                    myId = result.getEmail();
                    setUserList(myId);
                }
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println(t);
            }
        });
    }

    private void setUserList(String myId){

        Call<List<MyPageResponse>> call = userService.getProfileList();

        call.enqueue(new Callback<List<MyPageResponse>>() {
            @Override
            public void onResponse(Call<List<MyPageResponse>> call, Response<List<MyPageResponse>> response) {
                List<MyPageResponse> result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                MyPageResponse user;

                if(result != null){ //
                    for(int index = 0; index < result.size(); index++){
                        user = result.get(index);
                        if(user.getEmail().equals(myId)) continue;

                        userList.add(new MyPageResponse(user.getImage(), user.getEmail(),
                                user.getPassword(), user.getName(), user.getFollowing(),
                                user.getFollowing(), user.getAnimalFace()));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            customAdapter.notifyDataSetChanged();
                        }
                    });

                }else{     // 로그인 실패
                    System.out.println("회원목록을 가져오는데 오류가 발생했습니다.");
                    System.out.println(result);
                }
            }

            @Override
            public void onFailure(Call<List<MyPageResponse>> call, Throwable t) {
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
    }
}
