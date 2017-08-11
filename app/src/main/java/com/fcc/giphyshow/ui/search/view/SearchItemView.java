package com.fcc.giphyshow.ui.search.view;

/**
 * Created by firta on 8/5/2017.
 * The interface that describes the actions of a search list item
 */

public interface SearchItemView {

    void setImage(String imageURL);
    void setDesc(String description);
    void showLoading();
    void hideLoading();

}
