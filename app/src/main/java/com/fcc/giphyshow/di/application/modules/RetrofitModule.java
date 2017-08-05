package com.fcc.giphyshow.di.application.modules;

import com.fcc.giphyshow.di.application.ForGiphy;
import com.fcc.giphyshow.di.application.MainAppScope;
import com.fcc.giphyshow.utils.PlatformSettings;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by firta on 8/3/2017.
 *
 */

@Module
public class RetrofitModule {

    /**
     * @param okClient {@link OkHttpClient} client
     * @param gson the {@link Gson} instance
     * @return a root {@link Retrofit} client to be used by all other web services
     */
    @MainAppScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okClient, Gson gson){
        return new Retrofit.Builder()
                .baseUrl("http://127.0.0.1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okClient)
                .build();
    }

    /**
     *
     * @param retrofit a root {@link Retrofit} object that will be modified with {@link PlatformSettings#GIPHY_BASE_URL} base url
     * @return the {@link Retrofit} instance that points to the Giphy backend
     */
    @MainAppScope
    @Provides
    @ForGiphy
    public Retrofit provideGiphyRetrofit(Retrofit retrofit){
        return retrofit.newBuilder()
                .baseUrl(PlatformSettings.getGiphyBaseUrl())
                .build();
    }



}
