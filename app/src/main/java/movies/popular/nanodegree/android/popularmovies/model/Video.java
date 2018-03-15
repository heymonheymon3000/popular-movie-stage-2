package movies.popular.nanodegree.android.popularmovies.model;

public class Video {
    String id;
    String key;
    String name;

    public Video(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
