package com.fcc.giphyshow.ui.details.view;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.fcc.giphyshow.R;
import com.squareup.picasso.Picasso;

/**
 * Created by firta on 8/12/2017.
 * Will hold the state of the details view and will be binned with the layout
 */

public class DetailsViewState {


    public String upVote_count = "0";
    public String downVote_count = "0";
    public boolean downloadImgActive = true;
    public String logoImageURL = "";


    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if ( imageUrl != null && !imageUrl.isEmpty() ) {
            Picasso.with(view.getContext())
                    .load(imageUrl)
                    .into(view);
        }
    }

    public int getDownloadImgTint()
    {
        if ( downloadImgActive ){
            return R.color.black;
        }else{
            return R.color.white;
        }
    }


    public void setUpVote_count(String upVote_count) {
        this.upVote_count = upVote_count;
    }

    public void setDownVote_count(String downVote_count) {
        this.downVote_count = downVote_count;
    }

    public void setDownloadImgActive(boolean downloadImgActive) {
        this.downloadImgActive = downloadImgActive;
    }

    public void setLogoImageURL(String logoImageURL) {
        this.logoImageURL = logoImageURL;
    }



}
