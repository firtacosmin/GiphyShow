package com.fcc.giphyshow.ui.details;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.fcc.giphyshow.R;
import com.fcc.giphyshow.ui.details.model.Download;
import com.fcc.giphyshow.ui.details.model.DownloadGifServiceInterface;
import com.fcc.giphyshow.ui.details.view.GifDetailsController;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by firta on 8/12/2017.
 * The service that will download the gif
 */
public class DownloadService extends IntentService {

    public static final String IMAGE_TO_DOWNLOAD_URL = "IMAGE_TO_DOWNLOAD_URL";
    public static final String IMAGE_NAME_TO_DOWNLOAD_URL = "IMAGE_NAME_TO_DOWNLOAD_URL";
    public static final String DOWNLOAD_PROGRESS_KEY = "download";
    private File destinationFile;
    private String contentText;

    public DownloadService() {
        super("Download Service");
    }

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;

    private String imageUrl = "";
    private String imageName = "";


    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        contentText = getApplicationContext().getString(R.string.dowmload_notif_content_text);
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle(getApplicationContext().getString(R.string.app_name))
                .setContentText(contentText)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        imageUrl = intent.getStringExtra(IMAGE_TO_DOWNLOAD_URL);
        imageName = intent.getStringExtra(IMAGE_NAME_TO_DOWNLOAD_URL);
        if ( imageUrl != null && !imageUrl.isEmpty() &&
                imageName != null && !imageName.isEmpty()) {
            initDownload();
        }

    }

    private void initDownload(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getBaseURL())
                .build();

        DownloadGifServiceInterface retrofitInterface = retrofit.create(DownloadGifServiceInterface.class);

        Call<ResponseBody> request = retrofitInterface.downloadGif();
        try {

            downloadFile(request.execute().body());

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private String getBaseURL() {

        return imageUrl.substring(0, imageUrl.lastIndexOf("/")+1);

    }

    private void downloadFile(ResponseBody body) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = createDestinationFile();
        if ( outputFile == null ){
            sendDownloadError();
            return;
        }else if ( outputFile.exists() ){
            onDownloadComplete();
            return;
        }
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));

            int progress = (int) ((total * 100) / fileSize);

            long currentTime = System.currentTimeMillis() - startTime;

            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendDownloadError() {

        writeNotifMessage(R.string.error_on_download_message);


    }


    private File createDestinationFile() {
        String destinationFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+"/"+getApplicationContext().getResources().getString(R.string.app_name);
        File destination = new File(destinationFolder);
        boolean folderExists = true;
        if ( !destination.exists() ){
            folderExists = destination.mkdirs();
        }
        if ( folderExists ){
            destinationFile = new File(destinationFolder, getFileName());
            return destinationFile;
        }

        return null;
    }

    private String getFileName() {

        return imageName+".gif";
    }

    private void sendNotification(Download download){

        sendIntent(download);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText(contentText+" "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){

        Intent intent = new Intent(GifDetailsController.MESSAGE_PROGRESS);
        intent.putExtra(DOWNLOAD_PROGRESS_KEY,download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);
        assignClickToNotif();
        writeNotifMessage(R.string.download_completed_notif_memssage);
    }

    private void writeNotifMessage(int error_on_download_message) {
        writeNotifMessage(getApplicationContext().getString(error_on_download_message));
    }

    private void writeNotifMessage(String message) {
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText(message);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void assignClickToNotif(){

        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".ui.details.GifFileProvider", destinationFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationBuilder.setContentIntent(pIntent);

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}