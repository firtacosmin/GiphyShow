package com.fcc.giphyshow.ui.details;

import android.os.Bundle;

import io.reactivex.Observable;

/**
 * Created by firta on 8/5/2017.
 * This is the interface that will be implemented by the details view to export actions to {@link GifDetailsPresenter}
 *
 */

public interface GifDetailsView {

    void printLogo(String logoURL);
    void startPlayer(String playbackURL);
    void setUpVoteCount(String count);
    void setDownVoteCount(String count);
    Bundle getArgs();


    Observable<Object> observeUpVoteBtn();

    Observable<Object> observeDownVoteBtn();
}
