package com.fcc.giphyshow.data.search;

import android.database.Observable;

import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.data.search.request.SearchResponse;
import com.fcc.giphyshow.data.search.request.SearchService;
import com.fcc.giphyshow.di.application.MainAppScope;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firta on 8/5/2017.
 *
 * Class that will provide the search information.
 * It will decide if the data is retrieved from the server or from the database
 *
 */
//@MainAppScope
public class SearchRepo {

    /**
     * the maximum size of the local cache
     */
    private static final int CACHE_MAX_SIZE = 5;
    /**
     * the current position in the local cache
     */
    private int cacheLastUsedPosition = 0;

    private SearchService api;
    /**
     * array that will contain the cached searches.
     */
    private ArrayList<String> queryCache = new ArrayList<>(CACHE_MAX_SIZE);

    /**
     * array that will contain the cached responses
     */
    private ArrayList<SearchResponse> responseCache = new ArrayList<>(CACHE_MAX_SIZE);

    /**
     * the dispozable that will be used to dispose the subscriptions on {@link SearchResponse}
     */
    private Disposable disposableFromLastSearch;
    /**
     * {@link SearchResponseSingle} to return when waiting for {@link SearchService} response
     */
    private SearchResponseSingle searchResponseSingle;

    /**
     * storing the last requested Search query to be used when returning from api
     */
    private String lastRequestedSearch = "";

    @Inject
    public SearchRepo(SearchService api){

        this.api = api;
    }

    /**
     * the method that will return an
     * @param q the query that will be passed to the server
     * @return a {@link Single<SearchResponse>} the
     */
    public Single<SearchResponse> getItems(String q){

        if ( queryCache.contains(q) ){
            /*the q is in the local cache*/
            return getFromCache(q);
        }else {
            /*if a disposable is not null then we are waiting for a search result to be received
            * so we will dispose the single*/
            return getFromAPI(q);
        }


    }

    /**
     * will get the {@link SearchResponse} from {@link SearchService} API
     * @param q the query string
     * @return the {@link Single} where to subscribe to get the response
     */
    private Single<SearchResponse> getFromAPI(String q) {
        disposeFromAPI();
        lastRequestedSearch = q;
        disposableFromLastSearch = api.searchFor(q, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        this::receivedResponseFromApi,
                        this::receivedErrorFromApi
                );
        searchResponseSingle = new SearchResponseSingle();
        return Single.create(searchResponseSingle);
    }

    /**
     * will get the {@link SearchResponse} from the saved cache
     * @param q the query to search for
     * @return the {@link Single} where to subscribe to get the response
     */
    private Single<SearchResponse> getFromCache(String q) {
        SearchResponseSingle searchResponseSingle = new SearchResponseSingle();
        Single<SearchResponse> singleToReturn = Single.create(searchResponseSingle);
        searchResponseSingle.announceSuccess(responseCache.get(queryCache.indexOf(q)));
        return singleToReturn;
    }

    private void receivedErrorFromApi(Throwable error) {
        searchResponseSingle.announceError(error);
        disposeFromAPI();
    }

    private void receivedResponseFromApi(SearchResponse response) {
        storeResponseIntoCache(response);
        searchResponseSingle.announceSuccess(response);
        disposeFromAPI();
    }

    /**
     * will store the response into {@link #responseCache} and
     * the {@link #lastRequestedSearch} into {@link #queryCache}
     * @param response the {@link SearchResponse} to store
     */
    private void storeResponseIntoCache(SearchResponse response) {

        cacheLastUsedPosition++;
        if ( cacheLastUsedPosition >= CACHE_MAX_SIZE ){
            cacheLastUsedPosition = 0;
        }
        if ( queryCache.size() == CACHE_MAX_SIZE ) {
            /*if the cache is full the replace existing items*/
            queryCache.add(cacheLastUsedPosition, lastRequestedSearch);
            responseCache.add(cacheLastUsedPosition, response);
        }else{
            /*if cache not full the add the data*/
            queryCache.add( lastRequestedSearch );
            responseCache.add( response );
        }



    }

    private void disposeFromAPI(){
        if ( disposableFromLastSearch != null && !disposableFromLastSearch.isDisposed() ) {
            disposableFromLastSearch.dispose();
        }
        disposableFromLastSearch = null;
    }


}
