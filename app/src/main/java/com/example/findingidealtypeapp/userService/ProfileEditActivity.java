package com.example.findingidealtypeapp.userService;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.findingidealtypeapp.utility.DataProcessing;
import com.example.findingidealtypeapp.utility.TokenDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
    private String image;
    private String myEmail;
    private boolean isCamera = true;
    private DataProcessing processing = new DataProcessing();
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
        getUserProfile();// ?????? ?????? ????????????

        finishText.setOnClickListener(new View.OnClickListener() { // ??????????????? ?????? ??? ?????????
            @Override
            public void onClick(View v) {
                setRetrofit();
                editFinishProfile();
                finish();
                mainActivity.onFragmentChange(Constants.PROFILE_PAGE); // ?????????????????? ?????????????????? ?????????
                // ?????? ????????? ????????? ???????????? ????????? ?????????????????? ?????? ???????????? ??????????????? ??????

            }
        });
        cancelText.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    System.out.println("??????");
                    finish();
                }
        });

        proFileEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu pop = new PopupMenu(getApplicationContext(), view);
                //?????? ???????????? ??????????????? inflate ????????????.
                pop.getMenuInflater().inflate(R.menu.main_menu, pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.one:
                                // ?????? ????????? ?????? ????????? ????????? ??????
                                sendTakePhotoIntent();
                                break;
                            case R.id.two:
                                //???????????? ?????? ????????? ???????????? ??????
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
                        String animalFace = "";
                        if(result.getResultCode() == RESULT_OK) {
                            Bitmap bitMap = null;
                            Uri uri = null;
                            if(isCamera == true) {
                                Bundle bundle = result.getData().getExtras();
                                bitMap = (Bitmap) bundle.get("data");
                                camera(bitMap);
                                profileImage.setImageBitmap(processing.rotate(bitMap, 90));
                                TokenDTO.isImage = true;
                            }
                            else {
                                Intent intent = result.getData();
                                uri = intent.getData();

                                // uri ??????????????? ??????
                                try {
                                    bitMap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                                    camera(bitMap);
                                    profileImage.setImageBitmap(bitMap);
                                    TokenDTO.isImage = true;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            //bitmap -> base64 -> utf??? ?????? ??? ????????? ??????
                            //bitMap = resize(bitMap);
                            image = processing.bitmapToByteArray(bitMap);
                            storeImageToDatabase(image, animalFace);
                            //image = processing.bitmapToByteArray(bitMap);
                        }
                    }
                }
        );
    }

    private void checkPermission(){
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }
    }

    //????????? ????????? ??????
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

    // popup_menu?????? gallery??? ???????????? getAlbum?????? ??????
    private void getAlbum(){
        isCamera = false;
        Intent intent = new Intent();
        intent.setType("image/*");                      // ????????????
        intent.setAction(Intent.ACTION_GET_CONTENT);    // ?????????(ACTION_IMAGE_CAPTURE)
        activityResultLauncher.launch(intent);
    }

    private void sendTakePhotoIntent() {
        isCamera = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(takePictureIntent);
    }


    private void editFinishProfile() {
        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        System.out.println(name);
        Call<String> call = userService.editProrfile(profileImage.toString(), name, email, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                if(result != null){ //
                    Toast.makeText(getApplicationContext(), "??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }else{     // ????????? ??????
                    System.out.println("fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println(t);
            }
        });
    }

    private void getUserProfile() {
        Call<MyPageResponse> call = userService.getProfile(TokenDTO.Token);

        call.enqueue(new Callback<MyPageResponse>() {
            @Override
            public void onResponse(Call<MyPageResponse> call, Response<MyPageResponse> response) {
                MyPageResponse result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                if(result != null){
                    byte[] Image = null;
                    Image = processing.binaryStringToByteArray(result.getImage()); //????????? ???????????? ????????????
                    if(!result.getImage().equals("0")) profileImage.setImageBitmap(processing.byteArrayToBitmap(Image)); // ????????? ????????? ??????????????? ???????????? ??????
                    processing.byteArrayToBitmap(Image);
                    nameEditText.setText(result.getName());
                    emailEditText.setText(result.getEmail());
                    passwordEditText.setText(result.getPassword());
                    myEmail = result.getEmail();
                }
            }
            @Override
            public void onFailure(Call<MyPageResponse> call, Throwable t) { // ????????? ??? ???????????? ??????
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

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        userService = retrofit.create(UserService.class);
    }

    private void storeImageToDatabase(String image, String animalFace) {
        Call<String> call = userService.insertImage(image, animalFace, myEmail);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();    // ?????????????????? ???????????? ???????????? ????????????.
                if (result != null) {
                    System.out.println("Sucess");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) { // ????????? ??? ???????????? ??????
                System.out.println(t);
            }
        });
    }

    private Interpreter getTfliteInterpreter(String modelPath) {
        try {
            return new Interpreter(loadModelFile(this, modelPath));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }


    public String camera(Bitmap bitmap){
        //??? ????????? ?????? input , output shape ?????? ?????? ??????
        // ????????? 1 150 150 3
        float[][][][] input = new float[1][150][150][3];
        float[][] output = new float[1][4]; // ?????? 4???
        String animalFace = "";

        try {
            int batchNum = 0;

            //????????? ?????? ????????? ?????? ?????????
            //ImageView iv = findViewById(R.id.image);
            //iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //iv.setImageBitmap(bitmap);

            // x,y ????????? ?????? ????????? ?????? ????????? (?????? ????????????)
            for (int x = 0; x < 150; x++) {
                for (int y = 0; y < 150; y++) {
                    int pixel = bitmap.getPixel(x, y);
                    input[batchNum][x][y][0] = Color.red(pixel) / 1.0f;
                    input[batchNum][x][y][1] = Color.green(pixel) / 1.0f;
                    input[batchNum][x][y][2] = Color.blue(pixel) / 1.0f;
                }
            }

            // ????????? tflite ?????? ?????????
            Interpreter lite = getTfliteInterpreter("converted_ver2_model.tflite");
            Log.d("myTag", "This is my message");
            lite.run(input, output);

        } catch (Exception e) {
            e.printStackTrace();
        }


        int i;
        float max = 0;

        for (i = 0; i < 4; i++) {
            if (output[0][i] * 100 > 0) {
                if (i == 0) {
                    if(max<output[0][0]* 100){
                        max = output[0][0]* 100;
                        System.out.println(String.format("????????????,%d, %.5f", i, max));
                        animalFace = "????????????";
                    }
                    System.out.println(output[0][0] * 100);
                } else if (i == 1) {

                    if(max<output[0][1]* 100)
                    {
                        max = output[0][1]* 100;
                        System.out.println(String.format("????????????,%d, %.5f", i, max));
                        animalFace = "????????????";
                    }
                    System.out.println(output[0][1] * 100);
                } else if (i == 2) {
                    if(max<output[0][2]* 100)
                    {
                        max = output[0][2]* 100;
                        System.out.println(String.format("?????????,%d, %.5f", i, max));
                        animalFace = "?????????";
                    }
                    System.out.println(output[0][2] * 100);
                } else if (i == 3) {
                    if(max<output[0][3]* 100) {
                        max = output[0][3]* 100;
                        System.out.println(String.format("?????????,%d, %.5f", i, max));
                        animalFace = "?????????";
                    }
                    System.out.println(output[0][3] * 100);
                }
            } else
                System.out.println(String.format("%d", i));
            continue;
        }
        dialog(animalFace);
        return animalFace;
    }

    public void dialog(String animalFace){
        AlertDialog.Builder menu = new AlertDialog.Builder(this);
        menu.setIcon(R.drawable.send);
        menu.setTitle("?????????"); // ??????
        menu.setMessage("???????????? "+ "'"+animalFace+"'" + " ?????????"); // ??????

        menu.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dialog ??????
                dialog.dismiss();
            }
        });
        menu.show();
    }
}