package com.example.thisorthat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thisorthat.R;
import com.example.thisorthat.model.User;
import com.example.thisorthat.network.RetrofitClient;
import com.example.thisorthat.network.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        edUsername = findViewById(R.id.edLoginUsername);
        edPassword = findViewById(R.id.edLoginPassword);
    }

    public void login(View view) {
        if (!checkFormIsValid()) {
        } else {
            callLoginApi();
        }

    }

    private boolean checkFormIsValid() {
        if (TextUtils.isEmpty(edUsername.getText())) {
            edUsername.setError("Username is required.");
            return false;
        } else if (TextUtils.isEmpty(edPassword.getText())) {
            edPassword.setError("Password is required.");
            return false;
        } else return true;
    }

    private void callLoginApi() {
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
        Call<User> userCall = userApi.login(edUsername.getText().toString(), edPassword.getText().toString());
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", edUsername.getText().toString());
                    startActivity(intent);
                } else {//TODO: Get messages like this user already exists.
                    Toast.makeText(LoginActivity.this, "Login failed: Invalid username or password.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }
        });
    }


    public void signup(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
