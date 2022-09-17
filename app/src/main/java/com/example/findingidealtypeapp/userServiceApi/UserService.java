package com.example.findingidealtypeapp.userServiceApi;

import com.example.findingidealtypeapp.userServiceApi.myPageService.MyPageResponse;
import com.example.findingidealtypeapp.userServiceApi.loginService.LoginResponse;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {
    // @GET( EndPoint-자원위치(URI) )
    @GET("user/all")
    Call<List<MyPageResponse>> getProfileList();

    @GET("user/getPassword")
    Call<MyPageResponse> getPassword(
            @Query("email") String email
    );

    @GET("user/getProfile")
    Call<MyPageResponse> getProfile(
            @Header("X-AUTH-TOKEN") String token
    );

    @POST("user/login")
    Call<String> login(
            @Query("email") String email, @Query("password") String password);

    @POST("user/editProfile")
    Call<String> editProrfile(
            @Query("image") String image, @Query("name") String name, @Query("email") String email, @Query("password") String password);

    @PUT("user/create")
    Call<String> createUser(
            @Query("Image") String Image, @Query("email") String email, @Query("password") String password, @Query("name") String name, @Query("follow") String follow, @Query("following") String following
    );

    @GET("user/getName")
    Call<MyPageResponse> getName(@Query("email") String email);

    @POST("user/insertImage")
    Call<String> insertImage(
            @Query("imageFile") String imageFile, @Query("animalFace") String animalFace, @Query("email") String email);

    @PUT("user/insert")
    Call<String> createProfile(
            @Query("Image") String Image, @Query("email") String email, @Query("password") String password, @Query("name") String name, @Query("follow") String follow, @Query("following") String following, @Query("animalFace") String animalFace
    );

    @GET("user/getImage")
    Call<String> getUserProfileImage(@Query("email") String email);
}
