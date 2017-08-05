package com.fcc.giphyshow.di.application.modules;

import android.content.Context;

import com.fcc.giphyshow.BuildConfig;
import com.fcc.giphyshow.di.application.MainAppScope;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by firta on 8/3/2017.
 */

@Module
public class NetworkModule {

    @MainAppScope
    @Provides
    public HttpLoggingInterceptor provideLogingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BASIC:HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    @MainAppScope
    @Provides
    public Cache provideCache(Context c)
    {
        File cacheFile = new File(c.getCacheDir(),"intCache");
        long cacheSize = 10*1024*1024;
        Cache cache = new Cache(cacheFile, cacheSize);
        return cache;
    }

    @MainAppScope
    @Provides
    public OkHttpClient provideOkhttpClient(HttpLoggingInterceptor loggingInterceptor, Cache cache){
        return new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
    }

}
