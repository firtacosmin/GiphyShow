package com.fcc.giphyshow.ui.details;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import com.fcc.giphyshow.ui.details.model.VotesDAO;
import com.fcc.giphyshow.ui.details.view.GifDetailsView;
import com.fcc.giphyshow.ui.search.model.request.Images;
import com.fcc.giphyshow.ui.search.model.request.Looping;
import com.fcc.giphyshow.ui.search.model.request.Original;
import com.fcc.giphyshow.ui.search.model.request.SearchElement;

import static com.fcc.giphyshow.ui.details.GifDetailsPresenter.ELEMENT_BUNDLE_KEY;
import static org.mockito.Mockito.when;

/**
 * Created by firta on 8/6/2017.
 *
 * Unit tests for {@link GifDetailsPresenter} class
 * Will test the following:
 *
 * 1. When initialized will call the views
 *  {@link GifDetailsView#printLogo(String)} and
 *  {@link GifDetailsView#startPlayer(String)}
 *  methods and the {@link VotesDAO}
 *  {@link VotesDAO#getUpVote(String)} and
 *  {@link VotesDAO#getDownVote(String)} methods
 *
 * 2. When clicking the upvote button will call {@link VotesDAO#updateVote(String, int, int)} and
 * the views {@link GifDetailsView#setUpVoteCount(String)}
 *
 * 3. When clicking the downvote button will call {@link VotesDAO#updateVote(String, int, int)} and
 * the views {@link GifDetailsView#setDownVoteCount(String)}
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GifDetailsPresenterTest {

    private static final String GIF_ID = "gif_id";
    private static final String TEST_LOGO_URL = "TEST_LOGO_URL";
    private static final String TEST_MP4_URL = "TEST_MP4_URL";

    @Mock
    GifDetailsView view;
    @Mock
    VotesDAO votesDAO;
    @Mock
    SearchElement element;
    @Mock
    Images images;
    @Mock
    Original original;
    @Mock
    Looping looping;
    @Mock
    Bundle args;


    private GifDetailsPresenter presenter;



    @Before
    public void setUp() throws Exception {
//        view = Mockito.mock(GifDetailsView.class);
//        votesDAO = Mockito.mock(VotesDAO.class);


    }

    @After
    public void tearDown() throws Exception {

    }
    @Test
    public void initializationTest() throws Exception{



        when(view.getArgs()).thenReturn(args);
        when(args.getSerializable(ELEMENT_BUNDLE_KEY)).thenReturn(element);

        when(element.getId()).thenReturn(GIF_ID);
        when(element.getImages()).thenReturn(images);
        when(images.getOriginal()).thenReturn(original);
        when(original.getUrl()).thenReturn(TEST_LOGO_URL);
        when(images.getLooping()).thenReturn(looping);
        when(looping.getMp4()).thenReturn(TEST_MP4_URL);

        when(votesDAO.getDownVote(GIF_ID)).thenReturn(10);
        when(votesDAO.getUpVote(GIF_ID)).thenReturn(5);

        presenter = new GifDetailsPresenter(view, votesDAO);

        Mockito.verify(view).printLogo(TEST_LOGO_URL);
        Mockito.verify(view).startPlayer(TEST_MP4_URL);
        Mockito.verify(view).setDownVoteCount("10");
        Mockito.verify(view).setUpVoteCount("5");
    }

    @Test
    public void downVoteClicked() throws Exception {
        initializationTest();
        presenter.downVoteClicked();

        Mockito.verify(view).setDownVoteCount("11");
        Mockito.verify(votesDAO).updateVote(GIF_ID, 5,11);
    }

    @Test
    public void upVoteClick() throws Exception {
        initializationTest();
        presenter.upVoteClick();

        Mockito.verify(view).setUpVoteCount("6");
        Mockito.verify(votesDAO).updateVote(GIF_ID, 6,10);

    }

}