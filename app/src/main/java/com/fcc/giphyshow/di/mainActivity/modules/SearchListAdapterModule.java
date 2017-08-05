package com.fcc.giphyshow.di.mainActivity.modules;

import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.ui.search.SearchListAdapter;
import com.fcc.giphyshow.ui.search.SearchViewPresenter;
import com.squareup.picasso.Picasso;

import dagger.Module;
import dagger.Provides;

/**
 * Created by firta on 8/5/2017.
 */

@Module
public class SearchListAdapterModule {

    @Provides
    @MainActivityScope
    public SearchListAdapter provideSearchListAdapter(Picasso picasso, SearchViewPresenter presenter){
        return new SearchListAdapter(picasso, presenter);
    }
}
