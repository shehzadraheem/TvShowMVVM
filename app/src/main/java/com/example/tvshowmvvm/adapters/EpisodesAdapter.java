package com.example.tvshowmvvm.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowmvvm.R;
import com.example.tvshowmvvm.databinding.ItemContainerEpisodeBinding;
import com.example.tvshowmvvm.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>{


    private List<Episode> list;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = layoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding itemContainerEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_episode,parent,false);
        return new EpisodeViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
      holder.bindEpisode(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;


        }

        public void bindEpisode(Episode episode){

            String title = "S";
            String season = episode.getSeason();
            if(season.length() == 1){
                season= "0".concat(season);
            }
            String episodeNumber = episode.getEpisode();
            if(episodeNumber.length() == 1){
                episodeNumber = "0".concat(episodeNumber);
            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            itemContainerEpisodeBinding.setTitle(title);
            itemContainerEpisodeBinding.setName(episode.getName());
            itemContainerEpisodeBinding.setAirDate(episode.getAirDate());
        }
    }
}

