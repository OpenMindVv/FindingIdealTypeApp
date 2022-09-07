package com.example.findingidealtypeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.findingidealtypeapp.chatting.ChatList;
import com.example.findingidealtypeapp.mainpage.MainPage;
import com.example.findingidealtypeapp.userService.JoinFragment;
import com.example.findingidealtypeapp.userService.LoginFragment;
import com.example.findingidealtypeapp.userService.ProfileFragment;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    MainActivity mainActivity=this;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LoginFragment loginFragment = new LoginFragment();
    private JoinFragment joinFragment = new JoinFragment();
    public ProfileFragment profileFragment = new ProfileFragment();
    private ChatList chatListPage = new ChatList();
    private MainPage mainPage = new MainPage();
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        transaction = fragmentManager.beginTransaction();
        mainActivity.onFragmentChange(2);
        //transaction.replace(R.id.container, joinFragment).commitAllowingStateLoss();
        //transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.menu_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    public void onFragmentChange(int index){
        switch(index){
            case Constants.JOIN_PAGE: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, joinFragment).commitAllowingStateLoss();
            break;
            case Constants.LOGIN_PAGE: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, loginFragment).commitAllowingStateLoss();
            break;
            case Constants.PROFILE_PAGE: getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
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
                    if(TokenDTO.Token == null){
                        transaction.replace(R.id.menu_frame_layout, loginFragment).commitAllowingStateLoss();
                    }
                    else{
                        transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                    }
                    break;
            }
            return true;
        }
    }

}