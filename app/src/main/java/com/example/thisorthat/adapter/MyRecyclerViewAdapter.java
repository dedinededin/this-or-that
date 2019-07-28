package com.example.thisorthat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thisorthat.R;
import com.example.thisorthat.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Post> postList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Post> data) {
        this.mInflater = LayoutInflater.from(context);
        this.postList = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_post_card, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post selectedProduct = postList.get(position);
        holder.setData(selectedProduct, position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return postList.size();
    }

    // convenience method for getting data at click position
    Post getItem(int id) {
        return postList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUsername, tvPostDate, tvPostDescription;
        ImageView ivProfileImage, ivLeftImage, ivRightImage;
        Button btnVoteThis, btnVoteThat;

        ViewHolder(View itemView) {
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
            if (selectedPost.getUser() != null) {
                tvUsername.setText(selectedPost.getUser().getUsername());
            } else tvUsername.setText("Not loaded yet..");
            tvPostDate.setText(selectedPost.getCreatedAt());
            tvPostDescription.setText(selectedPost.getDescription());
            Picasso.get().load("https://api.adorable.io/avatars/285/abott@adorable.png").fit().into(ivProfileImage);
            Picasso.get().load(selectedPost.getLeftImage().getUrl()).fit().into(ivLeftImage);
            Picasso.get().load(selectedPost.getRightImage().getUrl()).fit().into(ivRightImage);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}