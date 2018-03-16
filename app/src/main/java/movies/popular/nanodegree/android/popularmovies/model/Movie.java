package movies.popular.nanodegree.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String title;
    private String thumbnail;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private String runtime;

    public Movie(String id, String title, String thumbnail,
                 String overview, double voteAverage,
                 String releaseDate, String runtime) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.runtime = runtime+"min";
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.thumbnail = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readDouble();
        this.releaseDate = in.readString();
        this.runtime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(thumbnail);
        parcel.writeString(overview);
        parcel.writeDouble(voteAverage);
        parcel.writeString(releaseDate);
        parcel.writeString(runtime);
    }

    @Override
    public int describeContents() { return 0; }

    public final static Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}