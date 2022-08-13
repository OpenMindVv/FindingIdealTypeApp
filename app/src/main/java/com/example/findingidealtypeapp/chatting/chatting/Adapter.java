package com.example.findingidealtypeapp.chatting.chatting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.chatting.ViewHolder;
import com.example.findingidealtypeapp.chatting.utility.Constants;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChattingData> chattingDataList;

    public Adapter(ArrayList<ChattingData> chattingDataList){
        this.chattingDataList = chattingDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view;

        Log.v("테테테테", viewType+"");

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

    //각 뷰홀더에 데이터 연결
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewHolder){
            ((LeftViewHolder) holder).userId
                    .setText(chattingDataList.get(position).getUserId());
            ((LeftViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((LeftViewHolder) holder).time
                    .setText(chattingDataList.get(position).getTime());
        }
        else{
            ((RightViewHolder) holder).content
                    .setText(chattingDataList.get(position).getContent());
            ((RightViewHolder) holder).time
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
}
