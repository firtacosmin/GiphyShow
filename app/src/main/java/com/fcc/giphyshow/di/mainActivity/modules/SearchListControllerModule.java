package com.fcc.giphyshow.di.mainActivity.modules;

import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.ui.search.SearchViewController;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/5/2017.
 */

@Module
public class SearchListControllerModule {

    @Provides
    @MainActivityScope
    public SearchViewController provideSearchViewController(){
        return new SearchViewController();
    }

}
