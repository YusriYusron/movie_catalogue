package com.yusriyusron.tvmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TvShow implements Parcelable {
    private String id;
    private String imageTvShow;
    private String titleTvShow;
    private String overviewTvShow;

    public TvShow(String id, String imageTvShow, String titleTvShow, String overviewTvShow) {
        this.id = id;
        this.imageTvShow = imageTvShow;
        this.titleTvShow = titleTvShow;
        this.overviewTvShow = overviewTvShow;
    }

    protected TvShow(Parcel in) {
        id = in.readString();
        imageTvShow = in.readString();
        titleTvShow = in.readString();
        overviewTvShow = in.readString();
    }

    public static final Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getImageTvShow() {
        return imageTvShow;
    }

    public String getTitleTvShow() {
        return titleTvShow;
    }

    public String getOverviewTvShow() {
        return overviewTvShow;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imageTvShow);
        parcel.writeString(titleTvShow);
        parcel.writeString(overviewTvShow);
    }
}
