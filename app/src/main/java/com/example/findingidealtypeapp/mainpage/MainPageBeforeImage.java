package com.example.findingidealtypeapp.mainpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.findingidealtypeapp.R;

public class MainPageBeforeImage extends Fragment {

    private ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_before_image, container, false);



        return rootView;
    }
}