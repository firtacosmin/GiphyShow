package com.fcc.giphyshow.ui.search;

/**
 * Created by firta on 8/5/2017.
 * The interface that represents the view for the {@link SearchViewPresenter}
 */

public interface SearchView {

    void showLoading();
    void hideLoading();
    void newItemCount(int itemCount);
    void showError(String message);
    void bindPresenter(SearchViewPresenter preseter);


}
