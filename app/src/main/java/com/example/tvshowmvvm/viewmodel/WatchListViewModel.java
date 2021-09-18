package com.example.tvshowmvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tvshowmvvm.database.TVShowDatabase;
import com.example.tvshowmvvm.models.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchListViewModel extends AndroidViewModel {

    private TVShowDatabase tvShowDatabase;

    public WatchListViewModel(@NonNull Application application){
        super(application);
        tvShowDatabase = TVShowDatabase.getTvShowDatabase(application);
    }

    public Flowable<List<TvShow>> loadWatchList(){
        return tvShowDatabase.tvShowDao().getWatchList();
    }

    public Completable removeFromWatchList(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
