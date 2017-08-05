package com.fcc.giphyshow.di.mainActivity.modules;

import com.bluelinelabs.conductor.Router;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/5/2017.
 * The module that will inject the {@link Router} instance.
 * The instance will be added externally
 */

@Module
public class RouterModule {

    private final Router router;

    public RouterModule(Router router) {
        this.router = router;
    }

    @Provides
    @MainActivityScope
    public Router provideRouter(){
        return router;
    }

}
