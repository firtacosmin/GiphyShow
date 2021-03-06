package com.fcc.giphyshow.ui.details.view;

import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

public class PlayerViewManager implements PlaybackControlView.VisibilityListener, ExoPlayer.EventListener {
    public static final String TAG = "PlayerViewManager";

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

    private int playerState = 0;
    private MediaSource videoSource;


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
            videoSource = new ExtractorMediaSource(Uri.parse(mediaURL),
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
        playerView.setUseController(false);
        playerView.requestFocus();
        playerView.hideController();
        playerView.setOnTouchListener((view, motionEvent) -> {
            if ( motionEvent.getAction() == MotionEvent.ACTION_DOWN ){
                switchPlay();
            }
            return false;
        });
        playerView.setOnClickListener(
                view -> switchPlay()
        );
    }

    private void switchPlay() {

        if ( player.getPlayWhenReady() ){
            player.setPlayWhenReady(false);
        }else{
            player.setPlayWhenReady(true);
        }

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
            player.addListener(this);

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

        Log.d(TAG,"::onVisibilityChange");
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

        Log.d(TAG,"::onTimelineChanged");
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        Log.d(TAG,"::onTracksChanged");
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

        Log.d(TAG,"::onLoadingChanged");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ( playbackState == ExoPlayer.STATE_READY )
        {

            Log.d(TAG,"::onPlayerStateChanged STATE_READY");
        }else if ( playbackState == ExoPlayer.STATE_BUFFERING ) {
            Log.d(TAG, "::onPlayerStateChanged STATE_BUFFERING");
        }else if ( playbackState == ExoPlayer.STATE_ENDED ){
            player.setPlayWhenReady(true);
            player.seekTo(0);
//            player.prepare(videoSource);
            Log.d(TAG,"::onPlayerStateChanged STATE_ENDED");
        }else if ( playbackState == ExoPlayer.STATE_IDLE ){

            Log.d(TAG,"::onPlayerStateChanged STATE_IDLE");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

        Log.d(TAG,"::onPlayerError");
        error.printStackTrace();
    }

    @Override
    public void onPositionDiscontinuity() {

        Log.d(TAG,"::onPositionDiscontinuity");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        Log.d(TAG,"::onPlaybackParametersChanged");
    }
}
