package com.fcc.giphyshow.ui.details.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.conductor.Controller;
import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.databinding.ControllerViewDetailsBinding;
import com.fcc.giphyshow.di.DaggerGifDetailsComponent;
import com.fcc.giphyshow.di.GifDetailsComponent;
import com.fcc.giphyshow.di.details.modules.GifDetailsViewModule;
import com.fcc.giphyshow.ui.details.DownloadService;
import com.fcc.giphyshow.ui.details.GifDetailsPresenter;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static com.fcc.giphyshow.ui.details.DownloadService.IMAGE_NAME_TO_DOWNLOAD_URL;
import static com.fcc.giphyshow.ui.details.DownloadService.IMAGE_TO_DOWNLOAD_URL;

/**
 * Created by firta on 8/5/2017.
 * The {@link Controller} that will display details of a selected gif and will
 * give the possibility to play and to rate it
 */

public class GifDetailsController extends Controller implements GifDetailsView{
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;




    PublishSubject<String> controllerState = PublishSubject.create();

    /**
     * The {@link PublishSubject} that will inform about the download progress
     */
    PublishSubject<Integer> downloadProgress = PublishSubject.create();


    @Inject
    Picasso picasso;

    @Inject
    GifDetailsPresenter presenter;


    PlayerViewManager playerViewManager;


    private String playerURL = "";
    private ControllerViewDetailsBinding binding;

    private DetailsViewState detailsViewState;
    private MainActivity activity;


    public GifDetailsController(Bundle args) {
        super(args);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        detailsViewState = new DetailsViewState();

        activity = ((MainActivity)getActivity());

        binding = DataBindingUtil.inflate(inflater, R.layout.controller_view_details, container, false);
        binding.setState(detailsViewState);

        playerViewManager = new PlayerViewManager(binding.playerView);

        GifDetailsComponent diComponent = DaggerGifDetailsComponent
                .builder()
                .mainActivityComponent(activity.getDiComponent())
                .gifDetailsViewModule(new GifDetailsViewModule(this))
                .build();
        diComponent.injectInController(this);

        /*will hold the view when detached */
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);

        controllerState.onNext(VIEW_CREATED);
        return binding.getRoot();
    }


    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
//        presenter.onDestroyView();
        controllerState.onNext(VIEW_DESTROYED);
        activity = null;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    controllerState.onNext(PERMISSIONS_GRANTED);
                } else {
                    controllerState.onNext(PERMISSIONS_REFUSED);
                }
                break;
        }
    }



    public void newDownloadProgress(int progress) {
        downloadProgress.onNext(progress);
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
        detailsViewState.setLogoImageURL(logoURL);
        binding.invalidateAll();
//        picasso.load(logoURL).into(logoImg);
    }

    @Override
    public void startPlayer(String playbackURL) {
        playerViewManager.startPlayer(playbackURL);
    }

    @Override
    public void setUpVoteCount(String count) {
//        upVoteTxt.setText(count);
        detailsViewState.setUpVote_count(count);
        binding.invalidateAll();
    }

    @Override
    public void setDownVoteCount(String count) {
//        downVoteTxt.setText(count);
        detailsViewState.setDownVote_count(count);
        binding.invalidateAll();
    }

    @Override
    public Observable<Object> observeUpVoteBtn() {
        return RxView.clicks(binding.upvoteImg);
    }

    @Override
    public Observable<Object> observeDownloadBtn() {
        return RxView.clicks(binding.downloadImg);
    }

    @Override
    public Observable<Object> observeDownVoteBtn() {
        return RxView.clicks(binding.downvoteImg);
    }

    @Override
    public Observable<String> observeViewState() {
        return controllerState;
    }

    @Override
    public Observable<Integer> observeDownloadProgress(){
        return  downloadProgress;
    }

    @Override
    public void deactivateDownloadBtn() {
        detailsViewState.setDownloadImgActive(false);
        binding.invalidateAll();
    }

    @Override
    public void startDownloadService(String imageURL, String imageName) {
        Intent intent = new Intent(activity,DownloadService.class);
        intent.putExtra(IMAGE_TO_DOWNLOAD_URL, imageURL);
        intent.putExtra(IMAGE_NAME_TO_DOWNLOAD_URL, imageName);
        activity.startService(intent);
    }

    @Override
    public void printError(int errorMessageStringId) {
        Toast.makeText(activity, activity.getString(errorMessageStringId), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void requestPermission(){
//        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

}
