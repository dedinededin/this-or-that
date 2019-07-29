package com.example.thisorthat.activity.main;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.thisorthat.R;
import com.example.thisorthat.helper.ImageFilePath;
import com.example.thisorthat.model.Image;
import com.example.thisorthat.model.Post;
import com.example.thisorthat.model.UserId;
import com.example.thisorthat.network.PostApi;
import com.example.thisorthat.network.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_GET_LEFT_IMAGE = 100;
    private static final int REQUEST_GET_RIGHT_IMAGE = 101;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    ImageView leftImageView, rightImageView;
    File leftImageFile, rightImageFile;
    Button submitButton;
    EditText postDesc;
    PostApi api;
    ProgressDialog progressDialog;
    Image leftImage, rightImage;

    public AddPostFragment() {
        // Required empty public constructor
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_GET_LEFT_IMAGE
            );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        verifyStoragePermissions(getActivity());
    }

    private void initialize() {
        postDesc = getView().findViewById(R.id.postDescription);
        leftImageView = getView().findViewById(R.id.leftImagePlace);
        leftImageView.setOnClickListener(this);
        rightImageView = getView().findViewById(R.id.rightImagePlace);
        rightImageView.setOnClickListener(this);
        submitButton = getView().findViewById(R.id.submitPost);
        submitButton.setOnClickListener(this);

        TextView toolbarTitle = getActivity().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("New Post");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(android.R.color.transparent);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void onClick(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        switch (view.getId()) {
            case R.id.leftImagePlace:
                startActivityForResult(gallery, REQUEST_GET_LEFT_IMAGE);
                break;
            case R.id.rightImagePlace:
                startActivityForResult(gallery, REQUEST_GET_RIGHT_IMAGE);
                break;
            case R.id.submitPost:
                submitLeftAndRightImage();
                break;
        }
    }

    private void submitLeftAndRightImage() {
        api = RetrofitClient.getClient().create(PostApi.class);

        showProgressDialog();
        leftImage = uploadAndReturnImage(api, leftImageFile);
        rightImage = uploadAndReturnImage(api, rightImageFile);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Your images are uploading, please wait.");
            progressDialog.show();
        }
    }

    private void submitPost() {

        Post newPost = new Post();
        newPost.setDescription(postDesc.getText().toString());
        newPost.setLeftImage(leftImage);
        newPost.setRightImage(rightImage);
        newPost.setUserId(new UserId(getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("objectId", "Error")));//TODO: change this
        newPost.setUsername(getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("username", "Error"));
        Call<Post> postCall = api.submitPost(newPost);

        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private Image uploadAndReturnImage(PostApi api, File image) {
        final Image imageToReturn = new Image();
        final RequestBody requestBody = RequestBody
                .create(MediaType.parse("image/jpeg"), image);
        Call<Image> uploadImage = api.uploadImage(requestBody);
        uploadImage.enqueue(new Callback<Image>() {
            @Override
            public void onResponse(Call<Image> call, Response<Image> response) {
                imageToReturn.setName(response.body().getName());
                imageToReturn.setUrl(response.body().getUrl());
                if (leftImage != null && rightImage != null) {
                    submitPost();
                }
            }

            @Override
            public void onFailure(Call<Image> call, Throwable t) {

            }
        });

        return imageToReturn;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_GET_LEFT_IMAGE) {

            String filePath = ImageFilePath.getPath(getActivity(), data.getData());
            leftImageFile = new File(filePath);

            leftImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            leftImageView.setBackgroundResource(0);
            leftImageView.setImageURI(data.getData());
            leftImageView.setVisibility(View.VISIBLE);

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_GET_RIGHT_IMAGE) {

            String filePath = ImageFilePath.getPath(getActivity(), data.getData());
            rightImageFile = new File(filePath);

            rightImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            rightImageView.setBackgroundResource(0);
            rightImageView.setImageURI(data.getData());
            rightImageView.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }


    }

}
