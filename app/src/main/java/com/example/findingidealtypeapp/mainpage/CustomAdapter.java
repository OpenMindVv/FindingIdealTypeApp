package com.example.findingidealtypeapp.mainpage;

import android.app.AlertDialog;
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
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.DataProcessing;
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
    private Context mContext;
    private DataProcessing processing = new DataProcessing();
    private Retrofit retrofit;
    private UserService userService;
    private String receiverId;
    private String receiverName;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView iv_thumb;
        public TextView tv_animal;

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
        viewHolder.tv_animal = (TextView) convertView.findViewById(R.id.textView_animal);
        viewHolder.message_button = (ImageButton) convertView.findViewById(R.id.message_button);

        final MyPageResponse user = (MyPageResponse) list.get(position);

        viewHolder.tv_name.setText(user.getName());
        viewHolder.tv_animal.setText(user.getAnimalFace());

        byte[] Image = null;
        if(!user.getImage().equals("0")) {
            Image = processing.binaryStringToByteArray(user.getImage()); //????????? ???????????? ????????????
            viewHolder.iv_thumb.setImageBitmap(processing.byteArrayToBitmap(Image)); // ????????? ????????? ??????????????? ???????????? ??????
        }
        else viewHolder.iv_thumb.setImageResource(R.drawable.profile_image);

        Glide
                .with(context)
                .load(user.getImage())
                .centerCrop()
                .apply(new RequestOptions().override(250, 350));
                //.into(viewHolder.iv_thumb);
        viewHolder.tv_name.setTag(user.getName());
        viewHolder.tv_animal.setTag(user.getAnimalFace());


//        //????????? ?????? ??????2 - ????????? ????????? ?????? ????????? ??? ??????
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, " " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //1:1?????? ?????????
        viewHolder.message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiverId = user.getEmail();
                receiverName = user.getName();
                setChatRoom();
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
                .baseUrl(Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }

    private void checkChatRoom(String myId){
        FirebaseDatabase.getInstance().getReference().child("chatrooms")
                .orderByChild("users/" + myId)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String chatRoomId = null;

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                            //????????? id??? ?????????????????? ????????? key??? ?????????
                            if(chatModel.users.containsKey(receiverId)){
                                chatRoomId = dataSnapshot.getKey();
                            }
                        }

                        receiverId = receiverId.replace("@", "-");
                        receiverId = receiverId.replace(".", "-");

                        ChatRoom chatRoom = new ChatRoom("", chatRoomId, myId, receiverId,
                                receiverName ,"", "");

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

    private void setChatRoom(){
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                ChatRoom chatRoom;
                String myId;

                if(result != null){
                    myId = result.getEmail();
                    myId = myId.replace("@", "-");
                    myId = myId.replace(".", "-");

                    checkChatRoom(myId);
                }
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println("????????????");
            }
        });
    }
}
