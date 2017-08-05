package com.fcc.giphyshow.di.details.modules;

import com.fcc.giphyshow.di.details.GifDetailsScope;
import com.fcc.giphyshow.ui.details.GifDetailsView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/6/2017.
 * The module that will inject the {@link com.fcc.giphyshow.ui.details.GifDetailsView} instance
 */

@Module
public class GifDetailsViewModule {

    private final GifDetailsView view;

    public GifDetailsViewModule(GifDetailsView view) {
        this.view = view;
    }

    @GifDetailsScope
    @Provides
    public GifDetailsView provideGifDetailsView(){
        return view;
    }
}
