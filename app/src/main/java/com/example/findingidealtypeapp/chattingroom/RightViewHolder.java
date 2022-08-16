package com.example.findingidealtypeapp.chattingroom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

public class RightViewHolder extends RecyclerView.ViewHolder{

    public TextView content;
    public TextView time;

    public RightViewHolder(@NonNull View itemView) {

        super(itemView);

        content = itemView.findViewById(R.id.content);
        time = itemView.findViewById(R.id.time);
    }
}
