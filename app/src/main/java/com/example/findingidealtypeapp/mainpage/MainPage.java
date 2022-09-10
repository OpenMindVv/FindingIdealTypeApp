package com.example.findingidealtypeapp.mainpage;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.findingidealtypeapp.R;

public class MainPage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_list, container, false);

        //Button todayButton = (Button) view.findViewById(R.id.today_button);
        Button rabbitButton = (Button) view.findViewById(R.id.rabbit_button);
        Button dogButton = (Button) view.findViewById(R.id.dog_button);
        Button catButton = (Button) view.findViewById(R.id.cat_button);
        Button dinosaurButton = (Button) view.findViewById(R.id.dinosaur_button);

        rabbitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomListFragment.class);
                startActivity(intent);
            }
        });

        return view;
    }
}