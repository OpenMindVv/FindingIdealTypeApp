package com.example.findingidealtypeapp.chattingroom;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

public class CenterViewHolder extends RecyclerView.ViewHolder {
    public TextView date;

    public CenterViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date);
    }
}
