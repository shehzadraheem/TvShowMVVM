package com.example.tvshowmvvm.listeners;

import com.example.tvshowmvvm.models.TvShow;

public interface WatchListener {

    void onTVShowClicked(TvShow tvShow);

    void removeFromWatchList(TvShow tvShow , int position);
}
