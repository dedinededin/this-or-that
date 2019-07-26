package com.example.thisorthat.network;

import com.example.thisorthat.model.Image;
import com.example.thisorthat.model.Post;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface PostApi {

    @Headers("Content-Type: image/jpeg")
    @POST("files/image/")
    Call<Image> uploadImage(@Body RequestBody image);

    @POST("classes/Post/")
    Call<Post> submitPost(@Body Post post);
}
