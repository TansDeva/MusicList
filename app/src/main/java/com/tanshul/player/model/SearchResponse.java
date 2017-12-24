package com.tanshul.player.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tanshul.player.utils.Constants;

/**
 * Created by tansdeva on 20/12/17.
 * Search service response DAO
 */

public class SearchResponse implements Parcelable {
    private String song;
    private String url;
    private String artists;
    private String cover_image;
    private int progress = 0;
    private int isPlaying = Constants.NOT_PLAYING;

    public String getSong() {
        return song;
    }

    public String getUrl() {
        return url;
    }

    public String getArtists() {
        return artists;
    }

    public String getCoverImage() {
        return cover_image;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPlaying() {
        return isPlaying;
    }

    public void setPlaying(int playing) {
        isPlaying = playing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.song);
        dest.writeString(this.url);
        dest.writeString(this.artists);
        dest.writeString(this.cover_image);
        dest.writeInt(this.progress);
        dest.writeInt(this.isPlaying);
    }

    public SearchResponse() {
    }

    protected SearchResponse(Parcel in) {
        this.song = in.readString();
        this.url = in.readString();
        this.artists = in.readString();
        this.cover_image = in.readString();
        this.progress = in.readInt();
        this.isPlaying = in.readInt();
    }

    public static final Parcelable.Creator<SearchResponse> CREATOR = new Parcelable.Creator<SearchResponse>() {
        @Override
        public SearchResponse createFromParcel(Parcel source) {
            return new SearchResponse(source);
        }

        @Override
        public SearchResponse[] newArray(int size) {
            return new SearchResponse[size];
        }
    };
}
