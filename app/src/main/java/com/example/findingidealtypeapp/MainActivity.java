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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mainActivity.onFragmentChange(0);
        //transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    public void onFragmentChange(int index){
        switch(index){
            case 0: getSupportFragmentManager().beginTransaction().replace(R.id.container, joinFragment).commit();
            break;
            case 1: getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
            break;
            case 2: getSupportFragmentManager().beginTransaction().replace(R.id.container, loginFragment).commit();
            break;
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.menu_home:
                    mainActivity.onFragmentChange(0);
                    //transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                    break;
                case R.id.menu_dm:
                    mainActivity.onFragmentChange(1);
                    //transaction.replace(R.id.menu_frame_layout, chatListPage).commitAllowingStateLoss();
                    break;
                case R.id.menu_mypage:
                    mainActivity.onFragmentChange(2);
                    //transaction.replace(R.id.menu_frame_layout, loginFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}