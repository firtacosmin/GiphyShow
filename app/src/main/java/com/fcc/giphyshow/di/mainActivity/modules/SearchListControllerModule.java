package com.fcc.giphyshow.di.mainActivity.modules;

import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.ui.search.view.ISearchView;
import com.fcc.giphyshow.ui.search.view.SearchViewController;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/5/2017.
 */

@Module
public class SearchListControllerModule {
    SearchViewController controller;

    public SearchListControllerModule(){
        controller = new SearchViewController();
    }

    @Provides
    @MainActivityScope
    public SearchViewController provideSearchViewController(){
        return controller;
    }

    @Provides
    @MainActivityScope
    public ISearchView provideSearchView(){
        return controller;
    }

}
