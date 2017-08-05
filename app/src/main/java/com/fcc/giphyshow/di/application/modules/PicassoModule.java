package com.fcc.giphyshow.di.application.modules;

import android.content.Context;

import com.fcc.giphyshow.di.application.MainAppScope;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by firta on 8/5/2017.
 * The module that will inject the  {@link com.squareup.picasso.Picasso} instance
 */

@Module
public class PicassoModule {

    @MainAppScope
    @Provides
    public Picasso providePicasso(Context context, OkHttp3Downloader downloader){
        return new Picasso.Builder(context)
                .downloader(downloader)
                .build();
    }

    @MainAppScope
    @Provides
    public OkHttp3Downloader downloader(OkHttpClient client){
        return new OkHttp3Downloader(client);
    }

}
