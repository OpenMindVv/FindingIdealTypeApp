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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    String mCurrentPhotoPath;
    Uri imageURI;
    Uri photoURI, albumURI;

    public static final int REQUESTCODE = 101;

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
        mContext =container.getContext();

        arrayAdapter=new ArrayAdapter(mContext, android.R.layout.simple_expandable_list_item_1,menus);
        setList.setAdapter(arrayAdapter);

        //레트로핏 초기화
        setRetrofit();
        //유저정보 가져오기
        getUserProfile();

        if (getArguments() != null)
        {
            email = getArguments().getString("email"); // 로그인에서 받아온 이메일
            getUserProfile();
        }

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

        /*
        profileImage.setOnClickListener(new View.OnClickListener() {// 프로필 이미지 눌렀을 때 이벤트
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                //intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent, 1); //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.
                // 앨범 호출

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, pickFromAlbum(intent));
            }
        });
         */

        profileImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                PopupMenu pop = new PopupMenu(mContext, view);
                //다른 메뉴들과 마찬가지로 inflate 시켜준다.
                pop.getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case R.id.one:
                                // 실제 카메라 구동 코드는 함수로 처리
                                //Toast.makeText(getApplicationContext(), "popup_menu 처리"+ menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                                captureCamera();
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


        return rootView;
    }

    public void checkPermission(){
        int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((Activity) mContext,new String[]{Manifest.permission.CAMERA},0);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        }
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==1){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //사진 촬영 함수
    private void captureCamera(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePictureIntent.resolveActivity(rootView.getContext().getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(photoFile != null){
                    Uri providerUri = FileProvider.getUriForFile(mContext, mContext.getPackageName(),photoFile);
                    imageURI = providerUri;

                    //profileImage.setImageResource(imageURI);

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,providerUri);
                    startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
                }
            }else{
                Toast.makeText(rootView.getContext(),"접근 불가능 합니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    //촬영 혹은 크롭된 사진에 대한 새로운 이미지 저장 함수
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");

        if(!storageDir.exists()){
            storageDir.mkdirs();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    // popup_menu에서 gallery를 클릭하면 getAlbum함수 호출
    private void getAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }
    //사진 crop할 수 있도록 하는 함수
    public void cropImage(){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI,"image/*");
        cropIntent.putExtra("aspectX",1);
        cropIntent.putExtra("aspectY",1);
        cropIntent.putExtra("scale",true);
        cropIntent.putExtra("output",albumURI);
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }
    // 갤러리에 사진 추가 함수
    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);
        mContext.sendBroadcast(mediaScanIntent);
        Toast.makeText(rootView.getContext(),"앨범에 저장되었습니다.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageURI.toString());
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageURI.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            profileImage.setImageBitmap(bitmap);
        }
    }


    /*
    @Override //갤러리에서 이미지 불러온 후 행동
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지뷰에 세팅
                    mPhotoCircleImageView.setImageBitmap(img);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
     */

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
                System.out.println("call= "+call);
                System.out.println("result= "+result);
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
                System.out.println(call);
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
