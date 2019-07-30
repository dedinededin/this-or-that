package com.example.thisorthat.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.thisorthat.R;
import com.example.thisorthat.helper.DoubleClickListener;
import com.example.thisorthat.model.Post;
import com.example.thisorthat.model.Vote;
import com.example.thisorthat.network.PostApi;
import com.example.thisorthat.network.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    PostApi api;
    private List<Post> postList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Post> data) {
        this.mInflater = LayoutInflater.from(context);
        this.postList = data;
        api = RetrofitClient.getClient().create(PostApi.class);
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
        TextView tvUsername, tvPostDate, tvPostDescription, tvThisCount, tvThatCount, tvTotalCount;
        ImageView ivLeftImage, ivRightImage, ivVoteThis, ivVoteThat;
        CircleImageView ivProfileImage;
        ProgressBar voteProgress;
        LinearLayout voteResults;

        ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.username);
            tvPostDate = itemView.findViewById(R.id.postDate);
            tvPostDescription = itemView.findViewById(R.id.postDescription);
            tvThisCount = itemView.findViewById(R.id.thisCount);
            tvThatCount = itemView.findViewById(R.id.thatCount);
            tvTotalCount = itemView.findViewById(R.id.totalCount);

            ivProfileImage = itemView.findViewById(R.id.profileImage);
            ivVoteThis = itemView.findViewById(R.id.voteThisImage);
            ivVoteThat = itemView.findViewById(R.id.voteThatImage);

            voteResults = itemView.findViewById(R.id.voteResults);
            voteProgress = itemView.findViewById(R.id.voteProgress);
            voteProgress.setProgressDrawable(itemView.getContext().getResources().getDrawable(R.drawable.custom_progressbar));

            ivLeftImage = itemView.findViewById(R.id.leftImagePlace);
            ivRightImage = itemView.findViewById(R.id.rightImagePlace);
        }

        private void voteProgressCalculator(Post selectedPost) {
            int thisCount = selectedPost.getThisCount();
            int thatCount = selectedPost.getThatCount();
            int totalCount = thisCount + thatCount;
            int progressPercentage = 0;

            if (totalCount != 0) {
                progressPercentage = (thisCount * 100) / totalCount;
                tvThisCount.setText(String.format("This %d%%", progressPercentage));
                tvThatCount.setText(String.format("That %d%%", (100 - progressPercentage)));
                tvTotalCount.setText(String.format("Total Votes: %d", totalCount));
                voteProgress.setProgress(progressPercentage);
            }
            if (totalCount == 0) {
                voteProgress.setVisibility(View.GONE);
                tvThisCount.setText("");
                tvThatCount.setText("");
                tvTotalCount.setText(String.format("No votes yet!", totalCount));
            }
        }

        public void setData(Post selectedPost, int position) {
            if (selectedPost.getUser() != null) {
                tvUsername.setText(selectedPost.getUser().getUsername());
            } else tvUsername.setText(selectedPost.getUsername());


            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            try {
                Date date = format.parse(selectedPost.getCreatedAt());
                tvPostDate.setText(DateUtils.getRelativeTimeSpanString(date.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }


            SpannableStringBuilder builder = new SpannableStringBuilder();
            StyleSpan boldSpan = new StyleSpan(android.graphics.Typeface.BOLD);
            builder.append(selectedPost.getUsername(), boldSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    .append(" ")
                    .append(selectedPost.getDescription());

            tvPostDescription.setText(builder);
            Picasso.get().load("https://api.adorable.io/avatars/285/abott@adorable.png").fit().into(ivProfileImage);
            Picasso.get().load(selectedPost.getLeftImage().getUrl()).fit().into(ivLeftImage);
            Picasso.get().load(selectedPost.getRightImage().getUrl()).fit().into(ivRightImage);

            voteProgressCalculator(selectedPost);
            userVoteAction(selectedPost);
        }

        private void userVoteAction(final Post selectedPost) {
            ivLeftImage.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onDoubleClick() {
                    if (ivVoteThis.getVisibility() == View.VISIBLE) {
                        ivVoteThis.setVisibility(View.INVISIBLE);
                    } else if (ivVoteThis.getVisibility() == View.INVISIBLE) {
                        ivVoteThis.setVisibility(View.VISIBLE);
                        Call<Post> voteCall = api.updateVote(selectedPost.getObjectId(), new Vote(Vote.INCREMENT_THIS));
                        voteCall.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                selectedPost.setThisCount(response.body().getThisCount());
                                voteProgressCalculator(selectedPost);
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {

                            }
                        });
                    }
                    ivVoteThat.setVisibility(View.INVISIBLE);
                    voteResults.setVisibility(View.VISIBLE);
                }
            });
            ivRightImage.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onDoubleClick() {
                    if (ivVoteThat.getVisibility() == View.VISIBLE) {
                        ivVoteThat.setVisibility(View.INVISIBLE);
                    } else if (ivVoteThat.getVisibility() == View.INVISIBLE) {
                        ivVoteThat.setVisibility(View.VISIBLE);
                        Call<Post> voteCall = api.updateVote(selectedPost.getObjectId(), new Vote(Vote.INCREMENT_THAT));
                        voteCall.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                selectedPost.setThatCount(response.body().getThatCount());
                                voteProgressCalculator(selectedPost);
                            }

                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                            }
                        });
                    }
                    ivVoteThis.setVisibility(View.INVISIBLE);
                    voteResults.setVisibility(View.VISIBLE);
                    voteProgress.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}