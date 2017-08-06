package com.fcc.giphyshow.di;

import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.data.votes.Votes;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.di.mainActivity.modules.RouterModule;
import com.fcc.giphyshow.di.mainActivity.modules.SearchListAdapterModule;
import com.fcc.giphyshow.di.mainActivity.modules.SearchListControllerModule;
import com.fcc.giphyshow.di.mainActivity.modules.VotesBoxModule;
import com.fcc.giphyshow.ui.details.GifDetailsController;
import com.fcc.giphyshow.ui.search.SearchViewController;
import com.squareup.picasso.Picasso;

import dagger.Component;
import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by firta on 8/5/2017.
 * The component for {@link com.fcc.giphyshow.MainActivity}
 */
@MainActivityScope
@Component(modules = {
        RouterModule.class,
        SearchListControllerModule.class,
        SearchListAdapterModule.class,
        VotesBoxModule.class
}, dependencies = MainAppComponent.class)
public interface MainActivityComponent {
    Picasso getPicasso();
    Box<Votes> getVotesBox();
    void injectMainActivity(MainActivity activity);
    void injectSearchViewController(SearchViewController searchViewController);

}
