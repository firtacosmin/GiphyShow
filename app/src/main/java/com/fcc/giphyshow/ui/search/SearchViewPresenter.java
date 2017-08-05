package com.fcc.giphyshow.ui.search;

import android.support.v7.widget.RecyclerView;

import com.fcc.giphyshow.data.search.SearchRepo;
import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.data.search.request.SearchResponse;
import com.fcc.giphyshow.di.application.MainAppScope;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by firta on 8/5/2017.
 * The presenter for the {@link SearchViewController}
 */
@MainActivityScope
public class SearchViewPresenter {


    private SearchView searchView;
    private SearchRepo repo;
    private SearchResponse result;

    @Inject
    public SearchViewPresenter(SearchRepo repo){
        this.repo = repo;
    }

    public void bindView(SearchView searchView){
        this.searchView = searchView;
    }

    public void searchPressed(String searchWord){
        searchView.showLoading();
        repo.getItems(searchWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::gotTheItems,
                        this::errorOnGettingItems
                );
    }

    /**
     * callback when the items are received from the {@link SearchRepo]}
     * @param result the results
     */
    private void gotTheItems(SearchResponse result) {
        this.result = result;
        searchView.hideLoading();
        searchView.newItemCount(result.getPagination().getCount());
    }

    /**
     * callback when the response retrieved from {@link SearchRepo} is with error
     * @param error the {@link Throwable} error
     */
    private void errorOnGettingItems(Throwable error) {

    }


    /**
     * method called from {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * It is used by the {@link SearchViewPresenter} to update the view information
     * @param view the {@link SearchItemView} to be updated
     * @param position the position the view is on
     */
    public void onBindViewHolder(SearchItemView view, int position) {
        if ( result != null && result.getPagination().getCount() > 0 ){
            SearchElement el = result.getData().get(position);
            view.setDesc(el.getSlug());
            view.setImage(el.getImages().getOriginal().getUrl());
        }
    }
}
