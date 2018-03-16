package movies.popular.nanodegree.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import movies.popular.nanodegree.android.popularmovies.model.Video;
import movies.popular.nanodegree.android.popularmovies.utilities.NetworkUtils;

public class TrailerAdapter extends
        RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final static String TAG = TrailerAdapter.class.getSimpleName();
    private List<Video> mVideos;
    private final Context mContext;

    public TrailerAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        view.setFocusable(true);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapterViewHolder holder,
                                 int position) {
      final Video video = mVideos.get(position);
      holder.videoName.setText(video.getName());
      holder.playTrailer.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Uri uri = NetworkUtils.buildYouTubeUri(video.getKey());
              Intent intent = new Intent(Intent.ACTION_VIEW, uri);
              if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                  mContext.startActivity(intent);
              }
          }
      });
    }

    @Override
    public int getItemCount() {
        if(null == mVideos) return 0;
        return mVideos.size();
    }

    public void setVideoData(List<Video> videoData) {
        mVideos = videoData;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        final ImageView playTrailer;
        final TextView videoName;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            playTrailer = view.findViewById(R.id.play_trailer);
            videoName = view.findViewById(R.id.trailer_name);
        }
    }
}
