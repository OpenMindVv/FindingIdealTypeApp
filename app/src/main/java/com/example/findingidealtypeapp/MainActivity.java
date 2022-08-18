package com.example.findingidealtypeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.findingidealtypeapp.chatting.ChatList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MainActivity mainActivity=this;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LoginFragment loginFragment = new LoginFragment();
    private JoinFragment joinFragment = new JoinFragment();
    public ProfileFragment profileFragment = new ProfileFragment();
    private ChatList chatListPage = new ChatList();
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transaction = fragmentManager.beginTransaction();
        mainActivity.onFragmentChange(0);
        //transaction.replace(R.id.container, joinFragment).commitAllowingStateLoss();
        //transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    public void onFragmentChange(int index){
        switch(index){
            case 0: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, joinFragment).commitAllowingStateLoss();
            break;
            case 1: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, loginFragment).commitAllowingStateLoss();
            break;
            case 2: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
            break;
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_home:
                    transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                    break;
                case R.id.menu_dm:
                    transaction.replace(R.id.menu_frame_layout, chatListPage).commitAllowingStateLoss();
                    break;
                case R.id.menu_mypage:
                    transaction.replace(R.id.menu_frame_layout, loginFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}