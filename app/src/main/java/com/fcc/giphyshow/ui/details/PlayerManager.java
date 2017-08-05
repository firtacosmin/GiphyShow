package com.fcc.giphyshow.ui.details;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by firta on 8/6/2017.
 * A class that will do the player functionality and logic
 */

public class PlayerManager implements PlaybackControlView.VisibilityListener {

    private final SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;


    public PlayerManager(SimpleExoPlayerView playerView) {
        this.playerView = playerView;
        initPlayerView();
    }


    public void startPlayer(String mediaURL){
        initializePlayer();
// Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(playerView.getContext(),
                Util.getUserAgent(playerView.getContext(), "yourApplicationName"), bandwidthMeter);
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mediaURL),
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        player.prepare(videoSource);
    }

    public void parentViewDestroyed(){
        if ( player != null ){
            player.release();
        }
    }

    public void parentViewDetached(){
        if ( player != null ){
            player.stop();
        }
    }


    private void initPlayerView(){
        playerView.setControllerVisibilityListener(this);
        playerView.requestFocus();
    }



    private void initializePlayer() {
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            player =
                    ExoPlayerFactory.newSimpleInstance(playerView.getContext(), trackSelector);
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);

        }
        if (needNewPlayer){
        }
    }


    @Override
    public void onVisibilityChange(int visibility) {

    }


}
