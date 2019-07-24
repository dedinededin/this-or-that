package com.example.thisorthat.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    private static String BASE_URL = "https://parseapi.back4app.com/";
    private static final String APPLICATION_ID = "uQ50ISOzXGQOuu0cTXE3vEb6BdbWP37gavPXWlY7";
    private static final String REST_API_KEY = "q7GDdeAj7wMrptvcKN9XkslxB8bu452YjT7ASLkM";


    public static Retrofit getClient() {

        if (retrofit == null) {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("X-Parse-Application-Id", APPLICATION_ID)
                            .header("X-Parse-REST-API-Key", REST_API_KEY)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
            OkHttpClient client = httpClient.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }


}
