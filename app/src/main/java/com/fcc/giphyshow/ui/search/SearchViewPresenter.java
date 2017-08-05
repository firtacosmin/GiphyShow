package com.fcc.giphyshow.ui.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler;
import com.fcc.giphyshow.data.search.SearchRepo;
import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.data.search.request.SearchResponse;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.ui.Navigator;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.fcc.giphyshow.ui.details.GifDetailsPresenter.ELEMENT_BUNDLE_KEY;

/**
 * Created by firta on 8/5/2017.
 * The presenter for the {@link SearchViewController}
 */
@MainActivityScope
public class SearchViewPresenter {


    private SearchView searchView;
    private SearchRepo repo;
    private Router router;
    private Navigator navigator;
    private SearchResponse result;

    @Inject
    public SearchViewPresenter(SearchRepo repo, Router router, Navigator navigator){
        this.repo = repo;
        this.router = router;
        this.navigator = navigator;
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

    /**
     * callback when an item of the list is clicked
     * @param adapterPosition the position of the clicked item
     */
    public void itemClicked(int adapterPosition) {
        if ( result != null && result.getPagination().getCount() > 0 ) {

            SearchElement element = result.getData().get(adapterPosition);
            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable(ELEMENT_BUNDLE_KEY, element);
            navigator.navigateToDetails(dataToPass);
        }
    }
}
