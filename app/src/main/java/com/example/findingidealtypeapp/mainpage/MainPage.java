package com.example.findingidealtypeapp.mainpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.findingidealtypeapp.R;

import java.util.ArrayList;

public class MainPage extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_page, container, false);

        String[] menuItems = {"Jisoo", "Jennie", "Rosé", "Lisa"};

        ListView listView = (ListView) view.findViewById(R.id.user_listView);

        return view;
    }
}