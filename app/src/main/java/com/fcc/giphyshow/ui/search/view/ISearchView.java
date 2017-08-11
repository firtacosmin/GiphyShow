package com.fcc.giphyshow.ui.search.view;

import com.fcc.giphyshow.ui.search.SearchViewPresenter;

import io.reactivex.Observable;

/**
 * Created by firta on 8/5/2017.
 * The interface that represents the view for the {@link SearchViewPresenter}
 */

public interface ISearchView {

    void showLoading();
    void hideLoading();
    void newItemCount(int itemCount);
    void showError(int messageID);
    void bindPresenter(SearchViewPresenter preseter);

    Observable<String> searchEvent();

    String getQueryTest();


}
