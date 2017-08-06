package com.fcc.giphyshow.ui.details;

import android.net.Uri;

import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.ui.BuildConfig;
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

public class PlayerViewManager implements PlaybackControlView.VisibilityListener {

    private final SimpleExoPlayerView playerView;
    private SimpleExoPlayer player;
    private int resumeWindow;
    private long resumePosition;
    /**
     * the last media that has beed set to play when {@link #startPlayer(String)} was called.
     */
    private String mediaURL;

    /**
     * flag set when the {@link #parentAttached()} is called and reset when {@link #parentViewDetached()} is called
     * will tell the {@link PlayerViewManager} when the parent is in focus;
     */
    private boolean isParentAttached = false;




    public PlayerViewManager(SimpleExoPlayerView playerView) {
        this.playerView = playerView;
        initPlayerView();
    }


    public void startPlayer(String mediaURL){
        this.mediaURL = mediaURL;
        if ( isParentAttached ) {
            /* will start the player only if the parent is in focus. If the parent is not in focus
            will save the mediaURL and will start it when it will be in focus  */
            initializePlayer();
            // Measures bandwidth during playback. Can be null if not required.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(playerView.getContext(),
                    Util.getUserAgent(playerView.getContext(), com.fcc.giphyshow.BuildConfig.APPLICATION_ID), bandwidthMeter);
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(mediaURL),
                    dataSourceFactory, extractorsFactory, null, null);
            /*check if the player needs to resume position*/
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }
            // Prepare the player with the source.
            player.prepare(videoSource);
        }
    }

    public void parentViewDetached(){
        isParentAttached = false;
        if ( player != null ){
            updateResumePosition();
            player.release();
            player = null;
        }
    }

    public void parentAttached(){
        isParentAttached = true;
        if ( mediaURL != null && !mediaURL.isEmpty() ) {
            startPlayer(mediaURL);
        }
    }


    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }



    private void initPlayerView(){
        clearResumePosition();
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

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }
    @Override
    public void onVisibilityChange(int visibility) {

    }


}
