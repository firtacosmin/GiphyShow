package com.fcc.giphyshow.di.application.modules;

import com.fcc.giphyshow.di.application.MainAppScope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/3/2017.
 */

@Module
public class GsonModule {
    @MainAppScope
    @Provides
    public Gson provideGson(){
        return new GsonBuilder().create();
    }

}
