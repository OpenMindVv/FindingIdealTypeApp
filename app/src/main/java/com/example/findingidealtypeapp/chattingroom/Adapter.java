package com.example.findingidealtypeapp.chattingroom;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chatting.ViewHolder;
import com.example.findingidealtypeapp.utility.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FirebaseDatabase firebaseDatabase;
    private String myId;
    private String destId;
    private User destUser;
    private Handler handler;
    private RecyclerView recyclerView;
    public ArrayList<ChattingData> chattingDataList;
    private List<ChatModel.Comment> comments = new ArrayList<>();

    public Adapter(FirebaseDatabase firebaseDatabase, String myId, String destId,
                   RecyclerView recyclerView){

        chattingDataList = new ArrayList<>();
        handler = new Handler();

        this.firebaseDatabase = firebaseDatabase;
        this.myId = myId;
        this.destId = destId;
        this.recyclerView = recyclerView;

        getDestUid();
    }

    //채팅 대화상자 옆에 현재 시각을 표시하기 위해 데이터 저장
    private String getCurrentTime(){

        long currentTime = System.currentTimeMillis();
        Date date = new Date(currentTime);

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd aa hh:mm");

        return dateFormat.format(date);
    }

    private void reflectChatContents(ChatModel.Comment comment){

        if(comment.getUid().equals(myId)){
            setChattingData(new ChattingData(
                    myId, comment.getMessage(),
                    getTime(comment.date), Constants.RIGHT_CONTENT));
        }
        else{
            setChattingData(new ChattingData(
                    destId, comment.getMessage(),
                    getTime(comment.date), Constants.LEFT_CONTENT));
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getItemCount());
            }
        });
    }

    private void getAllMessageComment(DataSnapshot snapshot){
        ChatModel.Comment comment;

        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            comments.add(dataSnapshot.getValue(ChatModel.Comment.class));
            comment = dataSnapshot.getValue(ChatModel.Comment.class);

            reflectChatContents(comment);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getItemCount());
                //입력 후 최하단으로 이동
                recyclerView.scrollToPosition(getItemCount() - 1);
            }
        });
    }

    private void getLastMessageComment(DataSnapshot snapshot){
        int lastIndex = (int) snapshot.getChildrenCount();
        int index = 1;
        ChatModel.Comment comment = new ChatModel.Comment();

        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            comment = dataSnapshot.getValue(ChatModel.Comment.class);
            if(index++ > lastIndex) break;
        }

        if(comment.getUid().equals(myId)) return;

        reflectChatContents(comment);
    }

    public void getMessageList(String chatRoomId){

        firebaseDatabase.getReference().child("chatrooms")
                .child(chatRoomId).child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(comments.size() > 0){
                            getLastMessageComment(snapshot);
                        }
                        else{
                            getAllMessageComment(snapshot);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getDestUid()
    {
        firebaseDatabase.getReference().child("users").child(destId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                destUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        switch (viewType){
            case Constants.LEFT_CONTENT:
                view = inflater.inflate(R.layout.item_left_content, parent, false);
                return new LeftViewHolder(view);
            case Constants.RIGHT_CONTENT:
                view = inflater.inflate(R.layout.item_right_content, parent, false);
                return new RightViewHolder(view);
        }

        //임시 코드
        view = inflater.inflate(R.layout.item_chat_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    private String getTime(String date){
        if(date.indexOf("오전") != -1){
            return date.substring(date.indexOf("오전"));
        }

        return date.substring(date.indexOf("오후"));
    }

    //각 뷰홀더에 데이터 연결
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){
            ((LeftViewHolder) holder).userId
                    .setText(chattingDataList.get(position).getUserId());
            ((LeftViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((LeftViewHolder) holder).time
                    .setText(getTime(chattingDataList.get(position).getTime()));
        }
        else{
            ((RightViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((RightViewHolder) holder).time
                    .setText(getTime(chattingDataList.get(position).getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return chattingDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chattingDataList.get(position).getViewType();
    }

    public void setChattingData(ChattingData chattingData){
        chattingDataList.add(chattingData);
    }
}
