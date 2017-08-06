package com.fcc.giphyshow.di.mainActivity.modules;

import com.fcc.giphyshow.data.votes.Votes;
import com.fcc.giphyshow.di.details.GifDetailsScope;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by firta on 8/6/2017.
 * The module that will provide {@link Box<Votes>} instance.
 */

@Module
public class VotesBoxModule {

    @MainActivityScope
    @Provides
    public Box<Votes> provideVotesBox(BoxStore boxStore){
        return boxStore.boxFor(Votes.class);
    }

}
