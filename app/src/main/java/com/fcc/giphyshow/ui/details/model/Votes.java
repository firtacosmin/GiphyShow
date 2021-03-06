package com.fcc.giphyshow.ui.details.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.apihint.Internal;

/**
 * Created by firta on 8/6/2017.
 * the {@link Entity} that will contain the voting information for a specific gif
 */

@Entity
public class Votes {
    @Id
    private long  id;
    @Index
    private String gif_id;
    private int upvote_count;
    private int downvote_count;
    @Generated(1425843020)
    @Internal
    /** This constructor was generated by ObjectBox and may change any time. */
    public Votes(long id, String gif_id, int upvote_count, int downvote_count) {
        this.id = id;
        this.gif_id = gif_id;
        this.upvote_count = upvote_count;
        this.downvote_count = downvote_count;
    }
    @Generated(1825484516)
    public Votes() {
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getGif_id() {
        return gif_id;
    }
    public void setGif_id(String gif_id) {
        this.gif_id = gif_id;
    }
    public int getUpvote_count() {
        return upvote_count;
    }
    public void setUpvote_count(int upvote_count) {
        this.upvote_count = upvote_count;
    }
    public int getDownvote_count() {
        return downvote_count;
    }
    public void setDownvote_count(int downvote_count) {
        this.downvote_count = downvote_count;
    }

}
