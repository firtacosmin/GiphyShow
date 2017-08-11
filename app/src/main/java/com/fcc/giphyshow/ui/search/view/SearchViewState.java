package com.fcc.giphyshow.ui.search.view;

import android.view.View;

/**
 * Created by firta on 8/11/2017.
 * Class that contains the states of the views that are printed on the ISearchView
 */

public class SearchViewState {

    int errorVisibility = View.INVISIBLE;
    int listVisibility = View.INVISIBLE;
    int loadingVisibility = View.INVISIBLE;
    String errorMessage = "";

    public int getErrorVisibility() {
        return errorVisibility;
    }

    public void setErrorVisibility(int errorVisibility) {
        this.errorVisibility = errorVisibility;
    }

    public int getListVisibility() {
        return listVisibility;
    }

    public void setListVisibility(int listVisibility) {
        this.listVisibility = listVisibility;
    }

    public int getLoadingVisibility() {
        return loadingVisibility;
    }

    public void setLoadingVisibility(int loadingVisibility) {
        this.loadingVisibility = loadingVisibility;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
