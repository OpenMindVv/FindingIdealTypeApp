package com.example.findingidealtypeapp.mainpage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        userList.add(new User("JENNIERUBYJANE", R.drawable.jennie));
        userList.add(new User("SOOYAAA___", R.drawable.jisoo));
        userList.add(new User("ROSES_ARE_ROSIE", R.drawable.rose));
        userList.add(new User("LALALALISA_M", R.drawable.lisa));
        userList.add(new User("_FOR_YXXNG", R.drawable.kiyong));

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

        todayLayout = (ConstraintLayout) rootView.findViewById(R.id.today_layout);
        todayId = (TextView) rootView.findViewById(R.id.today_id);
        todayImage = (CircleImageView) rootView.findViewById(R.id.today_image);

        todayId.setText("JENNIERUBYJANE");
        todayImage.setImageResource(R.drawable.jennie);

        todayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("오늘의 이상형", todayId.toString());
            }
        });

        return rootView;
    }
}


//data class
class User {
    private String name;
    private int thumb_url;

    public User(String name, int thumb_url) {
        this.name = name;
        this.thumb_url = thumb_url;
    }

    public String getName() {
        return name;
    }

    public int getThumb_url() {
        return thumb_url;
    }
}
