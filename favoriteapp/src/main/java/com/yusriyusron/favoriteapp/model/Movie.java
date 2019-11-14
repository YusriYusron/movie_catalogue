package com.yusriyusron.favoriteapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String imageMovie;
    private String titleMovie;
    private String overviewMovie;

    public Movie(String id, String imageMovie, String titleMovie, String overviewMovie) {
        this.id = id;
        this.imageMovie = imageMovie;
        this.titleMovie = titleMovie;
        this.overviewMovie = overviewMovie;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        imageMovie = in.readString();
        titleMovie = in.readString();
        overviewMovie = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getImageMovie() {
        return imageMovie;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public String getOverviewMovie() {
        return overviewMovie;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageMovie);
        parcel.writeString(titleMovie);
        parcel.writeString(overviewMovie);
    }
}
