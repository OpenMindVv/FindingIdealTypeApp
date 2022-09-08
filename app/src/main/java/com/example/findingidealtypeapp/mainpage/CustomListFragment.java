package com.example.findingidealtypeapp.mainpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.findingidealtypeapp.R;

import java.util.ArrayList;

public class CustomListFragment extends Fragment {

    ArrayList<User> userList;
    ListView customListView;
    private static CustomAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_main_page, container, false);

        //data를 가져와서 어답터와 연결해 준다. 서버에서 가져오는게 대부분 이다.
        userList = new ArrayList<>();
        userList.add(new User("Robert Downey Jr.", "https://image.tmdb.org/t/p/w600_and_h900_bestv2/5qHNjhtjMD4YWH3UP0rm4tKwxCL.jpg"));
        userList.add(new User("Scarlett Johansson", "https://image.tmdb.org/t/p/w600_and_h900_bestv2/6NsMbJXRlDZuDzatN2akFdGuTvx.jpg"));
        userList.add(new User("Cho Yeo-jeong", "https://image.tmdb.org/t/p/w600_and_h900_bestv2/5MgWM8pkUiYkj9MEaEpO0Ir1FD9.jpg"));
        userList.add(new User("Scarlett Johansson", "https://image.tmdb.org/t/p/w600_and_h900_bestv2/6NsMbJXRlDZuDzatN2akFdGuTvx.jpg"));
        userList.add(new User("Robert Downey Jr.", "https://image.tmdb.org/t/p/w600_and_h900_bestv2/5qHNjhtjMD4YWH3UP0rm4tKwxCL.jpg"));

        customListView = (ListView) rootView.findViewById(R.id.listView_custom);
        customAdapter = new CustomAdapter(getContext(),userList);
        customListView.setAdapter(customAdapter);
        customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                //각 아이템을 분간 할 수 있는 position과 뷰
                String selectedItem = (String) view.findViewById(R.id.textView_name).getTag().toString();
                Toast.makeText(getContext(), "Clicked: " + position +" " + selectedItem, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}


//data class
class User {
    private String name;
    private String thumb_url;

    public User(String name, String thumb_url) {
        this.name = name;
        this.thumb_url = thumb_url;
    }

    public String getName() {
        return name;
    }

    public String getThumb_url() {
        return thumb_url;
    }
}
