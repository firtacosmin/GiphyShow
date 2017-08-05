package com.fcc.giphyshow;

import android.app.Application;

import com.fcc.giphyshow.data.search.request.SearchService;
import com.fcc.giphyshow.di.DaggerMainAppComponent;
import com.fcc.giphyshow.di.MainAppComponent;
import com.fcc.giphyshow.di.application.modules.ContextModule;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by firta on 8/5/2017.
 * The application class
 */

public class MainApp extends Application {

    private MainAppComponent diComponent;

    @Inject
    Picasso picasso;

    @Inject
    SearchService service;

    @Override
    public void onCreate() {
        super.onCreate();

        diComponent = DaggerMainAppComponent
                .builder()
                .contextModule(new ContextModule(this))
                .build();
        diComponent.inject(this);

    }

    public MainAppComponent getDiComponent() {
        return diComponent;
    }
}
