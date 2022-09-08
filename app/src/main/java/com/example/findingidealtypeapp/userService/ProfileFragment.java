package com.example.findingidealtypeapp.userService;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileFragment extends Fragment {

    private TextView profileName, numberFollow, numberFollowing;
    private ImageView profileImage;
    private UserService userService;
    private Button profileEditButton;
    private ListView setList;
    private MainActivity activity;
    private ViewGroup rootView;
    private Retrofit retrofit;
    private String email, name, follow, following;
    private ArrayAdapter arrayAdapter;
    private Context mContext;
    private static final int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private boolean isCamera = true;
    private File destFile;
    String AbsolutePhotoPath;
    Uri imageURI;
    Uri photoURI, albumURI;

    private List<String> menus = Arrays.asList(
            "도움말","안내","로그아웃","로그아웃","로그아웃","로그아웃"
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup)inflater.inflate(R.layout.profile_page, container, false);
        profileName = rootView.findViewById(R.id.profile_name);
        numberFollow = rootView.findViewById(R.id.follow);
        numberFollowing = rootView.findViewById(R.id.following);
        profileEditButton = rootView.findViewById(R.id.profile_edit_button);
        setList = rootView.findViewById(R.id.setList);
        profileImage = rootView.findViewById(R.id.profile_image);
        profileEditButton = rootView.findViewById(R.id.profile_edit_button);
        mContext =container.getContext();

        arrayAdapter=new ArrayAdapter(mContext, android.R.layout.simple_expandable_list_item_1,menus);
        setList.setAdapter(arrayAdapter);

        //레트로핏 초기화
        setRetrofit();
        //유저정보 가져오기
        getUserProfile();

        /* 프래그먼트 간 데이터 받아오는 코드
        if (getArguments() != null)
        {
            email = getArguments().getString("email"); // 로그인에서 받아온 이메일
            getUserProfile();
        }
         */

        setList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = (String) adapterView.getItemAtPosition(position); // 데이터에 클릭 정보가 담김
                System.out.println(data);
                switch(data) {
                    case "로그아웃":logoutDialog();
                        break;
                }
                //if(data.equals("로그아웃")){
                //   logoutDialog();
                //}
            }
        });

        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu pop = new PopupMenu(mContext, view);
                //다른 메뉴들과 마찬가지로 inflate 시켜준다.
                pop.getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.one:
                                // 실제 카메라 구동 코드는 함수로 처리
                                //Toast.makeText(getApplicationContext(), "popup_menu 처리"+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                sendTakePhotoIntent();
                                break;
                            case R.id.two:
                                //갤러리에 관한 권한을 받아오는 코드
                                //Toast.makeText(getApplicationContext(), "popup_menu 처리"+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                getAlbum();
                                break;
                        }
                        return true;
                    }
                });
                pop.show();
                checkPermission();
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Bitmap bitMap = null;
                            Uri uri = null;
                            if(isCamera == true) {
                                Bundle bundle = result.getData().getExtras();
                                bitMap = (Bitmap) bundle.get("data");
                                profileImage.setImageBitmap(rotate(bitMap, 90));
                                //sendImage(imageURI);
                            }
                            else {
                                Intent intent = result.getData();
                                uri = intent.getData();

                                // uri 비트맵으로 변경
                                try {
                                    bitMap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
                                    profileImage.setImageBitmap(bitMap);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //sendImagePath(uri);
                            }
                            //bitmap -> base64 -> utf로 변경 후 서버로 통신
                            bitMap = resize(bitMap);
                            String image = bitmapToByteArray(bitMap);
                            storeImageToDatabase(image);
                            //BitMapToString(bitMap);
                        }
                    }
                }
        );
        return rootView;
    }

    public void checkPermission(){
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CAMERA},0);
        }
    }
    //Permission에 대한 승인 완료확인 코드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if(grantResults[0]==0){
                Toast.makeText(mContext,"카메라 권한 승인완료",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"카메라 권한 승인 거절",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 사진 회전하는 함수
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    // popup_menu에서 gallery를 클릭하면 getAlbum함수 호출
    private void getAlbum(){
        isCamera = false;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        activityResultLauncher.launch(intent);
    }

    private void sendTakePhotoIntent() {
        isCamera = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(takePictureIntent);
    }

    public String bitmapToByteArray(Bitmap bitmap) {
        String image = "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        image = byteArrayToBinaryString(byteArray);
        return image;
    }

    /**바이너리 바이트 배열을 스트링으로 바꾸어주는 메서드 */
    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    /**바이너리 바이트를 스트링으로 바꾸어주는 메서드 */
    public static String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    //비트맵 사이즈 변경
    private Bitmap resize(Bitmap bm){
        Configuration config=getResources().getConfiguration();
        if(config.smallestScreenWidthDp>=800)
            bm = Bitmap.createScaledBitmap(bm, 400, 240, true);
        else if(config.smallestScreenWidthDp>=600)
            bm = Bitmap.createScaledBitmap(bm, 300, 180, true);
        else if(config.smallestScreenWidthDp>=400)
            bm = Bitmap.createScaledBitmap(bm, 200, 120, true);
        else if(config.smallestScreenWidthDp>=360)
            bm = Bitmap.createScaledBitmap(bm, 180, 108, true);
        else
            bm = Bitmap.createScaledBitmap(bm, 160, 96, true);
        return bm;
    }

    private void storeImageToDatabase(String image) {
        Call<String> call = userService.insertImage(image);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if (result != null) {
                    System.out.println("성공");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
    }

    private void getUserProfile() {
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){
                    profileName.setText(result.getName());
                    numberFollow.setText(result.getFollow());
                    numberFollowing.setText(result.getFollowing());
                }
            }

            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
    }

    private void setRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                //.client(okHttpClient)
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
    }

    private void logoutDialog(){
        AlertDialog.Builder logout = new AlertDialog.Builder(mContext);
        logout.setIcon(R.mipmap.ic_launcher);
        logout.setTitle("로그아웃"); // 제목
        logout.setMessage("로그아웃 하시겠습니까?"); // 문구


        // 확인 버튼
        logout.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog 제거
                dialog.dismiss();
            }
        });

        // 취소 버튼
        logout.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog 제거
                dialog.dismiss();
                // 자신(플래그먼트)를 호출할 상위 액티비티
                activity = (MainActivity) getActivity();
                // 액티비티에 플래그먼트를 변경하는 메소드 구현하여 호출
                activity.onFragmentChange(Constants.LOGIN_PAGE);
                TokenDTO.Token = null;
            }
        });
        logout.show();
    }


}