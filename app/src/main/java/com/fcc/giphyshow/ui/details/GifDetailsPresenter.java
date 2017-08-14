package com.fcc.giphyshow.ui.details;

import android.content.Context;
import android.content.Intent;

import com.fcc.giphyshow.R;
import com.fcc.giphyshow.ui.details.model.FavoritesDAO;
import com.fcc.giphyshow.ui.details.model.VotesDAO;
import com.fcc.giphyshow.ui.details.view.GifDetailsView;
import com.fcc.giphyshow.ui.search.model.request.SearchElement;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.fcc.giphyshow.ui.details.view.GifDetailsView.PERMISSIONS_GRANTED;
import static com.fcc.giphyshow.ui.details.view.GifDetailsView.PERMISSIONS_REFUSED;
import static com.fcc.giphyshow.ui.details.view.GifDetailsView.VIEW_CREATED;
import static com.fcc.giphyshow.ui.details.view.GifDetailsView.VIEW_DESTROYED;


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
    private FavoritesDAO favDao;

    private boolean isFav = false;

    private String thumbURL;
    private String gifID;
    private String mp4URL;
    private String gifURL;

    private CompositeDisposable subscriptions = new CompositeDisposable();


    public GifDetailsPresenter(GifDetailsView view, FavoritesDAO favDao) {
        this.view = view;
        element = (SearchElement) view.getArgs().getSerializable(ELEMENT_BUNDLE_KEY);
        this.favDao = favDao;

        gifID = element.getId();
        thumbURL = element.getImages().getFixedHeightStill().getUrl();
        mp4URL = element.getImages().getLooping().getMp4();
        gifURL = element.getImages().getOriginal().getUrl();

        view.printLogo(thumbURL);
        view.startPlayer(mp4URL);
        subscriptions.add(view.observeViewState()
            .subscribe(this::viewStateUpdate));
        isFav = favDao.getFavorite(gifID) != null;
        view.setFavState(isFav);
    }

    private void viewStateUpdate(String state) {

        switch (state) {
            case VIEW_CREATED:
                onCreateView();
                break;
            case VIEW_DESTROYED:
                onDestroyView();
                break;
            case PERMISSIONS_REFUSED:
                view.printError(R.string.cannor_download_permission);
                break;
            case PERMISSIONS_GRANTED:
                view.startDownloadService(gifURL, gifID);
                break;
        }

    }

    public void onDestroyView() {
        subscriptions.clear();
    }

    public void onCreateView() {
        subscriptions.add(observerFavBtn());
        subscriptions.add(observeDownloadBtn());
        subscriptions.add(observeDownloadProgress());
    }

    private Disposable observerFavBtn() {

        return view.observerFavClick()
                .subscribe(
                        __ -> favBtnClick()
                );

    }

    private void favBtnClick() {
        if ( isFav ){
            removeFav();
        }else{
            setFav();
        }
        view.setFavState(isFav);
    }

    private Disposable observeDownloadProgress() {

        return view.observeDownloadProgress()
                .subscribe(this::newDownloadProgress);

    }

    private void newDownloadProgress(Integer progress) {
        if ( progress == 100 ){
            view.deactivateDownloadBtn();
        }
    }

    private Disposable observeDownloadBtn() {
        return view.observeDownloadBtn().subscribe(
                __ -> downloadImage()
        );
    }

    /**
     * the method that will save the gif into the local storage
     */
    private void downloadImage() {
        if ( view.checkPermission() ) {
            view.startDownloadService(gifURL, gifID);
        }else{
            view.requestPermission();
        }


    }


    private void setFav(){
        favDao.addFavGif(gifID, thumbURL, mp4URL, gifURL);
        isFav = true;
    }
    private void removeFav(){
        favDao.removeFavGif(gifID);
        isFav = false;
    }
}
