package com.fcc.giphyshow.ui.details;

import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.data.votes.VotesDAO;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


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
        view.printLogo(element.getImages().getOriginal().getUrl());
        view.startPlayer(element.getImages().getLooping().getMp4());


        getVotesFromBox();

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
    }
}
