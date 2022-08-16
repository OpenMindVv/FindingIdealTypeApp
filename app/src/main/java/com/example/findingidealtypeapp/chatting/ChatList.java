package com.example.findingidealtypeapp.chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.findingidealtypeapp.R;

public class ChatList extends Fragment {

    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = (ViewGroup)inflater.inflate(R.layout.activity_chat_list, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.chat_list);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity(), RecyclerView.VERTICAL, false)) ;

        Adapter adapter = new Adapter(getActivity());

        for (int number = 1; number <= 5; number++) {
            ChatInformation example = new ChatInformation("밍귤" + number, "안녕하세요!", "오후 6:57");
            adapter.setChatInformations(example);
        }

        recyclerView.setAdapter(adapter);

        return rootView;
    }
}