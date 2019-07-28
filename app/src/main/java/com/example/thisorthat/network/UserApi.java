package com.example.thisorthat.network;

import com.example.thisorthat.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @Headers("X-Parse-Revocable-Session: 1")
    @GET("login/")
    Call<User> login(@Query("username") String username, @Query("password") String password);

    @Headers("X-Parse-Revocable-Session: 1")
    @POST("users/")
    Call<User> signup(@Body User user);

    @GET("users/me/")
    Call<User> getCurrentUser(@Header("X-Parse-Session-Token") String sessionToken);

    @GET("users/{objectId}")
    Call<User> getUser(@Path("objectId") String objectId);
}
