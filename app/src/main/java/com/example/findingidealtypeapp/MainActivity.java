package com.example.findingidealtypeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.findingidealtypeapp.chatting.ChatListFragment;
import com.example.findingidealtypeapp.mainpage.CustomListFragment;
import com.example.findingidealtypeapp.mainpage.MainPage;
import com.example.findingidealtypeapp.mainpage.MainPageBeforeImage;
import com.example.findingidealtypeapp.mainpage.MainPageBeforeLogin;
import com.example.findingidealtypeapp.userService.JoinFragment;
import com.example.findingidealtypeapp.userService.LoginFragment;
import com.example.findingidealtypeapp.userService.ProfileFragment;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    MainActivity mainActivity=this;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private LoginFragment loginFragment = new LoginFragment();
    private JoinFragment joinFragment = new JoinFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private MainPageBeforeLogin mainPageBeforeLogin = new MainPageBeforeLogin();
    private MainPageBeforeImage mainPageBeforeImage = new MainPageBeforeImage();
    private ChatListFragment chatListFragment = new ChatListFragment();
<<<<<<< HEAD
=======
    private CustomListFragment customListFragment = new CustomListFragment();
>>>>>>> 20b8bbcfe2d4a59c9b90e80450c12e29977f2936
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
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        if(TokenDTO.Token == null){
                            transaction.replace(R.id.menu_frame_layout, mainPageBeforeLogin).commitAllowingStateLoss();
                        }
                        else if(TokenDTO.isImage == false)
                        {
                            transaction.replace(R.id.menu_frame_layout, mainPageBeforeImage).commitAllowingStateLoss();
                        }
                        else if(TokenDTO.Token != null && TokenDTO.isImage == true) transaction.replace(R.id.menu_frame_layout, profileFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menu_dm:
                        transaction.replace(R.id.menu_frame_layout, chatListFragment).commitAllowingStateLoss();
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
        });
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
}
         