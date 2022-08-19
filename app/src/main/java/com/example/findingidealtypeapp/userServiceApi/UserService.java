package com.example.findingidealtypeapp.userServiceApi;

import com.example.findingidealtypeapp.userServiceApi.loginService.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserService {
    // @GET( EndPoint-자원위치(URI) )
    @GET("user/all")
    Call<MemberDTO> getProfileList();

    @GET("user/{email}")
    Call<MyPageResponse> getProfile(
      @Query("email") String email
    );

    @POST("user/login")
    Call<LoginResponse> login(
            @Query("email") String email, @Query("password") String password);

    @PUT("user/create")
    Call<String> createUser(
            @Query("email") String email, @Query("password") String password, @Query("name") String name, @Query("follow") String follow, @Query("following") String following
            //@Body MemberDTO memberDTO
    );
}