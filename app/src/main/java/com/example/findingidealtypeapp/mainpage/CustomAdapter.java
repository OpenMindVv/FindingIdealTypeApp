package com.example.findingidealtypeapp.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chatting.ChatRoom;
import com.example.findingidealtypeapp.chattingroom.ChatModel;
import com.example.findingidealtypeapp.chattingroom.ChatRoomActivity;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
    private Context context;
    private List list;
    private Retrofit retrofit;
    private UserService userService;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView iv_thumb;
        public ImageButton message_button;
    }

    public CustomAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        setRetrofit();

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.item_user_list, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_name);
        viewHolder.iv_thumb = (ImageView) convertView.findViewById(R.id.imageView_thumb);
        viewHolder.message_button = (ImageButton) convertView.findViewById(R.id.message_button);

        final MyPageResponse user = (MyPageResponse) list.get(position);

        viewHolder.tv_name.setText(user.getName());
        Glide
                .with(context)
                .load(user.getImage())
                .centerCrop()
                .apply(new RequestOptions().override(250, 350))
                .into(viewHolder.iv_thumb);
        viewHolder.tv_name.setTag(user.getName());


//        //아이템 클릭 방법2 - 클릭시 아이템 반전 효과가 안 먹힘
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, " " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //1:1채팅 보내기
        viewHolder.message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChatRoom(user.getEmail());
            }
        });

        //Return the completed view to render on screen
        return convertView;
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

    private void checkChatRoom(String myId, String receiverId){
        FirebaseDatabase.getInstance().getReference().child("chatrooms")
                .orderByChild("users/" + myId)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String chatRoomId = null;

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                            //상대방 id가 포함되어있는 채팅방 key를 가져옴
                            if(chatModel.users.containsKey(receiverId)){
                                chatRoomId = dataSnapshot.getKey();
                            }
                        }

                        ChatRoom chatRoom = new ChatRoom(chatRoomId, myId, receiverId,
                                "","", "");

                        Intent intent = new Intent(context, ChatRoomActivity.class);
                        intent.putExtra("chatRoom", chatRoom);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setChatRoom(String receiverId){
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                ChatRoom chatRoom;
                String myId;

                if(result != null){
                    myId = result.getEmail();
                    myId = myId.replace("@", "-");
                    myId = myId.replace(".", "-");

                    checkChatRoom(myId, receiverId);
                }
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
            }
        });
    }
}
