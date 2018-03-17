package movies.popular.nanodegree.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private List<Movie> mMovieData;
    private final Context mContext;
    final private MovieAdapterOnClickListener mClickHandler;

    public interface MovieAdapterOnClickListener {
        void onClick(Movie movie);
    }

    public MovieAdapter(Context context,
                        MovieAdapterOnClickListener clickHandler) {
        this.mContext = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        Movie movieImagePath = mMovieData.get(position);
        Picasso.with(mContext).load(movieImagePath.getThumbnail())
                .into(movieAdapterViewHolder.mMovieThumbNailView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Movie> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public List<Movie> getMovieData() {
        return mMovieData;
    }

    public class MovieAdapterViewHolder extends
            RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mMovieThumbNailView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mMovieThumbNailView = view.findViewById(R.id.movie_thumbnail);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movie);
        }
    }
}