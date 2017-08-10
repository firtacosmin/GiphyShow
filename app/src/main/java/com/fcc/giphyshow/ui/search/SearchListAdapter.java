package com.fcc.giphyshow.ui.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fcc.giphyshow.R;
import com.fcc.giphyshow.di.mainActivity.MainActivityScope;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Module;

/**
 * Created by firta on 8/5/2017.
 * The adapter for the recycler view in the {@link SearchViewController}
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListItemHolder>{

    private final Picasso picasso;
    private final SearchViewPresenter presenter;
    private int itemCount = 0;

    public SearchListAdapter(Picasso picasso, SearchViewPresenter presenter){

        this.picasso = picasso;
        this.presenter = presenter;
    }

    public void setNewItemCount(int newItemCount){
        itemCount = newItemCount;
    }

    @Override
    public SearchListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_view, parent, false);

        return new SearchListItemHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchListItemHolder holder, int position) {
        presenter.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(view ->{
            presenter.itemClicked(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }


    /**
     * the {@link android.support.v7.widget.RecyclerView.ViewHolder} used to display the items in
     * the {@link SearchListAdapter}
     */
    class SearchListItemHolder extends RecyclerView.ViewHolder implements SearchItemView{

//        @BindView(R.id.item_desc)
//        TextView item_desc;
        @BindView(R.id.item_gif_thumb)
        ImageView item_gif_thumb;
        @BindView(R.id.loading)
        ProgressBar loading;


        public SearchListItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void setImage(String imageURL) {
            picasso.load(imageURL).into(item_gif_thumb);
//            Glide.with(item_gif_thumb.getContext()).load(imageURL).into(item_gif_thumb);
        }

        @Override
        public void setDesc(String description) {
//            item_desc.setText(description);
        }

        @Override
        public void showLoading() {
            loading.setVisibility(View.VISIBLE);
            item_gif_thumb.setVisibility(View.INVISIBLE);
//            item_desc.setVisibility(View.INVISIBLE);
        }

        @Override
        public void hideLoading() {

            loading.setVisibility(View.INVISIBLE);
            item_gif_thumb.setVisibility(View.VISIBLE);
//            item_desc.setVisibility(View.VISIBLE);
        }
    }

}
