package com.tanshul.player.utils;

/**
 * Created by tansdeva on 20/12/17.
 * Constants to be used throught the application
 */

public class Constants {
    //Application constants
    public static final String SHARED_PREF_NAME = "OlaPlayPref";
    public static final String URL_IMAGE_OPTIM = "https://img.gs/ftwwpkfzdp/";
    public static final String SERVER_URL = "http://starlord.hackerearth.com/studio";
    public static final String SONG_DATA = "AUDIO_SONG_DATA";
    public static final float ASPECT_RATIO = 0.75f;
    public static final float VIEW_START = 0.3f;
    public static final float VIEW_FINAL = 0.7f;
    public final static int LOADER_DURATION = 1000;
    public static final int PAGEING_THRESHOLD = 5;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int GALLERY_REQUEST = 2022;

    //Media playback status
    public static final int NOT_PLAYING = 0;
    public static final int BUFFERING = 1;
    public static final int PLAYING = 2;
    public static final int PAUSED = 3;
    public static final int FINISHED = 4;
}
