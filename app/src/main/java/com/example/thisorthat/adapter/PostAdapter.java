package com.example.thisorthat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thisorthat.R;
import com.example.thisorthat.model.Post;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    ArrayList<Post> postArrayList;
    LayoutInflater inflater;


    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        inflater = LayoutInflater.from(context);
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_post_card, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post selectedProduct = postArrayList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUsername, tvPostDate, tvPostDescription;
        ImageView ivProfileImage, ivLeftImage, ivRightImage;
        Button btnVoteThis, btnVoteThat;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.username);
            tvPostDate = itemView.findViewById(R.id.postDate);
            tvPostDescription = itemView.findViewById(R.id.postDescription);
            ivProfileImage = itemView.findViewById(R.id.profileImage);
            ivLeftImage = itemView.findViewById(R.id.leftImagePlace);
            ivRightImage = itemView.findViewById(R.id.rightImagePlace);
            btnVoteThis = itemView.findViewById(R.id.voteThisButton);
            btnVoteThat = itemView.findViewById(R.id.voteThatButton);

            btnVoteThis.setOnClickListener(this);
            btnVoteThat.setOnClickListener(this);
        }

        public void setData(Post selectedPost, int position) {

            tvUsername.setText(selectedPost.getUser().getUsername());
            tvPostDate.setText(selectedPost.getCreatedAt());
            tvPostDescription.setText(selectedPost.getDescription());
            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);

        }


        @Override
        public void onClick(View v) {


        }


    }
}
