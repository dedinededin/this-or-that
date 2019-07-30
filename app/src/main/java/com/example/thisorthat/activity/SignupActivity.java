package com.example.thisorthat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.thisorthat.R;
import com.example.thisorthat.activity.main.MainActivity;
import com.example.thisorthat.model.User;
import com.example.thisorthat.network.RetrofitClient;
import com.example.thisorthat.network.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    EditText edUsername, edPassword, edPasswordConfirm, edEmail;
    ScrollView scrollView;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        attachKeyboardListeners();
        initialize();
    }

    private void initialize() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        attachKeyboardListeners();

        edUsername = findViewById(R.id.edSignupUsername);
        edPassword = findViewById(R.id.edSignupPassword);
        edPasswordConfirm = findViewById(R.id.edSignupPasswordConfirm);
        edEmail = findViewById(R.id.edSignupEmail);
        scrollView = findViewById(R.id.scrollView);
        signUpButton = findViewById(R.id.signUpButton);
    }

    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        // do things when keyboard is shown
        scrollView.smoothScrollTo(0, signUpButton.getTop());
    }

    @Override
    protected void onHideKeyboard() {
        // do things when keyboard is hidden
    }

    public void signup(View view) {
        if (!checkFormIsValid()) {
        } else {
            callSignUpApi();
        }
    }

    private boolean checkFormIsValid() {
        if (TextUtils.isEmpty(edUsername.getText())) {
            edUsername.setError("Username is required.");
            return false;
        } else if (TextUtils.isEmpty(edPassword.getText())) {
            edPassword.setError("Password is required.");
            return false;
        } else if (TextUtils.isEmpty(edPasswordConfirm.getText())) {
            edPassword.setError("Password Confirm is required.");
            return false;
        } else if (TextUtils.isEmpty(edEmail.getText())) {
            edPassword.setError("Email is required.");
            return false;
        } else if (edPassword.getText().equals(edPasswordConfirm.getText())) {
            edPassword.setError("Password does not match.");
            return false;
        } else return true;
    }

    private void callSignUpApi() {
        UserApi userApi = RetrofitClient.getClient().create(UserApi.class);
        User newUser = new User(edUsername.getText().toString(), edPassword.getText().toString(), edEmail.getText().toString());
        Call<User> userCall = userApi.signup(newUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 201) {

                    SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("sessionToken", response.body().getSessionToken());
                    editor.putString("objectId", response.body().getObjectId());
                    editor.commit();

                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    finish();
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
