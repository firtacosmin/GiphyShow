package com.fcc.giphyshow.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.data.search.request.SearchElement;
import com.fcc.giphyshow.di.DaggerGifDetailsComponent;
import com.fcc.giphyshow.di.GifDetailsComponent;
import com.fcc.giphyshow.di.details.modules.GifDetailsViewModule;
import com.fcc.giphyshow.utils.BundleBuilder;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firta on 8/5/2017.
 * The {@link Controller} that will display details of a selected gif and will
 * give the possibility to play and to rate it
 */

public class GifDetailsController extends Controller implements GifDetailsView{
    @BindView(R.id.title_txt)
    TextView titleTxt;
    @BindView(R.id.logo_img)
    ImageView logoImg;
    @BindView(R.id.playerFrame)
    FrameLayout playerFrame;
    @BindView(R.id.player_view)
    SimpleExoPlayerView playerView;

    @Inject
    Picasso picasso;

    @Inject
    GifDetailsPresenter presenter;


    PlayerManager playerManager;


    private String playerURL = "";


    public GifDetailsController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_view_details, container, false);
        ButterKnife.bind(this, view);

        playerManager = new PlayerManager(playerView);

        GifDetailsComponent diComponent = DaggerGifDetailsComponent
                .builder()
                .mainActivityComponent(((MainActivity)getActivity()).getDiComponent())
                .gifDetailsViewModule(new GifDetailsViewModule(this))
                .build();
        diComponent.injectInController(this);


        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if ( !playerURL.isEmpty() ){
            playerManager.startPlayer(playerURL);
            playerURL = "";
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        playerManager.parentViewDetached();
    }

    @Override
    public void printTitle(String title) {
        titleTxt.setText(title);
    }

    @Override
    public void printLogo(String logoURL) {
        picasso.load(logoURL).into(logoImg);
    }

    @Override
    public void startPlayer(String playbackURL) {
        if ( isAttached() ) {
            playerURL = "";
            playerManager.startPlayer(playbackURL);
        }else{
            playerURL = playbackURL;
        }
    }
}