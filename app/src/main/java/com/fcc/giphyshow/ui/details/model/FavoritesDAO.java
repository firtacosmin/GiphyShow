package com.fcc.giphyshow.ui.details.model;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.query.Query;

/**
 * Created by firta on 8/13/2017.
 * The DAO Object that will help with managing the actions with favorite gifs
 */

public class FavoritesDAO {

    private Box<Favorites> favoritesBox;


    @Inject
    public FavoritesDAO(Box<Favorites> favoritesBox) {
        this.favoritesBox = favoritesBox;
    }

    /**
     * method that will add the passed gif data to the box
     * @param GifID the gifID
     * @param thumbURL the url for the thumbnail
     * @param mp4URL the url for the mp4 to be played
     * @param gifURL the url for the gif to be downloaded
     */
    public void addFavGif(String GifID, String thumbURL, String mp4URL, String gifURL){

        Favorites fav = new Favorites();
        fav.setGif_id(GifID);
        fav.setThumbnailURL(thumbURL);
        fav.setMp4_url(mp4URL);
        fav.setGif_download_url(gifURL);

        favoritesBox.put(fav);

    }

    /**
     * method that will add the passed gif data to the box
     * @param GifID the gifID
     */
    public void removeFavGif(String GifID){
        Query<Favorites> query = favoritesBox.query().equal(Favorites_.gif_id, GifID).build();
        query.remove();

    }

    /**
     * will return the {@link Favorites} stored for the specified gifID
     * @param GifID the gifID to loog for
     * @return the {@link Favorites} element of null in case it is not found
     */
    public Favorites getFavorite(String GifID){
        List<Favorites> res =  favoritesBox.find(Favorites_.gif_id, GifID);
        if ( res != null && res.size() > 0){
            return res.get(0);
        }else{
            return null;
        }
    }

    /**
     * will return all thefavorite elements
     * @return a {@link List} of elements
     */
    public List<Favorites> getAllFavorites(){
        return favoritesBox.getAll();
    }



}
