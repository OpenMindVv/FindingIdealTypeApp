package com.example.findingidealtypeapp.apiService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    // @GET( EndPoint-자원위치(URI) )
    @GET("user/all")
    Call<MemberDTO> getProfileList();

    @POST("user/login")
    Call<String> login(
            @Query("email") String email, @Query("password") String password);

    @PUT("user/create")
    Call<String> createUser(
            @Query("email") String email, @Query("password") String password, @Query("name") String name
            //@Body MemberDTO memberDTO
    );
}
