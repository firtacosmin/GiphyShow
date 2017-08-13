package com.fcc.giphyshow.ui.details.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

/**
 * Created by firta on 8/12/2017.
 * teh {@link retrofit2.Retrofit} service interface for downloading the gifs
 */

public interface DownloadGifServiceInterface {

    @GET("giphy.gif")
    @Streaming
    Call<ResponseBody> downloadGif();

}
