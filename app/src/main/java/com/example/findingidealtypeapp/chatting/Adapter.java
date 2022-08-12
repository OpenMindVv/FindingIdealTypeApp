package com.example.findingidealtypeapp.chatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    private ArrayList<ChatInformation> chatInformationList;

    public Adapter(){
        chatInformationList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_chat_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(context, view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatInformation chatInformation = chatInformationList.get(position);

        holder.userId.setText(chatInformation.getUserId());
        holder.content.setText(chatInformation.getContent());
        holder.date.setText(chatInformation.getDate());
    }

    @Override
    public int getItemCount() {
        return chatInformationList.size();
    }

    public void setChatInformations(ChatInformation chatInformation){
        chatInformationList.add(chatInformation);
    }
}
