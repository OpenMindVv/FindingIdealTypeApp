package com.example.findingidealtypeapp.chatting.chatting;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

public class LeftViewHolder extends RecyclerView.ViewHolder{

    public TextView userId;
    public TextView content;
    public TextView time;

    public LeftViewHolder(@NonNull View itemView) {

        super(itemView);

        userId = itemView.findViewById(R.id.user_id);
        content = itemView.findViewById(R.id.content);
        time = itemView.findViewById(R.id.time);
    }
}
