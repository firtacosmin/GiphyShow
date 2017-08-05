package com.fcc.giphyshow.utils;

import okhttp3.HttpUrl;

/**
 * Created by firta on 8/5/2017.
 * a static class that contains all the platform dependent strings
 */

public class PlatformSettings {

    public static final String GIPHY_BASE_URL = "http://api.giphy.com/";
    public static final String GIPHY_SEARCH_PATH = "v1/gifs/search";
    public static final String GIPHY_APP_KEY = "7c938d250839433c94358ab49540ebf2";

    public static String getGiphySearchUrl(){
        return GIPHY_BASE_URL+GIPHY_SEARCH_PATH;
    }

    public static String getGiphyBaseUrl() {
        return GIPHY_BASE_URL;
    }
}
