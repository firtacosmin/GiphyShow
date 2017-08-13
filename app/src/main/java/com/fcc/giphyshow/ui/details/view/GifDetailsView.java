package com.fcc.giphyshow.ui.details.view;

import android.os.Bundle;

import com.fcc.giphyshow.ui.details.GifDetailsPresenter;

import io.reactivex.Observable;

/**
 * Created by firta on 8/5/2017.
 * This is the interface that will be implemented by the details view to export actions to {@link GifDetailsPresenter}
 *
 */

public interface GifDetailsView {

    public static final String VIEW_CREATED = "VIEW_CREATED";
    public static final String VIEW_DESTROYED = "VIEW_DESTROYED";
    public static final String PERMISSIONS_GRANTED = "PERMISSIONS_GRANTED";
    public static final String PERMISSIONS_REFUSED = "PERMISSIONS_REFUSED";

    boolean checkPermission();

    void requestPermission();

    void printLogo(String logoURL);
    void startPlayer(String playbackURL);
    void setUpVoteCount(String count);
    void setDownVoteCount(String count);
    Bundle getArgs();


    Observable<Object> observeUpVoteBtn();
    Observable<Object> observeDownloadBtn();
    Observable<Object> observeDownVoteBtn();

    Observable<String> observeViewState();


    void startDownloadService(String imageURL, String imageName);

    void printError(int errorMessageStringId);

    Observable<Integer> observeDownloadProgress();

    void deactivateDownloadBtn();
}
