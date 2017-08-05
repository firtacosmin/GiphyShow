package com.fcc.giphyshow.di.application.modules;

import android.content.Context;


import com.fcc.giphyshow.di.application.MainAppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/5/2017.
 * A module that provides the {@link com.fcc.giphyshow.MainApp} {@link android.content.Context}
 */

@Module
public class ContextModule {

    private final Context context;


    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @MainAppScope
    public Context provideContext(){
        return context;
    }
}
