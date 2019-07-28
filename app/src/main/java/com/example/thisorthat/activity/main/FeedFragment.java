package com.example.thisorthat.activity.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thisorthat.R;
import com.example.thisorthat.adapter.MyRecyclerViewAdapter;
import com.example.thisorthat.model.Post;
import com.example.thisorthat.model.PostGetResult;
import com.example.thisorthat.model.User;
import com.example.thisorthat.network.PostApi;
import com.example.thisorthat.network.RetrofitClient;
import com.example.thisorthat.network.UserApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {
    PostApi postApi;
    UserApi userApi;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    ArrayList<Post> postArrayList;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {
        recyclerView = getView().findViewById(R.id.recyclerView);
        postArrayList = new ArrayList<>();
        getPosts();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyRecyclerViewAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void getPosts() {

        postApi = RetrofitClient.getClient().create(PostApi.class);
        userApi = RetrofitClient.getClient().create(UserApi.class);
        Call<PostGetResult> listCall = postApi.getPosts();
        listCall.enqueue(new Callback<PostGetResult>() {
            @Override
            public void onResponse(Call<PostGetResult> call, Response<PostGetResult> response) {

                ArrayList<Post> x = response.body().getResults();

                for (final Post p : x) {
                    Call<User> userCall = userApi.getUser(p.getUserId().getObjectId());
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            p.setUser(response.body());
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                }
                postArrayList.addAll(x);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<PostGetResult> call, Throwable t) {

            }
        });
    }

}
