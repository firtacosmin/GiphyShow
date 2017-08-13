package com.fcc.giphyshow.ui.search.view;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.Controller;
import com.fcc.giphyshow.MainActivity;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.databinding.ControllerViewSearchBinding;
import com.fcc.giphyshow.ui.search.SearchViewPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by firta on 8/5/2017.
 * The controller for the search view
 */
//@MainActivityScope
public class SearchViewController extends Controller implements ISearchView {

    public static final String TAG = "SearchViewController";



    /**
     * the event that will send the search
     */
    private PublishSubject<String> searchEvent = PublishSubject.create();
    /**
     * the event that will send the search
     */
    private PublishSubject<String> controllerStateEvent = PublishSubject.create();

    SearchViewState viewState = new SearchViewState();


    @Inject
    SearchViewPresenter presenter;
    @Inject
    SearchListAdapter adapter;

    /**
     * the list of hints that will be used in the search box hint list
     */
    ArrayList<String> hints = new ArrayList<>();

    private RecyclerView.LayoutManager mLayoutManager;
    private ControllerViewSearchBinding binding;
    private SearchView mSearchView;


    public SearchViewController(){
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {

        MainActivity act = ((MainActivity)getActivity());
        if ( act == null ){
            /*if the activity is null then the view is not attached to the ui so will do nothing*/
            return new View(null);
        }
        setHasOptionsMenu(true);


        act.getDiComponent().injectSearchViewController(this);
        /*will hold the view when detached */
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);
//        presenter.bindView(this);

        initViewstate(act);

        binding = DataBindingUtil.inflate(inflater, R.layout.controller_view_search, container, false);
        binding.setState(viewState);


        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        binding.list.setLayoutManager(mLayoutManager);
        binding.list.setAdapter(adapter);

        controllerStateEvent.onNext(CREATED);

        return binding.getRoot();
    }

    @Override
    protected void onDestroyView(@NonNull View view) {
        super.onDestroyView(view);
        controllerStateEvent.onNext(DESTROYED);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.search_menu);
        mSearchView = (SearchView) searchMenu.getActionView();
        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextFocusChangeListener((view, hasFocus) -> {
            if ( !hasFocus ){
                /*if lost focus then update the search*/
                searchEvent.onNext(mSearchView.getQuery().toString());
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEvent.onNext(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void initViewstate(MainActivity act) {
        viewState.errorMessage = act.getString(R.string.wellcome_message);
        viewState.errorVisibility = View.VISIBLE;
    }


    /**********
     *
     *
     * Implementation from {@link ISearchView}
     *
     *
     **********************/


    @Override
    public void showLoading() {
        viewState.errorVisibility = View.INVISIBLE;
        viewState.listVisibility = View.INVISIBLE;
        viewState.loadingVisibility = View.VISIBLE;
        binding.invalidateAll();

    }

    @Override
    public void hideLoading() {

        viewState.errorVisibility = View.INVISIBLE;
        viewState.listVisibility = View.VISIBLE;
        viewState.loadingVisibility = View.INVISIBLE;
        binding.invalidateAll();
    }

    @Override
    public void newItemCount(int itemCount) {
            adapter.setNewItemCount(itemCount);
            adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(int messageID) {

        if ( getApplicationContext() != null ) {
            viewState.errorMessage = getApplicationContext().getString(messageID);
            viewState.errorVisibility = View.VISIBLE;
            viewState.listVisibility = View.INVISIBLE;
            viewState.loadingVisibility = View.INVISIBLE;
            binding.invalidateAll();

        }

    }

    @Override
    public Observable<String> searchEvent() {
        return searchEvent;
    }

    @Override
    public String getQueryTest() {
        return mSearchView.getQuery().toString();
    }

    @Override
    public Observable<SearchItemView> getBindViewHolderObservable(){
        return adapter.getBindViewHolderObservable();
    }

    @Override
    public Observable<Integer> getItemClickObservable(){
        return adapter.getItemClickObservable();
    }


    @Override
    public Observable<String> getControllerStateEvent() {
        return controllerStateEvent;
    }
}
