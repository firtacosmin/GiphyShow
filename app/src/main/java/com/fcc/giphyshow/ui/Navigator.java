package com.fcc.giphyshow.ui;

import android.os.Bundle;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.fcc.giphyshow.ui.details.view.GifDetailsController;
import com.fcc.giphyshow.ui.search.view.SearchViewController;

import javax.inject.Inject;

/**
 * Created by firta on 8/5/2017.
 * The class that will be in charge of the navigation within the app
 */

public class Navigator {

    private Router router;
    private SearchViewController listViewController;

    @Inject
    public Navigator(Router router, SearchViewController listViewController){

        this.router = router;
        this.listViewController = listViewController;
    }


    public void navigateToLandingPage(){
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(listViewController).tag(SearchViewController.TAG));
        }
    }

    public void navigateToDetails(Bundle args){
        router.pushController(RouterTransaction
                .with(new GifDetailsController(args))
                .pushChangeHandler(new FadeChangeHandler())
                .popChangeHandler(new FadeChangeHandler()));

    }

    public boolean onBackPressed() {
        return  !router.handleBack();
    }
}
