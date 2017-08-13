package com.fcc.giphyshow.ui.search.model;


import com.fcc.giphyshow.ui.search.model.request.SearchResponse;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Created by firta on 8/5/2017.
 *
 */

public class SearchResponseSingle<T> implements SingleOnSubscribe<T> {

    private SingleEmitter<T> emitter;

    private T responseToAnnounce;
    private Throwable errorToAnnounce;

    public SearchResponseSingle(){

    }

    /**
     * method that will call the {@link SingleEmitter#onSuccess(Object)} method with
     * the passed {@link SearchResponse}
     * @param response
     */
    public void announceSuccess(T response){
        if ( emitter != null ){
            emitter.onSuccess(response);
        }else{
            responseToAnnounce = response;
        }
        emitter = null;
    }


    public void announceError(Throwable e){
        if ( emitter != null ){
            emitter.onError(e);
        }else{
            errorToAnnounce = e;
        }
        emitter = null;
    }
    @Override
    public void subscribe(@NonNull SingleEmitter<T> e) throws Exception {
        if ( responseToAnnounce != null ){
            e.onSuccess(responseToAnnounce);
            responseToAnnounce = null;
        } else if (errorToAnnounce != null) {
            e.onError(errorToAnnounce);
            errorToAnnounce = null;
        }else{
            emitter = e;
        }
    }
}
