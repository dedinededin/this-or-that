package com.example.thisorthat.network;

import com.example.thisorthat.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserApi {
    @GET("login/")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @POST("users/")
    Call<User> signup(@Body User user);
}
