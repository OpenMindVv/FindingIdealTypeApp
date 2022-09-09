package com.example.findingidealtypeapp.userService;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.findingidealtypeapp.MainActivity;
import com.example.findingidealtypeapp.R;
import com.example.findingidealtypeapp.userServiceApi.UserService;
import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.utility.Constants;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileEditActivity extends AppCompatActivity {

    private TextView proFileEditText, cancelText, finishText;
    private EditText emailEditText, nameEditText, passwordEditText;
    private Retrofit retrofit;
    private UserService userService;
    private ImageView profileImage;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private boolean isCamera = true;
    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        proFileEditText = findViewById(R.id.editProfileText);
        emailEditText = findViewById(R.id.profile_edit_email);
        nameEditText = findViewById(R.id.profile_edit_name);
        passwordEditText = findViewById(R.id.profile_edit_password);
        cancelText = findViewById(R.id.cancelText);
        finishText = findViewById(R.id.finishText);
        profileImage = findViewById(R.id.profile_image);


        setRetrofit();
        getUserProfile();// 유저 정보 가져오기

        finishText.setOnClickListener(new View.OnClickListener() { // 완료텍스트 누를 때 이벤트
            @Override
            public void onClick(View v) {
                setRetrofit();
                editFinishProfile();
                finish();
                mainActivity.onFragmentChange(Constants.PROFILE_PAGE); // 수정완료하면 마이페이지로 가는데
                // 현재 수정된 화면이 안되어서 나중에 수정해야할듯 다른 페이지를 보여주던지 해서

            }
        });
        cancelText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    System.out.println("취소");
                    finish();
                }
        });

        proFileEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                //다른 메뉴들과 마찬가지로 inflate 시켜준다.
                pop.getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.one:
                                // 실제 카메라 구동 코드는 함수로 처리
                                sendTakePhotoIntent();
                                break;
                            case R.id.two:
                                //갤러리에 관한 권한을 받아오는 코드
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
                                    bitMap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
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
                        }
                    }
                }
        );
    }

    public void checkPermission(){
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }
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

    // 사진 회전하는 함수
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
    public String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    /**바이너리 바이트를 스트링으로 바꾸어주는 메서드 */
    public String byteToBinaryString(byte n) {
        StringBuilder sb = new StringBuilder("00000000");
        for (int bit = 0; bit < 8; bit++) {
            if (((n >> bit) & 1) > 0) {
                sb.setCharAt(7 - bit, '1');
            }
        }
        return sb.toString();
    }

    private void editFinishProfile() {

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Call<String> call = userService.editProrfile(name, email, password);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){ //
                    System.out.println("성공");
                    System.out.println(result);
                    Toast.makeText(getApplicationContext(), "회원정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                }else{     // 로그인 실패
                    System.out.println("실패");
                    System.out.println(result);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // 이거는 걍 통신에서 실패
                System.out.println("통신실패");
                System.out.println(t);
            }
        });
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
        System.out.println(TokenDTO.Token);
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // 웹서버로부터 응답받은 데이터가 들어있다.
                if(result != null){
                    byte[] Image = null;
                    Image = binaryStringToByteArray(result.getImage());
                    byteArrayToBitmap(Image);
                    nameEditText.setText(result.getName());
                    emailEditText.setText(result.getEmail());
                    passwordEditText.setText(result.getPassword());
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
    /** 바이너리 스트링을 바이트로 변환*/
    public byte[] binaryStringToByteArray(String s) {
        int count = s.length() / 8;
        byte[] b = new byte[count];
        for (int i = 1; i < count; ++i) {
            String t = s.substring((i - 1) * 8, i * 8);
            b[i - 1] = binaryStringToByte(t);
        }
        return b;
    }
    /** 바이너리 스트링을 바이트로 변환*/
    public byte binaryStringToByte(String s) {
        byte ret = 0, total = 0;
        for (int i = 0; i < 8; ++i) {
            ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
            total = (byte) (ret | total);
        }
        return total;
    }

    /**  Byte를 Bitmap으로 변환*/
    public Bitmap byteArrayToBitmap(byte[] byteArray ) {
        Bitmap bitmap = BitmapFactory.decodeByteArray( byteArray, 0, byteArray.length ) ;
        profileImage.setImageBitmap(bitmap);
        return bitmap ;
    }

    private void setRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://2fc39d2c-748a-42b0-8fda-cc926df84d08.mock.pstmn.io/")
                //.client(okHttpClient)
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }
}