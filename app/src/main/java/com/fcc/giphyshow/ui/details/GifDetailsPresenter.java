package com.fcc.giphyshow.ui.details;

import android.content.Context;
import android.content.Intent;

import com.fcc.giphyshow.R;
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
    private VotesDAO votesDAO;

    private int upVotes = 0;
    private int downVotes = 0;

    private CompositeDisposable subscriptions = new CompositeDisposable();


    public GifDetailsPresenter(GifDetailsView view, VotesDAO votesDAO) {
        this.view = view;
        element = (SearchElement) view.getArgs().getSerializable(ELEMENT_BUNDLE_KEY);
        this.votesDAO = votesDAO;
        view.printLogo(element.getImages().getFixedHeightStill().getUrl());
        view.startPlayer(element.getImages().getLooping().getMp4());
        subscriptions.add(view.observeViewState()
        .subscribe(this::viewStateUpdate));

        getVotesFromBox();

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
                view.startDownloadService(element.getImages().getOriginal().getUrl(), element.getId());
                break;
        }

    }

    /**
     * method that will get the votes for the printed element
     */
    private void getVotesFromBox() {
        String gifID = element.getId();
        downVotes = votesDAO.getDownVote(gifID);
        upVotes = votesDAO.getUpVote(gifID);
        /*int the vote values and display them*/
        displayVotes( upVotes, downVotes);
    }

    public void downVoteClicked() {

        downVotes++;
        view.setDownVoteCount(downVotes+"");
        saveVotesToDb();
    }

    public void upVoteClick() {

        upVotes++;
        view.setUpVoteCount(upVotes+"");
        saveVotesToDb();

    }

    private void displayVotes(int upvote, int downvote){
        view.setDownVoteCount(downvote+"");
        view.setUpVoteCount(upvote+"");
    }


    private void saveVotesToDb(){
        votesDAO.updateVote(element.getId(), upVotes, downVotes);
    }


    private Disposable observeUpVoteBtn(){
        return view.observeUpVoteBtn().subscribe(__ ->{
           upVoteClick();
        });
    }

    private Disposable observeDownVoteBtn(){
        return view.observeDownVoteBtn().subscribe( __ -> {
           downVoteClicked();
        });
    }

    public void onDestroyView() {
        subscriptions.clear();
    }

    public void onCreateView() {
        subscriptions.add(observeUpVoteBtn());
        subscriptions.add(observeDownVoteBtn());
        subscriptions.add(observeDownloadBtn());
        subscriptions.add(observeDownloadProgress());
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
            view.startDownloadService(element.getImages().getOriginal().getUrl(), element.getId());
        }else{
            view.requestPermission();
        }


    }
}
