package com.fcc.giphyshow.data.search;

import com.fcc.giphyshow.data.search.request.SearchResponse;
import com.fcc.giphyshow.data.search.request.SearchService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firta on 8/6/2017.
 * The repository that will offer access to pages of {@link com.fcc.giphyshow.data.search.request.SearchResponse}
 * It will cache a number of {@link #CACHE_MAX_SIZE} queries, each with a number of {@link #CACHE_PAGE_MAX_SIZE} pages
 * Every page will contain {@link #ELEMENTS_PER_PAGE} elements
 * When the number of queries is exceeded the oldest query is removed from cache
 */

public class SearchRepoPage {


    /**
     * the maximum size of the local cache
     */
    private static final int CACHE_MAX_SIZE = 5;

    /**
     * the maximum size of paged for every query
     */
    public static final int CACHE_PAGE_MAX_SIZE = 10;
    public static final int ELEMENTS_PER_PAGE = 20;


    /**
     * {@link SearchResponseSingle} to return when waiting for {@link SearchService} response
     */
    private SearchResponseSingle <List<SearchResponse>> searchResponseSingle;
    /**
     * the disposable that will be used to dispose the subscriptions on {@link SearchResponse}
     */
    private Disposable disposableFromLastSearch;
    /**
     * the {@link SearchService} from where to get the {@link com.fcc.giphyshow.data.search.request.SearchResponse} data
     */
    private SearchService api;

    @Inject
    public SearchRepoPage(SearchService api){
        this.api = api;
    }


    /**
     * will contain the a {@link java.util.HashMap} of {@link java.util.HashMap} of {@link SearchResponse}
     * that represent the pages for every query
     */
    private Map<String, Map<Integer,SearchResponse>> cacheMap = new ConcurrentHashMap<>();


    /**
     * will get the passed page position for the passed query position and will return the
     * {@link Single} object which to observe for the {@link List} of all the retrieved pager untill now
     * @param q the query to search for
     * @param pagePos the position of the requested list
     * @return the {@link Single} object to be observed for the {@link List<SearchResponse>} of pages
     */
    public Single<List<SearchResponse>> getPage(String q, int pagePos){
        if ( haveCache(q, pagePos) ){
            return getPageFromCache(q, pagePos);
        }else{
            return getPageFromApi(q, pagePos);
        }
    }

    private Single<List<SearchResponse>> getPageFromApi(String q, int pagePos) {
        disposeFromAPI();
        disposableFromLastSearch = api.searchFor(q, pagePos*ELEMENTS_PER_PAGE, ELEMENTS_PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        data -> retrievedDataFromApi(data, q, pagePos),
                        this::retrievedErrorFromApi
                );
        searchResponseSingle = new SearchResponseSingle<>();
        return Single.create(searchResponseSingle);
    }

    private void retrievedErrorFromApi(Throwable error) {

        searchResponseSingle.announceError(error);
        disposeFromAPI();

    }

    private void retrievedDataFromApi(SearchResponse data, String q, int pagePos) {
        if ( cacheMap.containsKey(q) ){
            /*if the cache contains the query then add the new page*/
            addNewPageToCache(cacheMap.get(q), data, pagePos);
        }else{
            clearTheOldestCacheIfNeeded();
            addNewData(data, q, pagePos);
        }
        List<SearchResponse> returnList = new ArrayList<>();
        returnList.addAll(cacheMap.get(q).values());
        searchResponseSingle.announceSuccess(returnList);
        disposeFromAPI();
    }

    private void addNewPageToCache(Map<Integer, SearchResponse> pagesMap, SearchResponse data, int pagePos) {
        if ( pagesMap.size() >= CACHE_PAGE_MAX_SIZE ) {
            /*if the pages map is full then clear the oldest one*/
            int oldestPos = getOldestPage(pagesMap);
            pagesMap.remove(oldestPos);
        }
        pagesMap.put(pagePos, data);

    }

    private void addNewData(SearchResponse data, String q, int pagePos) {
        Map<Integer, SearchResponse> pages = new ConcurrentHashMap<>();
        pages.put(pagePos, data);
        cacheMap.put(q, pages);
    }

    private void clearTheOldestCacheIfNeeded() {
    /*if the cache does not have the key in it the will add it*/
        if ( cacheMap.size() >= CACHE_MAX_SIZE ){
            /*if cache if full then remove the oldest cached value*/
            Iterator<String> it = cacheMap.keySet().iterator();
            String oldestCache = it.next();
            SearchResponse oldestResponse = cacheMap.get(oldestCache).get(getOldestSearchResponse(oldestCache));
            while( it.hasNext() ){
                String query = it.next();
                int oldestPagePosition = getOldestSearchResponse(query);
                SearchResponse oldestRsp = cacheMap.get(query).get(oldestPagePosition);
                if ( oldestRsp.getMeta().getTimestamp() < oldestResponse.getMeta().getTimestamp() ){
                    /*if the new oldest element is older then the last oldest element
                    * then switch*/
                    oldestCache = query;
                    oldestResponse = oldestRsp;
                }
            }
            /*will remove the oldest cached element*/
            cacheMap.remove(oldestCache);
        }
    }

    /**
     * method that will go through all the pages in thw {@link @cacheMap} element with query key
     * and return the oldest one by checking the {@link com.fcc.giphyshow.data.search.request.Meta}
     * field in the {@link SearchResponse}
     * @param query the query to look for
     * @return the position of the oldest {@link SearchResponse}
     */
    private int getOldestSearchResponse(String query) {

        Map<Integer, SearchResponse> pages = cacheMap.get(query);
        return getOldestPage(pages);
    }

    private int getOldestPage(Map<Integer, SearchResponse> pages) {
        long oldestTimestamp = System.currentTimeMillis();
        int oldestKey = -1;
        for (Integer key : pages.keySet()) {
            SearchResponse rsp = pages.get(key);
            if ( rsp.getMeta().getTimestamp() < oldestTimestamp ){
                /*if this is older then the last one
                 * then replace them
                 */
                oldestTimestamp = rsp.getMeta().getTimestamp();
                oldestKey = key;
            }
        }

        return oldestKey;
    }

    private Single<List<SearchResponse>> getPageFromCache(String q, int pagePos) {

        ArrayList<SearchResponse> listToReturn = new ArrayList<>();
        listToReturn.addAll(cacheMap.get(q).values());

        SearchResponseSingle<List<SearchResponse>> searchResponseSingle = new SearchResponseSingle<>();
        Single<List<SearchResponse>> singleToReturn = Single.create(searchResponseSingle);
        searchResponseSingle.announceSuccess(listToReturn);
        return singleToReturn;
    }

    private boolean haveCache(String q, int pagePos) {
        if ( cacheMap.containsKey(q) ){
            /*if there are pages for the query then check if the page is cached*/
            if (cacheMap.get(q).keySet().contains(pagePos)){
                /*if the page is in the cache then return true*/
                return true;
            }
        }
        return false;
    }

    private void disposeFromAPI(){
        if ( disposableFromLastSearch != null && !disposableFromLastSearch.isDisposed() ) {
            disposableFromLastSearch.dispose();
        }
        disposableFromLastSearch = null;
    }

}
