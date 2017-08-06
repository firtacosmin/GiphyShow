package com.fcc.giphyshow.di.application.modules;

import android.content.Context;

import com.fcc.giphyshow.data.votes.MyObjectBox;
import com.fcc.giphyshow.di.application.MainAppScope;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;

/**
 * Created by firta on 8/6/2017.
 * The module that provide the {@link io.objectbox.BoxStore} instance
 */

@Module
public class ObjectBoxStoreModule {

    @MainAppScope
    @Provides
    public BoxStore provideBoxStore(Context context){
       return  MyObjectBox.builder().androidContext(context).build();
    }

}
