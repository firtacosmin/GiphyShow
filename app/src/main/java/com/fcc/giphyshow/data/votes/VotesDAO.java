package com.fcc.giphyshow.data.votes;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;

/**
 * Created by firta on 8/6/2017.
 * A DAO Class that will offer the needed interactions with {@link Votes} box
 */

public class VotesDAO {

    private Box<Votes> votesBox;

    /**
     * the latest requested {@link Votes} using {@link #getVote(String)} method
     * stored for caching
     */
    private Votes latestVote;

    @Inject
    public VotesDAO(Box<Votes> votesBox){

        this.votesBox = votesBox;
    }

    /**
     * method that will retrieve the {@link Votes} object for the given gifID
     * @param gifID the gifId to look for the {@link Votes}
     * @return the {@link Votes} object that has been found in the {@link Box<Votes>}.
     * In case there is no element in the {@link Box<Votes>} will create a default
     * {@link Votes} object with 0 for the up vote and down vote values,  will add it to the
     * {@link Box<Votes>} and will return it
     */
    public Votes getVote(String gifID){

        if( latestVote == null || latestVote.getGif_id().equals(gifID) ){
            /*if there is no vote cached or the one cached is different then the one requested then
            * cache a new one*/
            latestVote = getVotesFromBox(gifID);
        }
        return latestVote;
    }


    /**
     * Method that will return the {@link Votes#upvote_count } for the given gifID
     * @param gifID the gifID to search for in the {@link Box<Votes>}
     * @return the {@link Votes#upvote_count} for the found {@link Votes}
     */
    public int getUpVote(String gifID){
        return getVote(gifID).getUpvote_count();
    }
    /**
     * Method that will return the {@link Votes#downvote_count } for the given gifID
     * @param gifID the gifID to search for in the {@link Box<Votes>}
     * @return the {@link Votes#downvote_count} for the found {@link Votes}
     */
    public int getDownVote(String gifID){
        return getVote(gifID).getDownvote_count();
    }

    /**
     *Method to update the {@link Votes#upvote_count} and {@link Votes#downvote_count} of the {
     * @link Votes} with {@link Votes#gif_id} the same as the one passed as parameter
     * @param gifID the {@link Votes#gif_id} to search for in the {@link Box<Votes>}
     * @param upVote the {@link Votes#upvote_count} to add
     * @param downVote the {@link Votes#downvote_count} to add
     */
    public void updateVote(String gifID, int upVote, int downVote){
        Votes vote;
        if ( latestVote.getGif_id().equals(gifID) ) {
            vote = latestVote;
        }else{
            vote = new Votes();
            vote.setGif_id(gifID);
        }
        vote.setDownvote_count(downVote);
        vote.setUpvote_count(upVote);
        votesBox.put(vote);
    }


    /**
     * method that will get the {@link Votes} object from the {@link Box<Votes>}
     * @param gifID the {@link Votes#gif_id} to search for in the {@link Box<Votes>}
     * @return the resulting {@link Votes}. If it is not found in the {@link Box<Votes>} then a
     * default one will be created with 0 on the up and down votes, will be added to the {@link Box<Votes>}
     * and will be returned
     */
    private Votes getVotesFromBox(String gifID) {
        Votes vote;
        List<Votes> votes = votesBox.find(Votes_.gif_id, gifID);
        if (votes.size() > 0) {
            vote = votes.get(0);
        } else {
        /*if votes not found then add it to the box*/
            vote = new Votes();
            vote.setGif_id(gifID);
            vote.setDownvote_count(0);
            vote.setUpvote_count(0);
            votesBox.put(vote);
        }
        return vote;
    }

}
