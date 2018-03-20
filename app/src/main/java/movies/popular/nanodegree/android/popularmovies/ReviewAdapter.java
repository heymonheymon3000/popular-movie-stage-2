package movies.popular.nanodegree.android.popularmovies;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Review;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Review> mReviews;
    private final Context mContext;

    public ReviewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder,
                                 int position) {
        final Review review = mReviews.get(position);
        StringBuilder sb = new StringBuilder(review.getAuthor());
        sb.append(":");
        holder.authorTextView.setText(sb.toString());
        holder.contentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(null == mReviews) return 0;
        return mReviews.size();
    }

    public void setReviewData(List<Review> reviewData) {
        this.mReviews = reviewData;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView authorTextView;
        final TextView contentTextView;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            authorTextView = view.findViewById(R.id.review_author);
            contentTextView = view.findViewById(R.id.review_content);
        }
    }
}
