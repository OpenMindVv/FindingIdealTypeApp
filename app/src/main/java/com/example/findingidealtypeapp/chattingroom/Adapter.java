package com.example.findingidealtypeapp.chattingroom;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chatting.ChatRoom;
import com.example.findingidealtypeapp.chatting.ViewHolder;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.DataProcessing;
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
    private ChatRoom chatRoom;
    private String userProfileImage;
    private Handler handler;
    private RecyclerView recyclerView;
    public ArrayList<ChattingData> chattingDataList;
    private List<ChatModel.Comment> comments = new ArrayList<>();
    private List<String> dateList = new ArrayList<>();
    private DataProcessing processing = new DataProcessing();

    public Adapter(FirebaseDatabase firebaseDatabase, ChatRoom chatRoom,
                   RecyclerView recyclerView){

        chattingDataList = new ArrayList<>();
        handler = new Handler();
        dateList = new ArrayList<>();

        this.firebaseDatabase = firebaseDatabase;
        this.chatRoom = chatRoom;
        this.recyclerView = recyclerView;
    }

    private void reflectOnChatWindow(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                notifyItemInserted(getItemCount());
            }
        });
    }

    private void reflectChatContents(ChatModel.Comment comment){
        setDateToChatWindow(comment.getDate());

        if(comment.getUid().equals(chatRoom.getMyId())){
            setChattingData(new ChattingData(
                    userProfileImage, chatRoom.getMyId(),
                    comment.getMessage(), getTime(comment.date),
                    Constants.RIGHT_CONTENT));
        }
        else{
            setChattingData(new ChattingData(
                    userProfileImage, chatRoom.getReceiverName(),
                    comment.getMessage(), getTime(comment.date),
                    Constants.LEFT_CONTENT));
        }

        reflectOnChatWindow();
        //입력 후 최하단으로 이동
        recyclerView.scrollToPosition(getItemCount() - 1);
    }

    private void getAllMessageComment(DataSnapshot snapshot){
        ChatModel.Comment comment;

        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            comments.add(dataSnapshot.getValue(ChatModel.Comment.class));
            comment = dataSnapshot.getValue(ChatModel.Comment.class);

            reflectChatContents(comment);
        }

        reflectOnChatWindow();
    }

    private void getLastMessageComment(DataSnapshot snapshot){
        int lastIndex = (int) snapshot.getChildrenCount();
        int index = 1;
        ChatModel.Comment comment = new ChatModel.Comment();

        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
            comment = dataSnapshot.getValue(ChatModel.Comment.class);
            if(index++ > lastIndex) break;
        }

        reflectChatContents(comment);
    }

    //상대방과 기존에 주고 받았던 메세지 기록을 가져옴(메세지가 갱신될때마다 수행)
    public void getMessageList(String chatRoomId){

        firebaseDatabase.getReference().child("chatrooms")
                .child(chatRoomId).child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //현재 사용자가 대화방에 있는 경우(화면에 기존에 주고받은 메세지 내용이 보여지는 상황)
                        //-> 실시간으로 대화내용을 화면에 보여줌(가장 최근에 받은 메세지 하나만 화면에 보여주기 위함)
                        if(comments.size() > 0){
                            getLastMessageComment(snapshot);
                        }
                        else{ //대화방 클릭시 보여지는 화면 초기화 -> 기존의 메세지 내역을 모두 화면에 보여줌
                            getAllMessageComment(snapshot);
                        }
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
            case Constants.LEFT_CONTENT:     //상대방 대화내용
                view = inflater.inflate(R.layout.item_left_content, parent, false);
                return new LeftViewHolder(view);
            case Constants.RIGHT_CONTENT:   //나의 대화내용
                view = inflater.inflate(R.layout.item_right_content, parent, false);
                return new RightViewHolder(view);
            default:                        //메세지를 주고받은 날짜 표시(하루에 한 번만 표시한다)
                view = inflater.inflate(R.layout.item_date, parent, false);
                return new CenterViewHolder(view);

        }
    }

    //대화상자 옆에 표시할 현재 시각
    private String getTime(String date){
        if(date.indexOf("오전") != -1){
            return date.substring(date.indexOf("오전"));
        }

        return date.substring(date.indexOf("오후"));
    }

    //화면 중앙에 하루에 한 번 표시할 날짜(yyyy년 mm월 dd일)
    private String getDateNotation(String date){
        StringBuilder dateBuilder = new StringBuilder();

        int year = date.indexOf("-");
        int month = date.lastIndexOf("-");
        int day = date.indexOf(" ");

        if(date.indexOf("-") == -1) return date;

        dateBuilder.append(date.substring(0, year))
                .append("년 ")
                .append(date.substring(year + 1, month))
                .append("월 ")
                .append(date.substring(month + 1, day))
                .append("일");

       return dateBuilder.toString();
    }

    //채팅화면에 날짜 정보를 표시
    private void setDateToChatWindow(String dateOfLastMessage){
        int size = dateList.size();

        dateOfLastMessage = getDateNotation(dateOfLastMessage);
        String dateDisplayedOnScreen="";

        if(size > 0) {
            dateDisplayedOnScreen = getDateNotation(dateList.get(size - 1));
        }

        //채팅화면의 가장 처음 or 날짜가 바뀐 시점에 날짜정보를 화면에 보여줌
        if(size == 0 || dateOfLastMessage.equals(dateDisplayedOnScreen)
                == !Constants.MESSAGE_RECEIVED_ON_SAME_DAY){

            dateList.add(dateOfLastMessage);
            chattingDataList.add(new ChattingData("", "", "",
                    dateOfLastMessage, Constants.CENTER_CONTENT));
        }

        reflectOnChatWindow();
    }

    //각 뷰홀더에 데이터 연결
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){

            if(!chattingDataList.get(position).getProfileImage().equals("0")) {
                byte[] Image = processing.binaryStringToByteArray(chattingDataList.get(position).getProfileImage());
                ((LeftViewHolder) holder).propfileImage.setImageBitmap(processing.byteArrayToBitmap(Image)); // 프로필 이미지 비트맵으로 가져와서 저장
            }
            else ((LeftViewHolder) holder).propfileImage.setImageResource(R.drawable.profile_image);

            ((LeftViewHolder) holder).userId.setText(chattingDataList.get(position).getUserId());
            ((LeftViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((LeftViewHolder) holder).time
                    .setText(getTime(chattingDataList.get(position).getTime()));
        }
        else if(holder instanceof RightViewHolder){
            ((RightViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((RightViewHolder) holder).time
                    .setText(getTime(chattingDataList.get(position).getTime()));
        }
        else{
            ((CenterViewHolder) holder).date
                    .setText(chattingDataList.get(position).getTime());
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

    public void setUserProfileImage(String userProfileImage){
        this.userProfileImage = userProfileImage;
    }
}
