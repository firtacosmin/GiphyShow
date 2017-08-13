package com.fcc.giphyshow.ui.details.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fcc.giphyshow.ui.details.model.Download;

import static com.fcc.giphyshow.ui.details.view.GifDetailsController.MESSAGE_PROGRESS;
import static com.fcc.giphyshow.ui.details.DownloadService.DOWNLOAD_PROGRESS_KEY;


/**
 * Created by firta on 8/12/2017.
 * The  {@link BroadcastReceiver} that will receive the information about the download process
 */

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    private GifDetailsController controller;

    public DownloadBroadcastReceiver(GifDetailsController controller)
    {

        this.controller = controller;
    }

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra(DOWNLOAD_PROGRESS_KEY);
//                mProgressBar.setProgress(download.getProgress());
                controller.newDownloadProgress(download.getProgress());
            }
        }
    };
