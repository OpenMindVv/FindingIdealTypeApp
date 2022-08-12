package com.example.findingidealtypeapp.chatting;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView userId;
    public TextView content;
    public TextView date;

    public ViewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        userId = itemView.findViewById(R.id.user_id);
        content = itemView.findViewById(R.id.content);
        date = itemView.findViewById(R.id.date);
    }
}
