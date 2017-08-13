package com.fcc.giphyshow.di;

import com.fcc.giphyshow.di.details.GifDetailsScope;
import com.fcc.giphyshow.di.details.modules.GifDetailsPresenterModule;
import com.fcc.giphyshow.di.details.modules.GifDetailsViewModule;
import com.fcc.giphyshow.ui.details.view.GifDetailsController;

import dagger.Component;

/**
 * Created by firta on 8/5/2017.
 * The component that will be used by the {@link GifDetailsController}
 */


@GifDetailsScope
@Component(modules = {
        GifDetailsPresenterModule.class,
        GifDetailsViewModule.class
}, dependencies = MainActivityComponent.class)
public interface GifDetailsComponent {
    void injectInController(GifDetailsController controller);
}
