package com.example.findingidealtypeapp.chatting;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView profileImage;
    public TextView userId;
    public TextView content;
    public TextView date;
    private LinearLayout chatListLayout;
    private OnViewHolderItemClickListener onViewHolderItemClickListener;

    public ViewHolder(Context context, @NonNull View itemView) {

        super(itemView);

        profileImage = itemView.findViewById(R.id.user_image);
        userId = itemView.findViewById(R.id.user_id);
        content = itemView.findViewById(R.id.content);
        date = itemView.findViewById(R.id.date);
        chatListLayout = itemView.findViewById(R.id.ll_chat_list);

        chatListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    public void setOnViewHolderItemClickListener(
            OnViewHolderItemClickListener onViewHolderItemClickListener) {

        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}