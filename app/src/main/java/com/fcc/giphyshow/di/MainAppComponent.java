package com.fcc.giphyshow.di;

import android.content.Context;

import com.fcc.giphyshow.MainApp;
import com.fcc.giphyshow.di.application.MainAppScope;
import com.fcc.giphyshow.di.application.modules.ApiModule;
import com.fcc.giphyshow.di.application.modules.ContextModule;
import com.fcc.giphyshow.di.application.modules.GsonModule;
import com.fcc.giphyshow.di.application.modules.NetworkModule;
import com.fcc.giphyshow.di.application.modules.ObjectBoxStoreModule;
import com.fcc.giphyshow.di.application.modules.PicassoModule;
import com.fcc.giphyshow.di.application.modules.RetrofitModule;
import com.fcc.giphyshow.ui.search.model.request.SearchService;
import com.squareup.picasso.Picasso;

import dagger.Component;
import io.objectbox.BoxStore;

/**
 * Created by firta on 8/5/2017.
 * The component that is associated with {@link com.fcc.giphyshow.MainApp}
 */

@MainAppScope
@Component(modules = {
        ObjectBoxStoreModule.class,
        ContextModule.class,
        ApiModule.class,
        PicassoModule.class,
        NetworkModule.class,
        GsonModule.class,
        RetrofitModule.class
}
)
public interface MainAppComponent {
    void inject(MainApp app);

    Picasso getPicasso();
    SearchService getSearchService();
    BoxStore getBoxStore();
    Context getContext();
}
