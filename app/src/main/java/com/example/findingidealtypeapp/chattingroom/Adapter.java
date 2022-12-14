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
        //?????? ??? ??????????????? ??????
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

    //???????????? ????????? ?????? ????????? ????????? ????????? ?????????(???????????? ?????????????????? ??????)
    public void getMessageList(String chatRoomId){

        firebaseDatabase.getReference().child("chatrooms")
                .child(chatRoomId).child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //?????? ???????????? ???????????? ?????? ??????(????????? ????????? ???????????? ????????? ????????? ???????????? ??????)
                        //-> ??????????????? ??????????????? ????????? ?????????(?????? ????????? ?????? ????????? ????????? ????????? ???????????? ??????)
                        if(comments.size() > 0){
                            getLastMessageComment(snapshot);
                        }
                        else{ //????????? ????????? ???????????? ?????? ????????? -> ????????? ????????? ????????? ?????? ????????? ?????????
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
            case Constants.LEFT_CONTENT:     //????????? ????????????
                view = inflater.inflate(R.layout.item_left_content, parent, false);
                return new LeftViewHolder(view);
            case Constants.RIGHT_CONTENT:   //?????? ????????????
                view = inflater.inflate(R.layout.item_right_content, parent, false);
                return new RightViewHolder(view);
            default:                        //???????????? ???????????? ?????? ??????(????????? ??? ?????? ????????????)
                view = inflater.inflate(R.layout.item_date, parent, false);
                return new CenterViewHolder(view);

        }
    }

    //???????????? ?????? ????????? ?????? ??????
    private String getTime(String date){
        if(date.indexOf("??????") != -1){
            return date.substring(date.indexOf("??????"));
        }

        return date.substring(date.indexOf("??????"));
    }

    //?????? ????????? ????????? ??? ??? ????????? ??????(yyyy??? mm??? dd???)
    private String getDateNotation(String date){
        StringBuilder dateBuilder = new StringBuilder();

        int year = date.indexOf("-");
        int month = date.lastIndexOf("-");
        int day = date.indexOf(" ");

        if(date.indexOf("-") == -1) return date;

        dateBuilder.append(date.substring(0, year))
                .append("??? ")
                .append(date.substring(year + 1, month))
                .append("??? ")
                .append(date.substring(month + 1, day))
                .append("???");

       return dateBuilder.toString();
    }

    //??????????????? ?????? ????????? ??????
    private void setDateToChatWindow(String dateOfLastMessage){
        int size = dateList.size();

        dateOfLastMessage = getDateNotation(dateOfLastMessage);
        String dateDisplayedOnScreen="";

        if(size > 0) {
            dateDisplayedOnScreen = getDateNotation(dateList.get(size - 1));
        }

        //??????????????? ?????? ?????? or ????????? ?????? ????????? ??????????????? ????????? ?????????
        if(size == 0 || dateOfLastMessage.equals(dateDisplayedOnScreen)
                == !Constants.MESSAGE_RECEIVED_ON_SAME_DAY){

            dateList.add(dateOfLastMessage);
            chattingDataList.add(new ChattingData("", "", "",
                    dateOfLastMessage, Constants.CENTER_CONTENT));
        }

        reflectOnChatWindow();
    }

    //??? ???????????? ????????? ??????
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){

            if(!chattingDataList.get(position).getProfileImage().equals("0")) {
                byte[] Image = processing.binaryStringToByteArray(chattingDataList.get(position).getProfileImage());
                ((LeftViewHolder) holder).propfileImage.setImageBitmap(processing.byteArrayToBitmap(Image)); // ????????? ????????? ??????????????? ???????????? ??????
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
