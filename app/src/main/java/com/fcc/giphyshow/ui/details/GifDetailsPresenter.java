package com.fcc.giphyshow.ui.details;

import com.fcc.giphyshow.data.search.request.SearchElement;

import javax.inject.Inject;

/**
 * Created by firta on 8/5/2017.
 * The presenter class for {@link GifDetailsView}
 */

public class GifDetailsPresenter {

    /**
     * the kwy used to store the {@link SearchElement} into the {@link android.os.Bundle} that is passed
     * to the {@link GifDetailsView}
     */
    public static final String ELEMENT_BUNDLE_KEY = "ELEMENT_BUNDLE_KEY";

    private GifDetailsView gifDetailsController;
    private SearchElement element;

    public GifDetailsPresenter(GifDetailsView view) {
        this.gifDetailsController = view;
        element = (SearchElement) gifDetailsController.getArgs().getSerializable(ELEMENT_BUNDLE_KEY);
        view.printLogo(element.getImages().getOriginal().getUrl());

    }

}
