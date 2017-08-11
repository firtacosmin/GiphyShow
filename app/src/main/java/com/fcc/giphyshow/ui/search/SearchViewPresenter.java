package com.fcc.giphyshow.ui.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.bluelinelabs.conductor.Router;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.data.search.SearchRepo;
import com.fcc.giphyshow.data.search.SearchRepoPage;
import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.data.search.request.SearchResponse;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.fcc.giphyshow.ui.Navigator;
import com.fcc.giphyshow.ui.search.view.ISearchView;
import com.fcc.giphyshow.ui.search.view.SearchItemView;
import com.fcc.giphyshow.ui.search.view.SearchViewController;

import java.util.List;

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


    private ISearchView searchView;
    private SearchRepoPage repo;
    private Router router;
    private Navigator navigator;
    private List<SearchResponse> result;
    private int itemCount;

    @Inject
    public SearchViewPresenter(SearchRepoPage repo, Router router, Navigator navigator) {
        this.repo = repo;
        this.router = router;
        this.navigator = navigator;
    }

    public void bindView(ISearchView searchView) {
        this.searchView = searchView;
        searchView.searchEvent()
                .doOnNext(__ -> searchView.showLoading())
                .map(__ -> searchView.getQueryTest())
                .observeOn(Schedulers.io())
                .switchMap( query -> repo.getPage(query, 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::gotTheItems,
                        this::errorOnGettingItems
                );
    }

    public void searchPressed(String searchWord) {
        searchView.showLoading();
        requestPage(0);
    }


    private void requestPage(int pageNo) {
        repo.getPage(searchView.getQueryTest(), pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::gotTheItems,
                        this::errorOnGettingItems
                );
    }

    /**
     * callback when the items are received from the {@link SearchRepo]}
     *
     * @param result the results
     */
    private void gotTheItems(List<SearchResponse> result) {
        this.result = result;
        searchView.hideLoading();
        itemCount = result.size() * SearchRepoPage.ELEMENTS_PER_PAGE;
        int retCount = 0;
        if (itemCount > result.get(0).getPagination().getTotalCount()) {
            itemCount = result.get(0).getPagination().getTotalCount();
            retCount = itemCount;
        } else if ( result.size() >= SearchRepoPage.CACHE_PAGE_MAX_SIZE ) {
            /*workaround to limit the page number*/
            retCount = itemCount;
        }else if ( itemCount > 0){
            /*if there are more elements on the server then add one to the size to print the loading last item*/
            retCount = itemCount + 1;
        }
        if ( itemCount > 0 ) {
            searchView.newItemCount(retCount);
        }else{
            searchView.showError(R.string.no_elements_error);
        }
    }

    /**
     * callback when the response retrieved from {@link SearchRepo} is with error
     *
     * @param error the {@link Throwable} error
     */
    private void errorOnGettingItems(Throwable error) {

    }


    /**
     * method called from {@link android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(RecyclerView.ViewHolder, int)}
     * It is used by the {@link SearchViewPresenter} to update the view information
     *
     * @param view     the {@link SearchItemView} to be updated
     * @param position the position the view is on
     */
    public void onBindViewHolder(SearchItemView view, int position) {
        if (result != null && itemCount > 0) {
            if (position >= itemCount) {
                /*on the last position so print loading*/
                view.showLoading();
                /*request new page*/
                if ( result.size() < SearchRepoPage.CACHE_PAGE_MAX_SIZE ) {
                    requestPage(result.size());
                }
            } else {
                SearchElement el = getElementForPosition(position);
                view.hideLoading();
                view.setDesc(el.getSlug());
                view.setImage(el.getImages().getFixedHeightStill().getUrl());
            }
        }
    }

    private SearchElement getElementForPosition(int position) {
        int pageNo = position / SearchRepoPage.ELEMENTS_PER_PAGE;
        int posInPage = position % SearchRepoPage.ELEMENTS_PER_PAGE;

        return result.get(pageNo).getData().get(posInPage);
    }

    /**
     * callback when an item of the list is clicked
     *
     * @param clickPosition the position of the clicked item
     */
    public void itemClicked(int clickPosition) {
        if (result != null && itemCount > 0 && clickPosition < itemCount) {

            SearchElement element = getElementForPosition(clickPosition);
            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable(ELEMENT_BUNDLE_KEY, element);
            navigator.navigateToDetails(dataToPass);
        }
    }
}
