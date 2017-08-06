package com.fcc.giphyshow.di.details.modules;

import com.fcc.giphyshow.data.votes.Votes;
import com.fcc.giphyshow.data.votes.VotesDAO;
import com.fcc.giphyshow.di.details.GifDetailsScope;
import com.fcc.giphyshow.ui.details.GifDetailsPresenter;
import com.fcc.giphyshow.ui.details.GifDetailsView;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;

/**
 * Created by firta on 8/6/2017.
 * The module that will inject {@link GifDetailsPresenterModule}
 */

@Module
public class GifDetailsPresenterModule {

    @GifDetailsScope
    @Provides
    public GifDetailsPresenter provideGifDetailsPresenter(GifDetailsView view, VotesDAO votesDAO){
        return  new GifDetailsPresenter(view, votesDAO);
    }
}
