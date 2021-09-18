package com.example.tvshowmvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowmvvm.R;
import com.example.tvshowmvvm.databinding.ItemContainerTvShowBinding;
import com.example.tvshowmvvm.listeners.TVShowListener;
import com.example.tvshowmvvm.models.TvShow;

import java.util.List;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.TVShowViewHolder>{

    private List<TvShow> list;
    private LayoutInflater layoutInflater;
    private TVShowListener tvShowListener;

    public TVShowAdapter(List<TvShow> list,TVShowListener tvShowListener) {
        this.list = list;
        this.tvShowListener = tvShowListener;
    }

    @NonNull
    @Override
    public TVShowAdapter.TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding binding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show,parent,false
        );

        return new TVShowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.TVShowViewHolder holder, int position) {
        holder.bindTVShow(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TvShow tvShow){
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(v -> tvShowListener.OnClickedTVShow(tvShow));
        }
    }
}
