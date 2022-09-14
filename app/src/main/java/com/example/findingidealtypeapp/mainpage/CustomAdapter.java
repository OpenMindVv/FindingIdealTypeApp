package com.example.findingidealtypeapp.mainpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.findingidealtypeapp.R;
<<<<<<< Updated upstream
=======
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.DataProcessing;
>>>>>>> Stashed changes

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {
    private Context context;
    private List list;
    private Context mContext;
    private DataProcessing processing = new DataProcessing();

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public TextView tv_name;
        public ImageView iv_thumb;
    }

    public CustomAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.item_user_list, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_name = (TextView) convertView.findViewById(R.id.textView_name);
        viewHolder.iv_thumb = (ImageView) convertView.findViewById(R.id.imageView_thumb);

<<<<<<< Updated upstream
        final User user = (User) list.get(position);
=======
        final MyPageResponse user = (MyPageResponse) list.get(position);

>>>>>>> Stashed changes
        viewHolder.tv_name.setText(user.getName());

        byte[] Image = null;
        if(!user.getImage().equals("0")) {
            Image = processing.binaryStringToByteArray(user.getImage()); //이미지 바이트로 가져오기
            viewHolder.iv_thumb.setImageBitmap(processing.byteArrayToBitmap(Image)); // 프로필 이미지 비트맵으로 가져와서 저장
        }
        else viewHolder.iv_thumb.setImageResource(R.drawable.profile_image);

        Glide
                .with(context)
                .load(user.getThumb_url())
                .centerCrop()
                .apply(new RequestOptions().override(250, 350));
                //.into(viewHolder.iv_thumb);
        viewHolder.tv_name.setTag(user.getName());


//        //아이템 클릭 방법2 - 클릭시 아이템 반전 효과가 안 먹힘
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, " " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        //Return the completed view to render on screen
        return convertView;
    }
}
