package com.example.findingidealtypeapp.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.findingidealtypeapp.R;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, int resource, List<User> userList) {
        super(context, resource, userList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.user_name);
        ImageView imageView = convertView.findViewById(R.id.user_image);

        textView.setText(user.getName());
        imageView.setImageResource(user.getImage());

        return convertView;
    }
}
