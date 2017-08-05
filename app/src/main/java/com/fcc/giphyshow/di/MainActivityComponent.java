package com.fcc.giphyshow.di;

import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.di.mainActivity.modules.SearchListAdapterModule;
import com.fcc.giphyshow.di.mainActivity.modules.SearchListControllerModule;
import com.fcc.giphyshow.ui.search.SearchViewController;

import dagger.Component;

/**
 * Created by firta on 8/5/2017.
 * The component for {@link com.fcc.giphyshow.MainActivity}
 */
@MainActivityScope
@Component(modules = {SearchListControllerModule.class, SearchListAdapterModule.class}, dependencies = MainAppComponent.class)
public interface MainActivityComponent {

    void injectMainActivity(MainActivity activity);

    void injectSearchViewController(SearchViewController searchViewController);
}
