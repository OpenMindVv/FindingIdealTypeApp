package com.example.findingidealtypeapp.mainpage;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findingidealtypeapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListFragment extends Fragment {

    ArrayList<User> userList;
    ListView customListView;
    ConstraintLayout todayLayout;
    TextView todayId;
    CircleImageView todayImage;
    private static CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_user_list, container, false);

        //data를 가져와서 어답터와 연결해 준다. 서버에서 가져오는게 대부분 이다.
        userList = new ArrayList<>();
        userList.add(new User("JENNIERUBYJANE", R.drawable.jennie, "CAT"));
        userList.add(new User("SOOYAAA___", R.drawable.jisoo, "DOG"));
        userList.add(new User("ROSES_ARE_ROSIE", R.drawable.rose, "CAT"));
        userList.add(new User("LALALALISA_M", R.drawable.lisa, "CAT"));
        userList.add(new User("_FOR_YXXNG", R.drawable.kiyong, "DINOSAUR"));

        customListView = (ListView) rootView.findViewById(R.id.listView_custom);
        customAdapter = new CustomAdapter(getContext(),userList);
        customListView.setAdapter(customAdapter);


        todayLayout = (ConstraintLayout) rootView.findViewById(R.id.today_layout);
        todayId = (TextView) rootView.findViewById(R.id.today_id);
        todayImage = (CircleImageView) rootView.findViewById(R.id.today_image);

        todayId.setText("JENNIERUBYJANE");
        todayImage.setImageResource(R.drawable.jennie);

        todayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnClickShowAlert(rootView);
            }
        });



        return rootView;
    }

    public void OnClickShowAlert(View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
        builder.setTitle("오늘의 이상형");
        builder.setMessage("JENNIERUBYJANE" +" 님은 " + "토끼상" + "입니다.");
        builder.setPositiveButton("채팅하기", null);
        builder.setNeutralButton("닫기", null);

        builder.show();
    }
}


//data class
class User {
    private String name;
    private int thumb_url;
    private String animal;

    public User(String name, int thumb_url, String animal) {
        this.name = name;
        this.thumb_url = thumb_url;
        this.animal = animal;
    }

    public String getName() {
        return name;
    }

    public int getThumb_url() {
        return thumb_url;
    }

    public String getAnimal() { return animal; }
}
