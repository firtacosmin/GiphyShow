package com.fcc.giphyshow.ui.details;

import android.os.Bundle;

/**
 * Created by firta on 8/5/2017.
 * This is the interface that will be implemented by the details view to export actions to {@link GifDetailsPresenter}
 *
 */

public interface GifDetailsView {

    void printTitle(String title);
    void printLogo(String logoURL);
    void startPlayer(String playbackURL);
    Bundle getArgs();


}