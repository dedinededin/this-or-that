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

public class SignupActivity extends AppCompatActivity {

    EditText edUsername, edPassword, edPasswordConfirm, edEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edUsername = findViewById(R.id.edSignupUsername);
        edPassword = findViewById(R.id.edSignupPassword);
        edPasswordConfirm = findViewById(R.id.edSignupPasswordConfirm);
        edEmail = findViewById(R.id.edSignupEmail);
    }

    public void signup(View view) {
        if (TextUtils.isEmpty(edUsername.getText())) {
            edUsername.setError("Username is required.");
        } else if (TextUtils.isEmpty(edPassword.getText())) {
            edPassword.setError("Password is required.");
        } else if (TextUtils.isEmpty(edPasswordConfirm.getText())) {
            edPassword.setError("Password Confirm is required.");
        } else if (TextUtils.isEmpty(edEmail.getText())) {
            edPassword.setError("Email is required.");
        } else if (edPassword.getText().equals(edPasswordConfirm.getText())) {
            edPassword.setError("Password does not match.");
        } else {
            UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
            User newUser = new User(edUsername.getText().toString(), edPassword.getText().toString(), edEmail.getText().toString());
            Call<User> userCall = userApi.signup(newUser);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("username", edUsername.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignupActivity.this, "An error happened", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }
    }
}
