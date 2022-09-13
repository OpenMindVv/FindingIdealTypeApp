package com.example.findingidealtypeapp.mainpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.findingidealtypeapp.R;

public class MainPageBeforeLogin extends Fragment {

    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_main_page_before_login, container, false);



        return rootView;
    }
}