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
import com.fcc.giphyshow.di.DaggerGifDetailsComponent;
import com.fcc.giphyshow.di.GifDetailsComponent;
import com.fcc.giphyshow.di.details.modules.GifDetailsViewModule;
import com.fcc.giphyshow.ui.search.SearchView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by firta on 8/5/2017.
 * The {@link Controller} that will display details of a selected gif and will
 * give the possibility to play and to rate it
 */

public class GifDetailsController extends Controller implements GifDetailsView{
    @BindView(R.id.logo_img) ImageView logoImg;
    @BindView(R.id.playerFrame) FrameLayout playerFrame;
    @BindView(R.id.player_view) SimpleExoPlayerView playerView;
    @BindView(R.id.upvote_count_txt) TextView upVoteTxt;
    @BindView(R.id.downvote_count_txt) TextView downVoteTxt;

    @Inject
    Picasso picasso;

    @Inject
    GifDetailsPresenter presenter;


    PlayerViewManager playerViewManager;


    private String playerURL = "";


    public GifDetailsController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_view_details, container, false);
        ButterKnife.bind(this, view);

        playerViewManager = new PlayerViewManager(playerView);

        GifDetailsComponent diComponent = DaggerGifDetailsComponent
                .builder()
                .mainActivityComponent(((MainActivity)getActivity()).getDiComponent())
                .gifDetailsViewModule(new GifDetailsViewModule(this))
                .build();
        diComponent.injectInController(this);

        /*will hold the view when detached */
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);


        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        playerViewManager.parentAttached();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        playerViewManager.parentViewDetached();
    }


    @OnClick(R.id.downvote_img)
    public void onDownVoteClick(){
        presenter.downVoteClicked();
    }


    @OnClick( R.id.upvote_img )
    public void onUpVoteClick(){
        presenter.upVoteClick();
    }


    /**********
     *
     *
     * Implementation from {@link GifDetailsView}
     *
     *
     **********************/


    @Override
    public void printLogo(String logoURL) {
        picasso.load(logoURL).into(logoImg);
    }

    @Override
    public void startPlayer(String playbackURL) {
        playerViewManager.startPlayer(playbackURL);
    }

    @Override
    public void setUpVoteCount(String count) {
        upVoteTxt.setText(count);
    }

    @Override
    public void setDownVoteCount(String count) {
        downVoteTxt.setText(count);
    }
}
