package com.fcc.giphyshow.data.search.request;

import android.database.Observable;

import com.fcc.giphyshow.utils.PlatformSettings;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by firta on 8/5/2017.
 * The service that will provide the {@link SearchResponse} information from the server
 */

public interface SearchService {
//http://api.giphy.com/v1/gifs/search?api_key=7c938d250839433c94358ab49540ebf2&q=things&offset=26
    @GET(PlatformSettings.GIPHY_SEARCH_PATH+"?api_key="+PlatformSettings.GIPHY_APP_KEY)
    Single<SearchResponse> searchFor(@Query("q") String q, @Query("offset") int offset);


    @GET(PlatformSettings.GIPHY_SEARCH_PATH+"?api_key="+PlatformSettings.GIPHY_APP_KEY)
    Single<SearchResponse> searchFor(@Query("q") String q, @Query("offset") int offset, @Query("limit") int limit);

}
