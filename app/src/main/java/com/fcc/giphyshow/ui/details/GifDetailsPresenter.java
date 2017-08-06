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

    private GifDetailsView view;
    private SearchElement element;

    private int upVotes = 0;
    private int downVotes = 0;


    public GifDetailsPresenter(GifDetailsView view) {
        this.view = view;
        element = (SearchElement) view.getArgs().getSerializable(ELEMENT_BUNDLE_KEY);
        view.printLogo(element.getImages().getOriginal().getUrl());
        view.startPlayer(element.getImages().getLooping().getMp4());
    }

    public void downVoteClicked() {

        downVotes ++;
        view.setDownVoteCount(downVotes+"");

    }

    public void upVoteClick() {

        upVotes ++;
        view.setUpVoteCount(upVotes+"");

    }
}
