package com.fcc.giphyshow.ui.search;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bluelinelabs.conductor.Controller;
import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.MainApp;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by firta on 8/5/2017.
 * The controller for the search view
 */
//@MainActivityScope
public class SearchViewController extends Controller implements SearchView{

    @BindView(R.id.list)
    RecyclerView theSearchList;

    @BindView(R.id.searchbox)
    SearchBox theSearchBox;

    @BindView(R.id.progressBar)
    ProgressBar loading;

    @BindView(R.id.error_message)
    TextView theErrorTextView;

    @Inject
    SearchViewPresenter presenter;
    @Inject
    SearchListAdapter adapter;

    /**
     * the list of hints that will be used in the search box hint list
     */
    ArrayList<String> hints = new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;



    public SearchViewController(){

    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {


        ((MainActivity)getActivity()).getDiComponent().injectSearchViewController(this);
        /*will hold the view when detached */
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);
        presenter.bindView(this);



        View view = inflater.inflate(R.layout.controller_view_search, container, false);
        ButterKnife.bind(this, view);


        mLayoutManager = new LinearLayoutManager(getActivity());
        theSearchList.setLayoutManager(mLayoutManager);
        theSearchList.setAdapter(adapter);


        theSearchBox.enableVoiceRecognition(getActivity());
        theSearchBox.setLogoText("Giphy Show");
        theSearchBox.setSearchListener(new SearchBox.SearchListener() {
            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String s) {

            }

            @Override
            public void onSearch(String s) {
                SearchResult option = new SearchResult(s);
                theSearchBox.addSearchable(option);
                if ( presenter != null ) {
                    presenter.searchPressed(s);
                }
            }

            @Override
            public void onResultClick(SearchResult searchResult) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (isAdded() && requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == getActivity().RESULT_OK) {
//            ArrayList<String> matches = data
//                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            search.populateEditText(matches);
//        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**********
     *
     *
     * Implementation from {@link SearchView}
     *
     *
     **********************/


    @Override
    public void showLoading() {

        theSearchList.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        theErrorTextView.setVisibility(View.INVISIBLE);


    }

    @Override
    public void hideLoading() {
        theSearchList.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
        theErrorTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void newItemCount(int itemCount) {
//        if ( adapter.getItemCount() == itemCount ){
            adapter.setNewItemCount(itemCount);
            adapter.notifyDataSetChanged();
//        }else {
//            adapter.notifyItemRangeChanged(adapter.getItemCount(), itemCount);
//        }
    }

    @Override
    public void showError(String message) {
        theErrorTextView.setText(message);
        theErrorTextView.setVisibility(View.VISIBLE);
        theSearchList.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void bindPresenter(SearchViewPresenter preseter) {
        presenter = preseter;
    }
}
